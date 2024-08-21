package org.ian.innodb.page;

import java.util.Arrays;
import java.util.Optional;

public record Page(FILHeader header, FILTrailer trailer, byte[] rawData) {

	public static Page fromBytes(byte[] buf) {
		if (buf.length != PageUtils.FIL_PAGE_SIZE)
			return null;

		Optional<FILHeader> header = FILHeader.fromBytes(Arrays.copyOfRange(buf, 0, PageUtils.FIL_HEADER_SIZE));
		Optional<FILTrailer> trailer = FILTrailer.fromBytes(Arrays.copyOfRange(buf, PageUtils.FIL_PAGE_SIZE - PageUtils.FIL_TRAILER_SIZE, PageUtils.FIL_PAGE_SIZE));

		if (header.isPresent() && trailer.isPresent())
			return new Page(header.get(), trailer.get(), buf);
		else
			return null;
	}

	public byte[] partialPageHeader() {
		return Arrays.copyOfRange(rawData, PageUtils.FIL_HEADER_PARTIAL_OFFSET, PageUtils.FIL_HEADER_PARTIAL_OFFSET + PageUtils.FIL_HEADER_PARTIAL_SIZE);
	}

	public byte[] getBody() {
		return Arrays.copyOfRange(rawData, PageUtils.FIL_PAGE_BODY_OFFSET, PageUtils.FIL_PAGE_BODY_OFFSET + PageUtils.FIL_PAGE_BODY_SIZE);
	}

	public int innodbChecksum() {
		int headerChecksum = PageUtils.foldBytes(partialPageHeader());
		int bodyChecksum = PageUtils.foldBytes(getBody());
		return headerChecksum + bodyChecksum;
	}

	public int crc32Checksum() {
		return PageUtils.crc32Checksum(partialPageHeader()) ^ PageUtils.crc32Checksum(getBody());
	}


	@Override
	public String toString() {
		return "Page{" +
				"header=" + header +
				", trailer=" + trailer +
				'}';
	}

}
