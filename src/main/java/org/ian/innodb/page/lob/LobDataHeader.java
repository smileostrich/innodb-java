package org.ian.innodb.page.lob;

import java.nio.ByteBuffer;
import java.util.Optional;

public record LobDataHeader(byte version, int dataLen, long trxId) {

	public static int size() {
		return 1 + 4 + 6;
	}

	public static Optional<LobDataHeader> tryFromBytes(byte[] buf) {
		if (buf.length < 11)
			return Optional.empty();  // Buffer too short for LobDataHeader

		byte version = buf[0];
		int dataLen = ByteBuffer.wrap(buf, 1, 4).getInt();

		// trx_id is 6 bytes, so pad with two zero bytes for u64
		long trxId = ByteBuffer.wrap(new byte[]{0, 0, buf[5], buf[6], buf[7], buf[8], buf[9], buf[10]}).getLong();

		return Optional.of(new LobDataHeader(version, dataLen, trxId));
	}


	@Override
	public String toString() {
		return "LobDataHeader{" +
				"version=" + version +
				", dataLen=" + dataLen +
				", trxId=" + trxId +
				'}';
	}

}
