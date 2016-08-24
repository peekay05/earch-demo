package com.sample.search.searcher;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.RAMDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sample.search.model.HotelQuery;
import com.sample.search.model.Match;

public abstract class AbstractSearcher implements Searcher{

	Logger logger = LoggerFactory.getLogger(getClass());
	
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
	
	protected abstract Sort getSort(HotelQuery hquery);
	
	@Override
	public List<Match> search(HotelQuery hquery) {
		ArrayList<Match> matches = new ArrayList<>();
		try { 		
			TopDocs topDocs =  null;
			if(hquery.getSortField() != null && !"".equalsIgnoreCase(hquery.getSortField()) && getSort(hquery) != null ){ 
				topDocs = searcher.search(buildLuceneQuery(hquery), hquery.getPageSize() * hquery.getPageNo(), getSort(hquery)); 
			}else{
				topDocs = searcher.search(buildLuceneQuery(hquery), hquery.getPageSize()); 
			}
			int skipItems = hquery.getPageSize() * (hquery.getPageNo() -1 );
			int skippedItems = 0;
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) { 
				if(skippedItems++ < skipItems)
					continue;
				matches.add( fromLuceneDoc(searcher.doc(scoreDoc.doc))) ;
			} 
		} catch (Exception e) {
			logger.error("Problem in querying the luecene index for query {}. Nested exception is {} ", hquery.getKeyword(), e.getMessage());
		}

		return matches;
	}
}
