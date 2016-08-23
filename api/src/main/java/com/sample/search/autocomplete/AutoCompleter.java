package com.sample.search.autocomplete;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import com.sample.search.model.AutoCompleteResult;

public final class AutoCompleter { 

	    public static final String GRAMMED_WORDS_FIELD = "words";

	    public static final String SOURCE_WORD_FIELD = "keywords_untok"; 

	    private static final String COUNT_FIELD = "count"; 

	    private final Directory autoCompleteDirectory;

	    private IndexReader autoCompleteReader;

	    private IndexSearcher autoCompleteSearcher;
	    
	    private FSDirectory mainIndexDir;

	    public AutoCompleter(String mainIndexDir, String autocompleteIndexDir) throws IOException {
	    	this.autoCompleteDirectory = FSDirectory.open(Paths.get(autocompleteIndexDir)); 
	    	this.mainIndexDir = FSDirectory.open(Paths.get(mainIndexDir)); 
	    }

	    public AutoCompleteResult suggestTermsFor(String term) throws IOException {
	     
	    	Query query = new TermQuery(new Term(GRAMMED_WORDS_FIELD, term)); 
	    	Sort sort = new Sort();
	    	TopDocs docs = autoCompleteSearcher.search(query,  10, sort);
	    	List<String> suggestions = new ArrayList<String>();
	    	for (ScoreDoc doc : docs.scoreDocs) {
	    		suggestions.add(fromAutoCompleteDoc(autoCompleteReader.document(doc.doc)));
	    	}

	    	return new AutoCompleteResult(suggestions);
	    }

	    private static String fromAutoCompleteDoc(Document doc){ 
	    	return doc.get(SOURCE_WORD_FIELD).trim() ; 
		}
	   
	    public void buildIndex()
	    		throws CorruptIndexException, IOException { 
	    
			DirectoryReader sourceReader = DirectoryReader.open( mainIndexDir); 
	    	LuceneDictionary dict = new LuceneDictionary(sourceReader,
	    			SOURCE_WORD_FIELD);
 
			IndexWriterConfig iwConf = new IndexWriterConfig(new AutocompleteAnalyzer());
			iwConf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			IndexWriter writer  = new IndexWriter(autoCompleteDirectory,iwConf);   

	    	// go through every word, storing the original word (incl. n-grams) 
	    	// and the number of times it occurs
	    	Map<String, Integer> wordsMap = new HashMap<String, Integer>();

	     
	    	InputIterator inputIterator = dict.getEntryIterator();
//	    	Iterator<String> iter =  //.getWordsIterator();
	    	BytesRef byteRef = inputIterator.next();
	    	while ( byteRef!= null) { 
	    		String word = byteRef.utf8ToString(); 
	    		int len = word.length();
	    		if (len < 3) {
	    			byteRef = inputIterator.next();
	    			continue; // too short we bail but "too long" is fine...
	    		}

	    		if (wordsMap.containsKey(word)) {
	    			throw new IllegalStateException(
	    					"This should never happen in Lucene 2.3.2"); 
	    		} else { 
	    			wordsMap.put(word, sourceReader.docFreq(new Term(
	    					SOURCE_WORD_FIELD, word)));
	    		}
	    		byteRef = inputIterator.next();
	    	}

	    	for (String word : wordsMap.keySet()) { 
	    		Document doc = new Document();
	    		doc.add(new StringField(SOURCE_WORD_FIELD, word, Field.Store.YES )); // orig term
	    		doc.add(new TextField(GRAMMED_WORDS_FIELD, word, Field.Store.YES )); // grammed
	    		 
//	    		doc.add(new TextField(ID, word, Field.Store.YES ));
	    		doc.add(new IntPoint(COUNT_FIELD, wordsMap.get(word))); // count 
	    		writer.addDocument(doc);
	    	}
	    	writer.commit(); 
	    	writer.flush();
	    	writer.close();
	    	sourceReader.close(); 
	    	// re-open our reader
	    	reOpenReader();
	    }

	    private void reOpenReader() throws CorruptIndexException, IOException {
	    	if (autoCompleteReader == null) {
	    		autoCompleteReader = DirectoryReader.open(autoCompleteDirectory);
	    	}
	    	autoCompleteSearcher = new IndexSearcher(autoCompleteReader);
	    }
   

}
