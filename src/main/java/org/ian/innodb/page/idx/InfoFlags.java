package org.ian.innodb.page.idx;

import java.util.Optional;

public class InfoFlags {

	private final boolean minRec;
	private final boolean deleted;

	public InfoFlags(boolean minRec, boolean deleted) {
		this.minRec = minRec;
		this.deleted = deleted;
	}

	public static Optional<InfoFlags> tryFromPrimitive(byte flags) {
		if ((flags & ~0x3) != 0) {
			return Optional.empty(); // Unexpected bitfield value
		}

		boolean minRec = (flags & 0x1) != 0;
		boolean deleted = (flags & 0x2) != 0;

		return Optional.of(new InfoFlags(minRec, deleted));
	}

	public boolean isMinRec() {
		return minRec;
	}

	public boolean isDeleted() {
		return deleted;
	}

	@Override
	public String toString() {
		return "InfoFlags{" +
				"minRec=" + minRec +
				", deleted=" + deleted +
				'}';
	}

}
