package com.sample.search.utils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.Field.Store;

import com.sample.search.model.Hotel;

public class DocMapper {

	public static Document toLuceneDoc(Hotel hotel){
		Document doc = new Document(); 
		doc.add(new LongPoint("id", hotel.getId()));
		doc.add(new StringField("name", hotel.getName(), Store.YES));
		doc.add(new StringField("city", hotel.getCity(), Store.YES));
		doc.add(new StringField("country", hotel.getCountry(), Store.YES));
		doc.add(new DoublePoint("rating", hotel.getStars()));
		doc.add(new IntPoint("bookings", hotel.getBookings()));
		return doc;
	}
	
	public static Hotel fromAutoCompleteDoc(Document doc){
		Long id = 0l; //Long.parseLong( doc.getField("id").stringValue() );
		String name = doc.get("name").trim();
		String city = "";//doc.get("city").trim(); 
		String country ="";// doc.get("country").trim(); 
		return new Hotel(id, name, city, country, 0, 0);
	}
}
