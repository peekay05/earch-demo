package com.sample.search.engines;

import com.sample.search.model.HotelQuery;
import com.sample.search.model.SearchResult;

public interface SearchEngine {

	public SearchResult search(HotelQuery query);
}
