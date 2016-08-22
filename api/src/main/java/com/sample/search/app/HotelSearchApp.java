package com.sample.search.app;

import java.io.IOException;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sample.hotelsearch.bycity.HotelSearcher;
import com.sample.search.app.ratelimiter.KeyManager;
import com.sample.search.autocomplete.AutoCompleter;
import com.sample.search.engines.LuceneEngine;
import com.sample.search.engines.SearchEngine; 

@SpringBootApplication
public class HotelSearchApp {

	Logger logger = LoggerFactory.getLogger("Hotel Search App");
	
	@Autowired
	AppConfig appConfig;
	
	public static void main(String[] args) {
		SpringApplication.run(HotelSearchApp.class, args);
	}
	
	@Bean
	public SearchEngine getSearchEngine() throws Exception{ 
		try {
			return new LuceneEngine(appConfig.getHotelDb1IndexDir());
		} catch (Exception e) {
			logger.error("Failed to create autoindex dir. Nested exception is : " + e.getMessage());
			throw new Exception();
		}
		
	}
	
	@Bean
	public HotelSearcher getHotelSearcher() throws Exception{ 
		try {
			return new HotelSearcher(appConfig.getHotelDb2IndexDir());
		} catch (Exception e) {
			logger.error("Failed to create search for hotel db2. Nested exception is : " + e.getMessage());
			throw new Exception();
		}
		
	}
	
	@Bean
	public KeyManager getKeyManager() throws Exception{ 
		try {
			return new KeyManager(appConfig.getKeyfile());
		} catch (Exception e) {
			logger.error("Failed to create keymanager. Nested exception is : " + e.getMessage());
			throw new Exception();
		}
		
	}
	
	@Bean
	public AutoCompleter getAutoCompleter() throws Exception{
		 
		try {
			return new AutoCompleter(appConfig.getHotelDb1IndexDir(), appConfig.getAutoCompleteIndexDir());
		} catch (IOException e) {
			logger.error("Failed to create autoindex dir. Nested exception is : " + e.getMessage());
			throw new Exception();
		} 
	}
}
