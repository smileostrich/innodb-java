package org.ian.innodb.page;

import java.nio.ByteBuffer;
import java.util.Optional;

public record FILHeader(int newChecksum, int offset, int prev, int next, long lsn, PageType pageType, long flushLsn, int spaceId) {

	public static Optional<FILHeader> fromBytes(byte[] buffer) {
		if (buffer.length < PageUtils.FIL_HEADER_SIZE)
			return Optional.empty();

		ByteBuffer buf = ByteBuffer.wrap(buffer);

		int newChecksum = buf.getInt();
		int offset = buf.getInt();
		int prev = buf.getInt();
		int next = buf.getInt();
		long lsn = buf.getLong();
		int pageTypeValue = buf.getShort() & 0xFFFF;
		PageType pageType = PageType.fromValue(pageTypeValue);
		long flushLsn = buf.getLong();
		int spaceId = buf.getInt();

		return Optional.of(new FILHeader(newChecksum, offset, prev, next, lsn, pageType, flushLsn, spaceId));
	}

	@Override
	public String toString() {
		return "FILHeader{" +
				"newChecksum=" + newChecksum +
				", offset=" + offset +
				", prev=" + prev +
				", next=" + next +
				", lsn=" + lsn +
				", pageType=" + pageType +
				", flushLsn=" + flushLsn +
				", spaceId=" + spaceId +
				'}';
	}

}
