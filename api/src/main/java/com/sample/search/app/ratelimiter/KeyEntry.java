package com.sample.search.app.ratelimiter;

public class KeyEntry {

	private String key;
	
	private int rateLimit;

	public KeyEntry(String key, int rateLimit) {
		super();
		this.key = key;
		this.rateLimit = rateLimit;
	}

	public String getKey() {
		return key;
	}

	public int getRateLimit() {
		return rateLimit;
	}
	
	
}
