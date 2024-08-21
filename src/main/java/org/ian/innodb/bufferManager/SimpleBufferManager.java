package org.ian.innodb.bufferManager;

import org.ian.innodb.page.Page;
import org.ian.innodb.page.PageUtils;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SimpleBufferManager implements BufferManager {
	private final Path pageDirectory;
	private final Map<PageKey, byte[]> pageCache;

	public SimpleBufferManager(String dir) {
		this.pageDirectory = Paths.get(dir);
		this.pageCache = new HashMap<>();
	}

	private byte[] getPage(int spaceId, int offset) throws IOException {
		PageKey key = new PageKey(spaceId, offset);
		if (pageCache.containsKey(key)) {
			return pageCache.get(key);
		}

		Path path = pageDirectory.resolve(String.format("%08d.pages", spaceId));
		try (RandomAccessFile file = new RandomAccessFile(path.toFile(), "r")) {
			file.seek((long) offset * PageUtils.FIL_PAGE_SIZE);
			byte[] buffer = new byte[PageUtils.FIL_PAGE_SIZE];
			file.readFully(buffer);
			pageCache.put(key, buffer);
			return buffer;
		}
	}

	@Override
	public PageGuard pin(int spaceId, int offset) throws IOException {
		byte[] buffer = getPage(spaceId, offset);
		System.out.println("Opened (" + spaceId + ", " + offset + ")");
		return new PageGuard(Page.fromBytes(buffer), this);
	}

	@Override
	public void unpin(Page page) {
		System.out.println("Closed (" + page.header().spaceId() + ", " + page.header().offset() + ")");
	}

	private record PageKey(int spaceId, int offset) {
	}

}
