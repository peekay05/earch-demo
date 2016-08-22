package com.sample.search.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="searchapp")
public class AppConfig {

	private String keyfile;
	private String hotelDb1FilePath; 
	
	private String hotelDb2FilePath; 
	private String hotelDb1IndexDir;
	private String hotelDb2IndexDir;
	private String autoCompleteIndexDir;
	
	public String getKeyfile() {
		return keyfile;
	}
	public void setKeyfile(String keyfile) {
		this.keyfile = keyfile;
	}
	public String getHotelDb1FilePath() {
		return hotelDb1FilePath;
	}	
	
	public String getHotelDb2FilePath() {
		return hotelDb2FilePath;
	}
	public void setHotelDb2FilePath(String hotelDb2FilePath) {
		this.hotelDb2FilePath = hotelDb2FilePath;
	}
	public void setHotelDb1FilePath(String hotelDb1FilePath) {
		this.hotelDb1FilePath = hotelDb1FilePath;
	}
	public String getHotelDb1IndexDir() {
		return hotelDb1IndexDir;
	}
	public void setHotelDb1IndexDir(String hotelDb1IndexDir) {
		this.hotelDb1IndexDir = hotelDb1IndexDir;
	}
	public String getHotelDb2IndexDir() {
		return hotelDb2IndexDir;
	}
	public void setHotelDb2IndexDir(String hotelDb2IndexDir) {
		this.hotelDb2IndexDir = hotelDb2IndexDir;
	}
	public String getAutoCompleteIndexDir() {
		return autoCompleteIndexDir;
	}
	public void setAutoCompleteIndexDir(String autoCompleteIndexDir) {
		this.autoCompleteIndexDir = autoCompleteIndexDir;
	}
	
	
}
