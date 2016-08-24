package com.sample.search.query;

import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.queries.function.FunctionQuery;
import org.apache.lucene.search.Query;

public class CustomQuery extends CustomScoreQuery {

	Query query;
	
	FunctionQuery fq;
	
	public CustomQuery(Query subQuery, FunctionQuery scoringQuery) {
		super(subQuery, scoringQuery); 
	}

 

}
