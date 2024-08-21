package org.ian.innodb.bufferManager;

import org.ian.innodb.page.Page;

public class DummyBufferManager implements BufferManager {

	@Override
	public PageGuard pin(int spaceId, int offset) {
		return null; // Dummy implementation returns empty
	}

	@Override
	public void unpin(Page page) {
		throw new UnsupportedOperationException("Dummy buffer manager can't unpin");
	}

}
