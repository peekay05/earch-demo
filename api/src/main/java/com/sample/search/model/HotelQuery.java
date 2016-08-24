package com.sample.search.model;

public class HotelQuery {

	private String keyword;
	
	private String city; 
	
	private String sortField;
	
	public boolean sortDesc;
	
	private int pageNo;
	
	private int pageSize;

	
	public HotelQuery(String keyword, String city) { 
		this.keyword = keyword;
		this.city = city; 
	}
	
	 
	
	public HotelQuery(String keyword, String city, int pageNo, int pageSize) {
		super();
		this.keyword = keyword;
		this.city = city;
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	public String getKeyword() {
		return keyword.toLowerCase();
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortField() {
		return sortField;
	}

	public void setSortField(String sortField) {
		this.sortField = sortField;
	}

	public boolean isSortDesc() {
		return sortDesc;
	}

	public void setSortDesc(boolean sortDesc) {
		this.sortDesc = sortDesc;
	}
	
	
	
}
