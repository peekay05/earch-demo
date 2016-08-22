package com.sample.search.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
import com.sample.search.model.Match;
import com.sample.search.model.SearchResult;

public abstract class AbstractSearcher implements Searcher{

	String indexDir;

	FSDirectory fsDir;

	IndexSearcher searcher;

	public AbstractSearcher(String indexDir){
		this.indexDir = indexDir;		
	}

	public void initSearcher() throws IOException{
		fsDir = FSDirectory.open(Paths.get(indexDir));
		RAMDirectory ramDir = new RAMDirectory(fsDir, new IOContext());
		DirectoryReader reader = DirectoryReader.open(ramDir);
		searcher = new IndexSearcher(reader);
	}
	
	
	protected abstract Query buildLuceneQuery(HotelQuery hquery);
	
	protected abstract Match fromLuceneDoc(Document doc);
	
	@Override
	public List<Match> search(HotelQuery hquery) {
		ArrayList<Match> matches = new ArrayList<>();
		try { 
			 
			TopDocs topDocs = searcher.search(buildLuceneQuery(hquery), 10); 
			Document doc = null; 
			long id = 0;
			String name = "";
			String city = "";
			String country = "";
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				doc = searcher.doc(scoreDoc.doc); 
				matches.add( fromLuceneDoc(doc)) ;
			} 
		} catch (Exception e) {
			e.printStackTrace(); 
		}

		return matches;
	}
}
