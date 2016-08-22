package com.sample.search.searcher;

import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import com.sample.search.model.Hotel;
import com.sample.search.model.HotelQuery;
import com.sample.search.model.Match;

public class HotelDB1Searcher extends AbstractSearcher{

	public HotelDB1Searcher(String indexDir) {
		super(indexDir); 
	}

	@Override
	protected Query buildLuceneQuery(HotelQuery hquery) {
		ArrayList<Query> queries = new ArrayList<>();
		queries.add(new TermQuery(new Term("keywords_tok", hquery.getKeyword())));
		queries.add(new TermQuery(new Term("name", hquery.getKeyword())));
		queries.add(new TermQuery(new Term("city", hquery.getKeyword())));
		queries.add(new TermQuery(new Term("country", hquery.getKeyword()))); 
		DisjunctionMaxQuery dismaxQuery = new DisjunctionMaxQuery(queries, (float) 0.01);
		return dismaxQuery;
	}

	@Override
	protected Match fromLuceneDoc(Document doc) {
		long id = 0;
		String name = "";
		String city = "";
		String country = "";
		id = 0;// Long.parseLong( doc.getField("id").stringValue() );
		name = doc.get("name").trim();
		city = doc.get("city").trim(); 
		city = doc.get("country").trim();  
		return new Hotel(id, name, city, country, 0, 0);
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
