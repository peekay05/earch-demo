package com.sample.search.searcher;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;

import com.sample.search.model.Hotel2;
import com.sample.search.model.HotelQuery;
import com.sample.search.model.Match;

public class HotelDB2Searcher extends AbstractSearcher{

	public HotelDB2Searcher(String indexDir) {
		super(indexDir); 
	}

//	public  static void main(String[] args){
//		HotelDB2Searcher hotelSearcher = new HotelDB2Searcher("byCityIndex");
//		Set<Hotel2> matches = hotelSearcher.searchByCity("Bangkok", true, false);
//		for (Hotel2 hotel2 : matches) {
//			System.out.println(hotel2);
//		}
//		 
//	}


	protected Sort getSort(HotelQuery hquery){
		if(!"".equalsIgnoreCase(hquery.getSortField())){
			return new Sort(new SortField(hquery.getSortField() +"_idx", SortField.Type.INT, hquery.isSortDesc()) ); 
		}else
			return null;
	}

	@Override
	protected Query buildLuceneQuery(HotelQuery hquery) {
		TermQuery tquery = new TermQuery(new Term("city", hquery.getCity().toLowerCase())); 
		
		return tquery;
	}



	@Override
	protected Match fromLuceneDoc(Document doc) { 
		long id = 0; 
		String hcity = "";
		int hprice = 0;
		String hroomType = "";
		id =  Long.parseLong( doc.getField("id").stringValue() ); 
		hprice =  doc.getField("price").numericValue().intValue(); 
		hcity = doc.get("city").trim(); 
		hroomType = doc.get("type").trim();  
		return  new Hotel2(id,   hcity,  hprice, hroomType);
	}

	 

}
