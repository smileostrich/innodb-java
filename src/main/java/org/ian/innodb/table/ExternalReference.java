package org.ian.innodb.table;

import java.nio.ByteBuffer;

public record ExternalReference(int spaceId, int pageNumber, int offset, boolean owner, boolean inherit, long length) {

	public static ExternalReference fromBytes(byte[] bytes) {
		if (bytes.length < 20)
			return null; // Insufficient bytes to construct BlobHeader

		ByteBuffer buffer = ByteBuffer.wrap(bytes);

		int spaceId = buffer.getInt();
		int pageNumber = buffer.getInt();
		int offset = buffer.getInt();
		long length = buffer.getLong();

		boolean owner = (length & 0x8000000000000000L) == 0;
		boolean inherit = (length & 0x4000000000000000L) != 0;
		length &= 0x0FFFFFFFFFFFFFFFL;

		return new ExternalReference(spaceId, pageNumber, offset, owner, inherit, length);
	}

	// Getters for the fields can be added as needed
	public int spaceId() {
		return spaceId;
	}

}