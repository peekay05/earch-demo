package com.sample.search.app;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.hotelsearch.bycity.HotelSearcher;
import com.sample.search.app.ratelimiter.KeyManager;
import com.sample.search.app.ratelimiter.KeyStatus;

@RestController
@RequestMapping(path="/hotelsearch")
public class HotelSearchResource2 {

	@Autowired
	HotelSearcher hotelSearcher;
	
	 
	@Autowired
	KeyManager keyManager;
	
	
	
	@RequestMapping(value = "/bycity/{city}", method = RequestMethod.GET)
	public String search(@PathVariable String city, 
					@RequestParam String apikey,
					@RequestParam(defaultValue="false") boolean sortByPrice, 
					@RequestParam(defaultValue="false") boolean desc 
					) throws Exception{
	 
		String response = "";
		KeyStatus keyStatus = keyManager.processKey(apikey);
		switch (keyStatus) {
		case ACTIVE:
			try {
				response =  toJsonP(hotelSearcher.searchByCity(city, sortByPrice, desc), "");
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
