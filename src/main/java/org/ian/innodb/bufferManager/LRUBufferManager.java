package org.ian.innodb.bufferManager;

import org.ian.innodb.page.Page;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LRUBufferManager implements BufferManager {

	private static final int FIL_PAGE_SIZE = 16384;
	private static final int LRU_PAGE_COUNT = 16;

	private final byte[][] backingStore;
	private final AtomicInteger[] pagePinCounter;
	private final Path pageDirectory;
	private final Map<Long, Integer> pagePinMap;
	private final long[] lruList;

	public LRUBufferManager(Path dir) {
		this.backingStore = new byte[LRU_PAGE_COUNT][FIL_PAGE_SIZE];
		this.pagePinCounter = new AtomicInteger[LRU_PAGE_COUNT];
		for (int i = 0; i < LRU_PAGE_COUNT; i++) {
			this.pagePinCounter[i] = new AtomicInteger(0);
		}
		this.pageDirectory = dir.toAbsolutePath();
		this.pagePinMap = new HashMap<>();
		this.lruList = new long[LRU_PAGE_COUNT];
		Arrays.fill(lruList, 0L);
	}

	private int findFree() {
		long minTimestamp = Long.MAX_VALUE;
		int resultFrame = 0;

		for (int i = 0; i < lruList.length; i++) {
			if (lruList[i] == 0) {
				return i;
			}
			if (lruList[i] < minTimestamp && pagePinCounter[i].get() == 0) {
				minTimestamp = lruList[i];
				resultFrame = i;
			}
		}

		if (minTimestamp != Long.MAX_VALUE) {
			for (Map.Entry<Long, Integer> entry : pagePinMap.entrySet()) {
				if (entry.getValue().equals(resultFrame)) {
					pagePinMap.remove(entry.getKey());
					break;
				}
			}
			lruList[resultFrame] = 0;
			return resultFrame;
		} else {
			throw new IllegalStateException("Pinned too many pages.");
		}
	}

	@Override
	public PageGuard pin(int spaceId, int offset) throws IOException {
		long currentTime = System.currentTimeMillis();

		long pageKey = ((long) spaceId << 32) | (offset & 0xFFFFFFFFL);

		if (pagePinMap.containsKey(pageKey)) {
			int frameNumber = pagePinMap.get(pageKey);
			pagePinCounter[frameNumber].incrementAndGet();
			lruList[frameNumber] = currentTime;
			Page page = Page.fromBytes(backingStore[frameNumber]);
			return new PageGuard(page, this);
		}

		try (RandomAccessFile file = new RandomAccessFile(new File(pageDirectory.toString(), String.format("%08d.pages", spaceId)), "r")) {
			file.seek((long) offset * FIL_PAGE_SIZE);
			int freeFrame = findFree();
			file.readFully(backingStore[freeFrame]);

			Page page = Page.fromBytes(backingStore[freeFrame]);
			if (Objects.requireNonNull(page).header().getSpaceId() == 0 && page.header().getOffset() == 0)
				throw new IOException("Page not found.");

			if (page.header().getSpaceId() != spaceId || page.header().getOffset() != offset)
				throw new IOException("Checksum mismatch.");

			lruList[freeFrame] = currentTime;
			pagePinCounter[freeFrame].incrementAndGet();
			pagePinMap.put(pageKey, freeFrame);

			return new PageGuard(page, this);
		} catch (IOException e) {
			throw new FileNotFoundException("Page not found.");
		}
	}

	@Override
	public void unpin(Page page) {
		long pageKey = ((long) page.header().getSpaceId() << 32) | (page.header().getOffset() & 0xFFFFFFFFL);
		Integer frameNumber = pagePinMap.get(pageKey);

		if (frameNumber != null) {
			pagePinCounter[frameNumber].decrementAndGet();
		} else {
			throw new IllegalStateException("Unpinning a non-pinned page.");
		}
	}

	@Override
	public String toString() {
		return "LRUBufferManager{" +
				"pagePinCounter=" + Arrays.toString(pagePinCounter) +
				", pageDirectory=" + pageDirectory +
				", pagePinMap=" + pagePinMap +
				", lruList=" + Arrays.toString(lruList) +
				'}';
	}

}
