package org.ian.innodb.page.idx;

public enum IndexFormat {

	REDUNDANT(0),
	COMPACT(1);

	private final int value;

	IndexFormat(int value) {
		this.value = value;
	}

	public static IndexFormat fromValue(int value) {
		for (IndexFormat format : values()) {
			if (format.value == value) {
				return format;
			}
		}
		throw new IllegalArgumentException("Invalid IndexFormat value: " + value);
	}

}
