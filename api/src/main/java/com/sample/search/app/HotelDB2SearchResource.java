package com.sample.search.app;

import java.net.URLEncoder;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.search.app.ratelimiter.KeyManager;
import com.sample.search.app.ratelimiter.KeyStatus;
import com.sample.search.indexing.HotelDb2Indexer;
import com.sample.search.model.HotelQuery;
import com.sample.search.searcher.HotelDB2Searcher;

@RestController
@RequestMapping(path="/api2")
public class HotelDB2SearchResource {

	@Autowired
	HotelDB2Searcher hotelDB2Searcher;
	
	 
	@Autowired
	KeyManager keyManager;
	
	@Autowired
	HotelDb2Indexer hotelDb2Indexer;
	
 
	@PostConstruct
	public void init() throws Exception{
		hotelDb2Indexer.buildIndex();
		hotelDB2Searcher.initSearcher();
	}
	
	@RequestMapping(value = "/bycity/{city}", method = RequestMethod.GET)
	public String search(@PathVariable String city, 
					@RequestParam String apikey,
					@RequestParam(defaultValue="") String sortField, 
					@RequestParam(defaultValue="1") int pageNum, 
					@RequestParam(defaultValue="10") int pageSize,  
					@RequestParam(defaultValue="true") boolean desc  ) throws Exception{
	 
		String response = "";
		KeyStatus keyStatus = keyManager.processKey(apikey);
		switch (keyStatus) {
		case ACTIVE:
			try {
				HotelQuery hotelQuery = new HotelQuery(city, city);
				hotelQuery.setPageNo(pageNum);
				hotelQuery.setPageSize(pageSize);
				if(!"".equalsIgnoreCase(sortField)){
					hotelQuery.setSortField( sortField );
					hotelQuery.setSortDesc(desc);
				}
				response =  toJsonP(hotelDB2Searcher.search(hotelQuery ), "");
			} catch (Exception e) {
				throw new Exception("An error has occured");
			} 
			break;

		case SUSPENDED:
			response = "You have exceeded the max number of permissible requests. You key has been suspended for 5 minutes";
			break;
			
		case INVALID:
			response = "The supplied key is invalid. Please contact the admin for a valid key";
			break;
		default:
			break;
		}
		return response;
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
