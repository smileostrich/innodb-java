package org.ian.innodb;

import java.nio.ByteBuffer;
import java.util.Optional;

public record FileAddress(int pageNumber, int offset) {
	public static final int FIL_NULL = 0xFFFF_FFFF;

	public static Optional<FileAddress> tryFromBytes(byte[] buf) {
		if (buf.length < 6)
			return Optional.empty();

		int pageNumber = ByteBuffer.wrap(buf, 0, 4).getInt();
		int offset = ByteBuffer.wrap(buf, 4, 2).getShort() & 0xFFFF;
		return Optional.of(new FileAddress(pageNumber, offset));
	}

	public boolean isNull() {
		return this.pageNumber == FIL_NULL;
	}

	public static int size() {
		return 6;
	}

	@Override
	public String toString() {
		return "FileAddress{" +
				"pageNumber=" + pageNumber +
				", offset=" + offset +
				'}';
	}

}
