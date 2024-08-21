package org.ian.innodb;

import java.util.Optional;

public enum InnoDBCharset {

	ARMSCII8(1),
	ASCII(1),
	BIG5(2),
	BINARY(1),
	CP1250(1),
	CP1251(1),
	CP1256(1),
	CP1257(1),
	CP850(1),
	CP852(1),
	CP866(1),
	CP932(2),
	DEC8(1),
	EUCJPMS(3),
	EUCKR(2),
	GB18030(4),
	GB2312(2),
	GBK(2),
	GEOSTD8(1),
	GREEK(1),
	HEBREW(1),
	HP8(1),
	KEYBCS2(1),
	KOI8R(1),
	KOI8U(1),
	LATIN1(1),
	LATIN2(1),
	LATIN5(1),
	LATIN7(1),
	MACCE(1),
	MACROMAN(1),
	SJIS(2),
	SWE7(1),
	TIS620(1),
	UCS2(2),
	UJIS(3),
	UTF16(4),
	UTF16LE(4),
	UTF32(4),
	UTF8MB3(3),
	UTF8MB4(4);

	private final int maxLen;

	InnoDBCharset(int maxLen) {
		this.maxLen = maxLen;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public static Optional<InnoDBCharset> withName(String name) {
		try {
			return Optional.of(InnoDBCharset.valueOf(name.toUpperCase()));
		} catch (IllegalArgumentException e) {
			return Optional.empty(); // Handle unknown charset name
		}
	}

}
