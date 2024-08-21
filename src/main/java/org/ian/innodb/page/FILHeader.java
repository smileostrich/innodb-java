package org.ian.innodb.page;

import java.nio.ByteBuffer;
import java.util.Optional;

public class FILHeader {
	private final int newChecksum;
	private final int offset;
	private final int prev;
	private final int next;
	private final long lsn;
	private final PageType pageType;
	private final long flushLsn;
	private final int spaceId;

	public FILHeader(int newChecksum, int offset, int prev, int next, long lsn, PageType pageType, long flushLsn, int spaceId) {
		this.newChecksum = newChecksum;
		this.offset = offset;
		this.prev = prev;
		this.next = next;
		this.lsn = lsn;
		this.pageType = pageType;
		this.flushLsn = flushLsn;
		this.spaceId = spaceId;
	}

	public static Optional<FILHeader> fromBytes(byte[] buffer) {
		if (buffer.length < PageUtils.FIL_HEADER_SIZE) {
			return Optional.empty();
		}

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

	public int getNewChecksum() {
		return newChecksum;
	}

	public int getOffset() {
		return offset;
	}

	public int getPrev() {
		return prev;
	}

	public int getNext() {
		return next;
	}

	public long getLsn() {
		return lsn;
	}

	public PageType getPageType() {
		return pageType;
	}

	public long getFlushLsn() {
		return flushLsn;
	}

	public int getSpaceId() {
		return spaceId;
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
