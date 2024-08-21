package org.ian.innodb.page;

public enum PageType {
	ALLOCATED(0),
	UNDO_LOG(2),
	INODE(3),
	IBUF_FREE_LIST(4),
	IBUF_BITMAP(5),
	SYS(6),
	TRX_SYS(7),
	FSP_HDR(8),
	XDES(9),
	BLOB(10),
	ZBLOB(11),
	ZBLOB2(12),
	UNKNOWN(13),
	COMPRESSED(14),
	ENCRYPTED(15),
	COMPRESSED_AND_ENCRYPTED(16),
	ENCRYPTED_RTREE(17),
	SDI_BLOB(18),
	SDI_ZBLOB(19),
	LEGACY_DBLWR(20),
	RSEG_ARRAY(21),
	LOB_INDEX(22),
	LOB_DATA(23),
	LOB_FIRST(24),
	ZLOB_FIRST(25),
	ZLOB_DATA(26),
	ZLOB_INDEX(27),
	ZLOB_FRAG(28),
	ZLOB_FRAG_ENTRY(29),
	SDI(17853),
	RTREE(17854),
	INDEX(17855);

	private final int value;

	PageType(int value) {
		this.value = value;
	}

	public static PageType fromValue(int value) {
		for (PageType type : values()) {
			if (type.value == value) {
				return type;
			}
		}
		return UNKNOWN;
	}

}
