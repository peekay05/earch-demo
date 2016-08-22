package com.sample.search.app.ratelimiter;

public class SuspensionToken {

	private long suspensionTime;
	 
	public SuspensionToken(){
		this.suspensionTime = System.currentTimeMillis(); 
	}

	public long getSuspensionTime() {
		return suspensionTime;
	}
	
	
}
