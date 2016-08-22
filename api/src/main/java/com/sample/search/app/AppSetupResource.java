package com.sample.search.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.sample.search.indexing.HotelDb1Indexer;
import com.sample.search.indexing.HotelDb2Indexer;

@Controller
public class AppSetupResource {

	@Autowired
	HotelDb1Indexer hotelDb1Indexer;


	@Autowired
	HotelDb2Indexer hotelDb2Indexer;
	
	
	
}
