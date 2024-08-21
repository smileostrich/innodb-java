package org.ian.innodb.bufferManager;

import org.ian.innodb.page.Page;

import java.io.IOException;

public interface BufferManager {

	PageGuard pin(int spaceId, int offset) throws IOException;

	void unpin(Page page);

}

