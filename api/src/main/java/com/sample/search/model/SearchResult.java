package com.sample.search.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {

	@JsonProperty
	private List<Hotel> hotels;
	
	public SearchResult(List<Hotel> hotels){
		 this.hotels = hotels;
	}

	
	public List<Hotel> getHotels() {
		return hotels;
	}


	public void setHotels(List<Hotel> hotels) {
		this.hotels = hotels;
	}


	@Override
	public String toString() {
		String matches = "";
		for (Hotel hotel : hotels) {
			matches += hotel +"\n";
		}
		return matches;
	}
	
	
}
