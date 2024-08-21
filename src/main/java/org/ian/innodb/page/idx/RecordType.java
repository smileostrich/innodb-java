package org.ian.innodb.page.idx;

import java.util.Optional;

public enum RecordType {

	CONVENTIONAL(0),
	NODE_POINTER(1),
	INFIMUM(2),
	SUPREMUM(3);

	private final int value;

	RecordType(int value) {
		this.value = value;
	}

	public static Optional<RecordType> fromValue(int value) {
		for (RecordType type : values()) {
			if (type.value == value) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}

}
