package com.sample.search.searcher;

import java.util.List;

import com.sample.search.model.HotelQuery;
import com.sample.search.model.Match;

public interface Searcher {

	public List<Match> search(HotelQuery hquery);
}
