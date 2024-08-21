package org.ian.innodb.page;

import java.nio.ByteBuffer;
import java.util.Optional;

public record FILTrailer(int oldChecksum, int lsnLow32) {

	public static Optional<FILTrailer> fromBytes(byte[] buffer) {
		if (buffer.length != PageUtils.FIL_TRAILER_SIZE) {
			return Optional.empty();
		}

		ByteBuffer buf = ByteBuffer.wrap(buffer);

		int oldChecksum = buf.getInt();
		int lsnLow32 = buf.getInt();

		return Optional.of(new FILTrailer(oldChecksum, lsnLow32));
	}

	@Override
	public String toString() {
		return "FILTrailer{" +
				"oldChecksum=" + oldChecksum +
				", lsnLow32=" + lsnLow32 +
				'}';
	}

}
