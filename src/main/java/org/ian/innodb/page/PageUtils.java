package org.ian.innodb.page;

import java.util.zip.CRC32C;

public class PageUtils {

	public static final int FIL_PAGE_SIZE = 16384;
	public static final int FIL_TRAILER_SIZE = 8;

	private static final int FIL_HEADER_OFFSET = 0;
	public static final int FIL_HEADER_SIZE = 38;

	public static final int FIL_HEADER_PARTIAL_OFFSET = 4;
	public static final int FIL_HEADER_PARTIAL_SIZE = FIL_HEADER_SIZE - 4 - 8 - 4;

	public static final int FIL_PAGE_BODY_OFFSET = FIL_HEADER_OFFSET + FIL_HEADER_SIZE;
	public static final int FIL_PAGE_BODY_SIZE = FIL_PAGE_SIZE - FIL_HEADER_SIZE - FIL_TRAILER_SIZE;

	private static final int HASH_RANDOM_MASK = 1463735687;
	private static final int HASH_RANDOM_MASK2 = 1653893711;

	private static final CRC32C CRC32C_INSTANCE = new CRC32C();

	public static int foldPair(int n1, int n2) {
		return (((n1 ^ n2 ^ HASH_RANDOM_MASK2) << 8) + n1) ^ HASH_RANDOM_MASK + n2;
	}

	public static int foldBytes(byte[] buf) {
		int fold = 0;

		for (byte b : buf) {
			fold = foldPair(fold, b & 0xFF);
		}

		return fold;
	}

	public static int crc32Checksum(byte[] data) {
		CRC32C_INSTANCE.reset();
		CRC32C_INSTANCE.update(data);

		return (int) CRC32C_INSTANCE.getValue();
	}

}
