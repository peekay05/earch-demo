package com.sample.search.app;

import java.net.URLEncoder;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.search.autocomplete.AutoCompleter;
import com.sample.search.indexing.HotelDb1Indexer;
import com.sample.search.model.AutoCompleteResult;
import com.sample.search.model.HotelQuery;
import com.sample.search.searcher.HotelDB1Searcher;

@RestController
@RequestMapping(path="/api1")
public class HotelDB1SearchResource {

	@Autowired
	HotelDB1Searcher hotelDB1Searcher;
	
	@Autowired
	AutoCompleter autoCompleter;
	
	@Autowired
	HotelDb1Indexer hotelDb1Indexer;


	@PostConstruct  
	public void init() throws Exception{
		hotelDb1Indexer.buildIndex();
		autoCompleter.buildIndex();
		hotelDB1Searcher.initSearcher();
	}
	
	@RequestMapping(value = "/search/{query}", method = RequestMethod.GET)
	public String search(@PathVariable String query, 
					@RequestParam(defaultValue="") String sortField, 
					@RequestParam(defaultValue="") String city, 
					@RequestParam(defaultValue="1") int pageNum, 
					@RequestParam(defaultValue="10") int pageSize,
					@RequestParam(defaultValue="true") boolean desc,
					@RequestParam(defaultValue="") String callback ){
		
		HotelQuery hotelQuery = new HotelQuery(query, city); 
		hotelQuery.setPageNo(pageNum);
		hotelQuery.setPageSize(pageSize);
		
		if(!"".equalsIgnoreCase(sortField)){
			hotelQuery.setSortField( sortField );
			hotelQuery.setSortDesc(desc);
		}
		try {
			return  toJsonP(hotelDB1Searcher.search(hotelQuery), callback);
		} catch (Exception e) {
			return "";
		} 
	}
	
	@RequestMapping(value = "/autocomplete/{query}", method = RequestMethod.GET)
	public String  autocomplete(@PathVariable String query, @RequestParam(defaultValue="") String callback){
		String response;
		AutoCompleteResult autoCompleteResult = null; 
		try { 
			autoCompleteResult =  autoCompleter.suggestTermsFor(query); 
			response = toJsonP(autoCompleteResult, callback);
		} catch (Exception e) {
			autoCompleteResult = new AutoCompleteResult(new ArrayList<>());
			response="An error as occured";
		}
		
		return response ;
	}
	
	private String toJsonP(Object  obj, String callback) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL); 
		String jsonpResponse = "";
		jsonpResponse = mapper.writeValueAsString(obj); 
		if (null != callback && !"".equalsIgnoreCase(callback)) {
			String callbackStr = URLEncoder.encode(callback, "UTF-8");
			jsonpResponse = callbackStr + "(" + jsonpResponse + ")";
		}
		return jsonpResponse;
	}
}
