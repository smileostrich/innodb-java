package org.ian.innodb.page.lob;

import org.ian.innodb.page.Page;
import org.ian.innodb.page.PageType;

import java.util.Optional;

public record LobData(Page page, LobDataHeader header) {

	public static Optional<LobData> tryFromPage(Page page) {
		if (page.header().getPageType() == PageType.LOB_DATA) {
			return LobDataHeader.tryFromBytes(page.getBody()).map(header -> new LobData(page, header));
		} else {
			return Optional.empty();  // Handle InnoDBError.InvalidPageType appropriately
		}
	}

	public int read(int offset, byte[] buf) {
		int dataLen = header.dataLen();
		byte[] data = body();
		if (offset >= data.length)
			throw new IllegalArgumentException("Offset too large");

		int bytesToCopy = Math.min(buf.length, data.length - offset);
		System.arraycopy(data, offset, buf, 0, bytesToCopy);
		return bytesToCopy;
	}

	public byte[] body() {
		byte[] body = new byte[header.dataLen()];
		System.arraycopy(page.getBody(), LobDataHeader.size(), body, 0, header.dataLen());
		return body;
	}


	@Override
	public String toString() {
		return "LobData{" +
				"page=" + page +
				", header=" + header +
				'}';
	}

}
