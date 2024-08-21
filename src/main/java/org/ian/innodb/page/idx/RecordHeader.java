package org.ian.innodb.page.idx;

import java.nio.ByteBuffer;
import java.util.Optional;

public class RecordHeader {

	private final InfoFlags infoFlags;
	private final byte numRecordsOwned;
	private final int order;
	private final RecordType recordType;
	private final Integer nextRecordOffset;

	public RecordHeader(InfoFlags infoFlags, byte numRecordsOwned, int order, RecordType recordType, Integer nextRecordOffset) {
		this.infoFlags = infoFlags;
		this.numRecordsOwned = numRecordsOwned;
		this.order = order;
		this.recordType = recordType;
		this.nextRecordOffset = nextRecordOffset;
	}

	public RecordType getRecordType() {
		return recordType;
	}

	public static Optional<RecordHeader> tryFromOffset(byte[] buffer, int offset) {
		if (offset < Record.RECORD_HEADER_FIXED_LENGTH) {
			return Optional.empty(); // Invalid length
		}

		ByteBuffer buf = ByteBuffer.wrap(buffer);
		int recordTypeOrder = buf.getShort(offset - 4) & 0xFFFF;
		byte ownedFlags = buf.get(offset - 5);

		Optional<InfoFlags> infoFlags = InfoFlags.tryFromPrimitive((byte) (ownedFlags >> 4));
		if (infoFlags.isEmpty()) {
			return Optional.empty();
		}

		byte numRecordsOwned = (byte) (ownedFlags & 0xF);
		int order = recordTypeOrder >> 3;
		Optional<RecordType> recordType = RecordType.fromValue(recordTypeOrder & 0x7);

		if (recordType.isEmpty()) {
			return Optional.empty();
		}

		int nextOffset = buf.getShort(offset - 2);
		Integer nextRecordOffset = nextOffset == 0 ? null : offset + nextOffset;

		return Optional.of(new RecordHeader(infoFlags.get(), numRecordsOwned, order, recordType.get(), nextRecordOffset));
	}

	public int getNextRecordOffset() {
		return nextRecordOffset;
	}

	@Override
	public String toString() {
		return "RecordHeader{" +
				"infoFlags=" + infoFlags +
				", numRecordsOwned=" + numRecordsOwned +
				", order=" + order +
				", recordType=" + recordType +
				", nextRecordOffset=" + nextRecordOffset +
				'}';
	}

}
