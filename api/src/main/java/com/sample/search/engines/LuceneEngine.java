package com.sample.search.engines;

import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.DisjunctionMaxQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;

import com.sample.search.model.Hotel;
import com.sample.search.model.HotelQuery;
import com.sample.search.model.SearchResult;

public class LuceneEngine implements SearchEngine{

	private FSDirectory fsDir;
	
	private IndexSearcher searcher;
	
	public LuceneEngine(String indexDir) {
		try { 
			fsDir = FSDirectory.open(Paths.get(indexDir));
			RAMDirectory ramDir = new RAMDirectory(fsDir, new IOContext());
			DirectoryReader reader = DirectoryReader.open(ramDir);
			searcher = new IndexSearcher(reader);
		} catch (Exception e) {
			//			logger.log(level, msg, thrown);.error("Failed to initialize Product Searcher", e);
		}
	}
	@Override
	public SearchResult search(HotelQuery hquery) {
		ArrayList<Hotel> hotels = new ArrayList<>();
		try { 
			ArrayList<Query> queries = new ArrayList<>();
			queries.add(new TermQuery(new Term("keywords", hquery.getKeyword())));
			queries.add(new TermQuery(new Term("name", hquery.getKeyword())));
			queries.add(new TermQuery(new Term("city", hquery.getKeyword())));
			queries.add(new TermQuery(new Term("country", hquery.getKeyword()))); 
			DisjunctionMaxQuery dismaxQuery = new DisjunctionMaxQuery(queries, (float) 0.01);
			TopDocs topDocs = searcher.search(dismaxQuery, 10); 
			Document doc = null; 
			long id = 0;
			String name = "";
			String city = "";
			String country = "";
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				doc = searcher.doc(scoreDoc.doc);
				id = 0;// Long.parseLong( doc.getField("id").stringValue() );
				name = doc.get("name").trim();
				city = doc.get("city").trim(); 
				city = doc.get("country").trim(); 
				hotels.add(new Hotel(id, name, city, country, 0, 0)) ;
			} 
		} catch (Exception e) {
 			e.printStackTrace();
			// TODO: handle exception
		}
		
		return new SearchResult(hotels);
	}

	public  static void main(String[] args){
		LuceneEngine luceneEngine = new LuceneEngine("indexDir");
		SearchResult searchResult = luceneEngine.search(new HotelQuery("", "Beijing"));
		System.out.println(searchResult);
	}

}
