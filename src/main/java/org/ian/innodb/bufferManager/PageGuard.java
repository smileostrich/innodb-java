package org.ian.innodb.bufferManager;

import org.ian.innodb.page.Page;

public class PageGuard implements AutoCloseable {
	private Page page;
	private final BufferManager bufferManager;

	public PageGuard(Page page, BufferManager bufferManager) {
		this.page = page;
		this.bufferManager = bufferManager;
	}

	public Page getPage() {
		return page;
	}

	@Override
	public void close() {
		bufferManager.unpin(page);
		page = null; // Ensure page is null after unpinning
	}

}
