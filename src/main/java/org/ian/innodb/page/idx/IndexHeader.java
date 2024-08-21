package org.ian.innodb.page.idx;

import java.nio.ByteBuffer;

public record IndexHeader(
		int numberOfDirectorySlots,
		int heapTopPosition,
		IndexFormat format,
		int numberOfHeapRecords,
		int firstGarbageRecordOffset,
		int garbageSpace,
		int lastInsertPosition,
		PageDirection pageDirection,
		int numberOfInsertsInPageDirection,
		int numberOfRecords,
		long maximumTransactionId,
		int pageLevel,
		long indexId
) {
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
