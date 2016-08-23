package com.sample.search.model;

public class Hotel2 extends Match implements Comparable<Hotel2>{
 	
	private String city;
	
	private Integer price;
	
	private String type;
 
	

	public Hotel2(long id, String city, int price, String type) {
		super(id, "");
		this.city = city;
		this.price = price;
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Hotel2 [id=" + id + ", city=" + city + ", price=" + price + ", type=" + type + "]";
	}

	@Override
	public int compareTo(Hotel2 o) {
		return this.getPrice().compareTo(o.getPrice()) ;
		 
	}
	
	
	
}
