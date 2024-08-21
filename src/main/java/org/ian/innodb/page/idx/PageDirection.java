package org.ian.innodb.page.idx;

public enum PageDirection {

	LEFT(1),
	RIGHT(2),
	SAME_REC(3),  // Not Used
	SAME_PAGE(4), // Not Used
	NO_DIRECTION(5);

	private final int value;

	PageDirection(int value) {
		this.value = value;
	}

	public static PageDirection fromValue(int value) {
		for (PageDirection direction : values()) {
			if (direction.value == value) {
				return direction;
			}
		}
		throw new IllegalArgumentException("Invalid PageDirection value: " + value);
	}

}
