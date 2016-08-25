package com.sample.search.query;

import java.io.IOException;

import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;

public class CustomQuery extends CustomScoreQuery {

	public CustomQuery(Query subQuery) {
		super(subQuery);
	}


	protected CustomScoreProvider getCustomScoreProvider(
			LeafReaderContext context) throws IOException {
		return new RatingBoostedScoreProvider("rating", "bookings", context);
	}


}
