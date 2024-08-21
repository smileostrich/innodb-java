package org.ian.innodb.page.idx;

import java.nio.ByteBuffer;

public class IndexHeader {

	private final int numberOfDirectorySlots;
	private final int heapTopPosition;
	private final IndexFormat format;
	private final int numberOfHeapRecords;
	private final int firstGarbageRecordOffset;
	private final int garbageSpace;
	private final int lastInsertPosition;
	private final PageDirection pageDirection;
	private final int numberOfInsertsInPageDirection;
	private final int numberOfRecords;
	private final long maximumTransactionId;
	private final int pageLevel;
	private final long indexId;

	public IndexHeader(int numberOfDirectorySlots, int heapTopPosition, IndexFormat format, int numberOfHeapRecords,
	                   int firstGarbageRecordOffset, int garbageSpace, int lastInsertPosition, PageDirection pageDirection,
	                   int numberOfInsertsInPageDirection, int numberOfRecords, long maximumTransactionId,
	                   int pageLevel, long indexId) {
		this.numberOfDirectorySlots = numberOfDirectorySlots;
		this.heapTopPosition = heapTopPosition;
		this.format = format;
		this.numberOfHeapRecords = numberOfHeapRecords;
		this.firstGarbageRecordOffset = firstGarbageRecordOffset;
		this.garbageSpace = garbageSpace;
		this.lastInsertPosition = lastInsertPosition;
		this.pageDirection = pageDirection;
		this.numberOfInsertsInPageDirection = numberOfInsertsInPageDirection;
		this.numberOfRecords = numberOfRecords;
		this.maximumTransactionId = maximumTransactionId;
		this.pageLevel = pageLevel;
		this.indexId = indexId;
	}

	public static IndexHeader fromBytes(byte[] data) {
		if (data.length < 36)
			throw new IllegalArgumentException("Data slice is too short");

		ByteBuffer buffer = ByteBuffer.wrap(data);

		int numberOfDirectorySlots = buffer.getShort() & 0xFFFF;
		int heapTopPosition = buffer.getShort() & 0xFFFF;

		int formatAndHeapRecords = buffer.getShort() & 0xFFFF;
		IndexFormat format = (formatAndHeapRecords & 0x8000) == 0 ? IndexFormat.REDUNDANT : IndexFormat.COMPACT;
		int numberOfHeapRecords = formatAndHeapRecords & 0x7FFF;

		int firstGarbageRecordOffset = buffer.getShort() & 0xFFFF;
		int garbageSpace = buffer.getShort() & 0xFFFF;
		int lastInsertPosition = buffer.getShort() & 0xFFFF;

		PageDirection pageDirection = PageDirection.fromValue(buffer.getShort() & 0xFFFF);

		int numberOfInsertsInPageDirection = buffer.getShort() & 0xFFFF;
		int numberOfRecords = buffer.getShort() & 0xFFFF;

		long maximumTransactionId = buffer.getLong();
		int pageLevel = buffer.getShort() & 0xFFFF;
		long indexId = buffer.getLong();

		return new IndexHeader(
				numberOfDirectorySlots, heapTopPosition, format, numberOfHeapRecords,
				firstGarbageRecordOffset, garbageSpace, lastInsertPosition, pageDirection,
				numberOfInsertsInPageDirection, numberOfRecords, maximumTransactionId,
				pageLevel, indexId
		);
	}

}
