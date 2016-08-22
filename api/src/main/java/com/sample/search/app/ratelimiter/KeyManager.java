package com.sample.search.app.ratelimiter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
 

public class KeyManager {

	Logger logger = LoggerFactory.getLogger(KeyManager.class);
	 
	public HashMap<String, KeyEntry>  keyMap = new HashMap<String, KeyEntry>();
	
	public ExpiringMap<String, TokenEntry> limitMap = new ExpiringMap<>(1000*60);
	
	public ExpiringMap<String, SuspensionToken> suspendedKeys = new ExpiringMap<>(1000*60*5);
	 
	public KeyManager(String keyFile) throws IOException{
		List<String> keyRecords = Files.readAllLines(Paths.get(keyFile));
		for(String keyRec : keyRecords){
			keyMap.put( keyRec.split(",")[0], new KeyEntry(keyRec.split(",")[0], Integer.parseInt(keyRec.split(",")[1])));
		}
	}
	
	 
	public KeyStatus processKey(String key){
		if(! keyMap.containsKey(key))
			return KeyStatus.INVALID;
		
		if(suspendedKeys.containsKey(key)){
			return KeyStatus.SUSPENDED;
		}
		
		if(limitMap.containsKey(key)){
			TokenEntry tokenEntry = limitMap.get(key);
			if(tokenEntry.isWithinLimit()){
				logger.info("No of request remaining" + tokenEntry.remainingRequests());
				return KeyStatus.ACTIVE;
			}else{
				suspendedKeys.put(key, new SuspensionToken());
			}
		}else{
			TokenEntry tokenEntry = new TokenEntry(keyMap.get(key).getRateLimit());
			limitMap.put(key,  tokenEntry);
		}
		return KeyStatus.ACTIVE;
	}

	 
}
