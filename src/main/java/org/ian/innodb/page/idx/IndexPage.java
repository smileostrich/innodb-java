package org.ian.innodb.page.idx;

import org.ian.innodb.page.Page;
import org.ian.innodb.page.PageType;

import java.util.Optional;

public class IndexPage {

	private final Page page;
	private final IndexHeader indexHeader;

	public IndexPage(Page page, IndexHeader indexHeader) {
		this.page = page;
		this.indexHeader = indexHeader;
	}

	public static Optional<IndexPage> tryFromPage(Page page) {
		if (page.header().getPageType() != PageType.INDEX) {
			return Optional.empty();  // Handle InnoDBError.InvalidPageType appropriately
		}

		return Optional.of(new IndexPage(page, IndexHeader.fromBytes(page.getBody())));
	}

	public Optional<Record> recordAt(int offset) {
		return Record.tryFromOffset(page.rawData(), offset);
	}

	public Optional<Record> infimum() {
		return recordAt(99);
	}

	public Optional<Record> supremum() {
		return recordAt(112);
	}

}
