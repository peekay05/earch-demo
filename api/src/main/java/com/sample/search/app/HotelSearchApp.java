package com.sample.search.app;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.sample.search.app.ratelimiter.KeyManager;
import com.sample.search.autocomplete.AutoCompleter;
import com.sample.search.indexing.HotelDb1Indexer;
import com.sample.search.indexing.HotelDb2Indexer;
import com.sample.search.searcher.HotelDB2Searcher;
import com.sample.search.searcher.HotelDB1Searcher;
import com.sample.search.searcher.Searcher; 

@SpringBootApplication
public class HotelSearchApp {

	Logger logger = LoggerFactory.getLogger("Hotel Search App");
	
	@Autowired
	AppConfig appConfig;
	
	public static void main(String[] args) {
		SpringApplication.run(HotelSearchApp.class, args);
	}
	
	@Bean
	public HotelDb1Indexer getHotelDb1Indexer() throws Exception{ 
		try {
			return new HotelDb1Indexer(appConfig.getHotelDb1IndexDir(), appConfig.getHotelDb1FilePath());
		} catch (Exception e) {
			logger.error("Failed to create indexer for Hotel Db1 dir. Nested exception is : " + e.getMessage());
			throw new Exception();
		}		
	}
	
	@Bean
	public HotelDb2Indexer getHotelDb2Indexer() throws Exception{ 
		try {
			return new HotelDb2Indexer(appConfig.getHotelDb2IndexDir(), appConfig.getHotelDb2FilePath());
		} catch (Exception e) {
			logger.error("Failed to create indexer for Hotel Db2 dir. Nested exception is : " + e.getMessage());
			throw new Exception();
		}		
	}
	
 
	
	@Bean
	public HotelDB1Searcher getHotelDB1Searcher() throws Exception{ 
		try {
			return new HotelDB1Searcher(appConfig.getHotelDb1IndexDir());
		} catch (Exception e) {
			logger.error("Failed to create autoindex dir. Nested exception is : " + e.getMessage());
			throw new Exception();
		}
		
	}
	
	@Bean
	public HotelDB2Searcher getHotelDB2Searcher() throws Exception{ 
		try {
			return new HotelDB2Searcher(appConfig.getHotelDb2IndexDir());
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
