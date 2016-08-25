package com.sample.search.searcher;

import java.util.ArrayList;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BoostQuery;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;

import com.sample.search.model.Hotel;
import com.sample.search.model.HotelQuery;
import com.sample.search.model.Match;
import com.sample.search.query.CustomQuery;

public class HotelDB1Searcher extends AbstractSearcher{

	public HotelDB1Searcher(String indexDir) {
		super(indexDir); 
	}

	
	@Override
	protected Query buildLuceneQuery(HotelQuery hquery) throws ParseException {
		ArrayList<Query> queries = new ArrayList<>();
		QueryParser qp = new QueryParser("keywords_tok", new StandardAnalyzer()); 
		queries.add(  qp.parse(hquery.getKeyword()));
		queries.add(new TermQuery(new Term("name", hquery.getKeyword())));
		queries.add(new BoostQuery(new TermQuery(new Term("city", hquery.getKeyword())), 2));
		queries.add(new BoostQuery(new TermQuery(new Term("country", hquery.getKeyword())),2)); 
		DisjunctionMaxQuery dismaxQuery = new DisjunctionMaxQuery(queries, (float) 0.1); 
		return new CustomQuery(dismaxQuery);
	}
	
//	private FunctionQuery boostBookings(Query query){
//		 return new FunctionQuery(query.);
//	}

	@Override
	protected Match fromLuceneDoc(Document doc) {
		long id = Long.parseLong( doc.getField("id").stringValue() );;
		String name = doc.get("name").trim();
		String city = doc.get("city").trim(); 
		String country =  doc.get("country").trim(); 
		int rating = Integer.parseInt( doc.get("rating").trim()); 
		int bookings = Integer.parseInt( doc.get("bookings").trim()); 
		return new Hotel(id, name, city, country, rating, bookings);
	}


	@Override
	protected Sort getSort(HotelQuery hquery) { 
		return null;
	}

}


//	private FSDirectory fsDir;
//	
//	private IndexSearcher searcher;
//	
//	public HotelDB1Searcher(String indexDir) {
//		try { 
//			fsDir = FSDirectory.open(Paths.get(indexDir));
//			RAMDirectory ramDir = new RAMDirectory(fsDir, new IOContext());
//			DirectoryReader reader = DirectoryReader.open(ramDir);
//			searcher = new IndexSearcher(reader);
//		} catch (Exception e) {
//			//			logger.log(level, msg, thrown);.error("Failed to initialize Product Searcher", e);
//		}
//	}

//
//@Override
//public SearchResult search(HotelQuery hquery) {
//	ArrayList<Hotel> hotels = new ArrayList<>();
//	try { 
//		ArrayList<Query> queries = new ArrayList<>();
//		queries.add(new TermQuery(new Term("keywords", hquery.getKeyword())));
//		queries.add(new TermQuery(new Term("name", hquery.getKeyword())));
//		queries.add(new TermQuery(new Term("city", hquery.getKeyword())));
//		queries.add(new TermQuery(new Term("country", hquery.getKeyword()))); 
//		DisjunctionMaxQuery dismaxQuery = new DisjunctionMaxQuery(queries, (float) 0.01);
//		TopDocs topDocs = searcher.search(dismaxQuery, 10); 
//		Document doc = null; 
//		
//		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
//			doc = searcher.doc(scoreDoc.doc);
//			id = 0;// Long.parseLong( doc.getField("id").stringValue() );
//			name = doc.get("name").trim();
//			city = doc.get("city").trim(); 
//			city = doc.get("country").trim(); 
//			hotels.add(new Hotel(id, name, city, country, 0, 0)) ;
//		} 
//	} catch (Exception e) {
//		e.printStackTrace();
//		// TODO: handle exception
//	}
//
//	return new SearchResult(hotels);
//}

//public  static void main(String[] args){
//	HotelDB1Searcher luceneEngine = new HotelDB1Searcher("indexDir");
//	SearchResult searchResult = luceneEngine.search(new HotelQuery("", "Beijing"));
//	System.out.println(searchResult);
//}
