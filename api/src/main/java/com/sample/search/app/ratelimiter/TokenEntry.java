package com.sample.search.app.ratelimiter;

import java.util.concurrent.atomic.AtomicInteger;

public class TokenEntry{
 
  

	private final int maxReqests;
 
    private final AtomicInteger counter;

   
    TokenEntry(int maxReqests) { 
    	this.maxReqests = maxReqests;
        this.counter = new AtomicInteger(0);
    }

   
    public int remainingRequests(){
    	return this.maxReqests  - incrementAndGet();
    }
    
    private int incrementAndGet() {
        return this.counter.incrementAndGet();
    }
    
    public boolean isWithinLimit(){
    	incrementAndGet();
    	return this.counter.intValue() <= maxReqests;
    }
}
