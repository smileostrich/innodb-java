package org.ian.innodb.page.lob;

import org.ian.innodb.FileListBaseNode;
import org.ian.innodb.FileListInnerNode;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Optional;

public class LobIndexEntry {

	private final FileListInnerNode fileListNode;
	private final FileListBaseNode versionList;
	private final long creationTransactionId;
	private final long modifyTransactionId;
	private final int undoNumber;
	private final int undoNumberModify;
	private final int pageNumber;
	private final int dataLength;
	private final int lobVersion;

	public LobIndexEntry(FileListInnerNode fileListNode, FileListBaseNode versionList, long creationTransactionId,
	                     long modifyTransactionId, int undoNumber, int undoNumberModify, int pageNumber,
	                     int dataLength, int lobVersion) {
		this.fileListNode = fileListNode;
		this.versionList = versionList;
		this.creationTransactionId = creationTransactionId;
		this.modifyTransactionId = modifyTransactionId;
		this.undoNumber = undoNumber;
		this.undoNumberModify = undoNumberModify;
		this.pageNumber = pageNumber;
		this.dataLength = dataLength;
		this.lobVersion = lobVersion;
	}

	public static Optional<LobIndexEntry> tryFromBytes(byte[] bytes) {
		if (bytes.length < size()) {
			return Optional.empty(); // Buffer not long enough
		}

		int offset = 0;

		Optional<FileListInnerNode> fileListNode = FileListInnerNode.tryFromBytes(Arrays.copyOfRange(bytes, offset, offset + FileListInnerNode.size()));
		offset += FileListInnerNode.size();

		Optional<FileListBaseNode> versionList = FileListBaseNode.tryFromBytes(Arrays.copyOfRange(bytes, offset, offset + FileListBaseNode.size()));
		offset += FileListBaseNode.size();

		long creationTransactionId = ByteBuffer.wrap(new byte[]{0, 0, bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3], bytes[offset + 4], bytes[offset + 5]}).getLong();
		offset += 6;

		long modifyTransactionId = ByteBuffer.wrap(new byte[]{0, 0, bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3], bytes[offset + 4], bytes[offset + 5]}).getLong();
		offset += 6;

		int undoNumber = ByteBuffer.wrap(bytes, offset, 4).getInt();
		offset += 4;

		int undoNumberModify = ByteBuffer.wrap(bytes, offset, 4).getInt();
		offset += 4;

		int pageNumber = ByteBuffer.wrap(bytes, offset, 4).getInt();
		offset += 4;

		int dataLength = ByteBuffer.wrap(bytes, offset, 2).getShort() & 0xFFFF;
		offset += 2;

		// Gap of 2 bytes
		offset += 2;

		int lobVersion = ByteBuffer.wrap(bytes, offset, 4).getInt();
		offset += 4;

		if (fileListNode.isPresent() && versionList.isPresent()) {
			return Optional.of(new LobIndexEntry(fileListNode.get(), versionList.get(), creationTransactionId, modifyTransactionId,
					undoNumber, undoNumberModify, pageNumber, dataLength, lobVersion));
		} else {
			return Optional.empty();
		}
	}

	public static int size() {
		return 60; // Adjust based on the actual byte size calculated
	}

}
