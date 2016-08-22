package com.sample.search.indexing;

import org.apache.lucene.document.Document;

public interface RecordParser {

	public Document parseRecord(String record);
}
