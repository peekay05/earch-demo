package com.sample.search.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

 
public class AutoCompleteResult {

	@JsonProperty
	private List<String> matches;
	
	public AutoCompleteResult(List<String> matches){
		this.matches = matches;
	}

	public List<String> getMatches() {
		return matches;
	}

	public void setMatches(List<String> matches) {
		this.matches = matches;
	}
	
	@Override
	public String toString() {
		String matcheStr = "";
		for (String match : matches) {
			matcheStr += match +"\n";
		}
		return matcheStr;
	}
}
