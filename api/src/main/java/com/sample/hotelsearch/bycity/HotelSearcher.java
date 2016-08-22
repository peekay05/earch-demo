package com.sample.hotelsearch.bycity;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;

public class HotelSearcher{

	private FSDirectory fsDir;

	private IndexSearcher searcher;

	public HotelSearcher(String indexDir) {
		try { 
			fsDir = FSDirectory.open(Paths.get(indexDir));
			RAMDirectory ramDir = new RAMDirectory(fsDir, new IOContext());
			DirectoryReader reader = DirectoryReader.open(ramDir);
			searcher = new IndexSearcher(reader);
		} catch (Exception e) {
			//			logger.log(level, msg, thrown);.error("Failed to initialize Product Searcher", e);
		}
	}

	  
	public Set<Hotel2> searchByCity(String city,  boolean sortByPrice, boolean desc) {
		TreeSet<Hotel2> macthes = new TreeSet<>();
		TopDocs topDocs = null;
		try { 


			TermQuery tquery = new TermQuery(new Term("city", city.toLowerCase())); 
			if(sortByPrice){
				Sort sort = new Sort(new SortField("price", SortField.Type.SCORE, true) );
				topDocs = searcher.search(tquery, 10, sort ); 
			}else{
				topDocs = searcher.search(tquery, 10  );
			}
			Document doc = null; 
			long id = 0;
			 
			String hcity = "";
			int hprice = 0;
			String hroomType = "";
			 
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				doc = searcher.doc(scoreDoc.doc);
				id =  Long.parseLong( doc.getField("id").stringValue() ); 
				hprice =  doc.getField("price").numericValue().intValue(); 
				hcity = doc.get("city").trim(); 
				hroomType = doc.get("type").trim(); 
				macthes.add(new Hotel2(id,   hcity,  hprice, hroomType)) ;
			} 
		} catch (Exception e) {
			 e.printStackTrace();
		}

		if(desc)
			return  macthes.descendingSet();
		else
			return macthes;
	}

	public  static void main(String[] args){
		HotelSearcher hotelSearcher = new HotelSearcher("byCityIndex");
		Set<Hotel2> matches = hotelSearcher.searchByCity("Bangkok", true, false);
		for (Hotel2 hotel2 : matches) {
			System.out.println(hotel2);
		}
		 
	}

}
