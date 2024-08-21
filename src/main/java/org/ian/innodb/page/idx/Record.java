package org.ian.innodb.page.idx;

import java.util.Optional;

public record Record(RecordHeader header, int offset, byte[] buf) {

	public static final int RECORD_HEADER_FIXED_LENGTH = 5;

	public static Optional<Record> tryFromOffset(byte[] buffer, int offset) {
		return RecordHeader.tryFromOffset(buffer, offset).map(header -> new Record(header, offset, buffer));
	}

	public Optional<Record> next() {
		if (header.getRecordType() == RecordType.SUPREMUM)
			return Optional.empty();

		return Record.tryFromOffset(buf, header.getNextRecordOffset());
	}

	@Override
	public String toString() {
		return "Record{" +
				"header=" + header +
				", offset=" + offset +
				'}';
	}

}
