package com.sample.search.indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DocValuesType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.sample.search.autocomplete.AutocompleteAnalyzer;


public class LuceneIndexer {

	private static final FieldType DOUBLE_FIELD_TYPE_STORED_SORTED = new FieldType();

	static {
		DOUBLE_FIELD_TYPE_STORED_SORTED.setTokenized(true);
		DOUBLE_FIELD_TYPE_STORED_SORTED.setOmitNorms(true);
		DOUBLE_FIELD_TYPE_STORED_SORTED.setIndexOptions(IndexOptions.DOCS); 
		DOUBLE_FIELD_TYPE_STORED_SORTED.setStored(true);
		DOUBLE_FIELD_TYPE_STORED_SORTED.setDocValuesType(DocValuesType.NUMERIC);
		DOUBLE_FIELD_TYPE_STORED_SORTED.freeze();
	}

	public LuceneIndexer(){ 
	}	 

	public void buildIndex(Directory directory, String dataDir, boolean indexNGrams) throws Exception {

		FileReader fileReader =null;;
		IndexWriter indexWriter = null;

		BufferedReader br = null; 
		long docCount = 0;
		long pasreErrorCount = 0;
		try {  

			IndexWriterConfig iwConf = new IndexWriterConfig(new AutocompleteAnalyzer());
			iwConf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			indexWriter= new IndexWriter(directory,iwConf);  
			br = new BufferedReader(new FileReader(new File(dataDir))); 
			Document doc = null; 
			String csvRecord = null;
			String tokens[] = null; 
			
			while((csvRecord = br.readLine()) != null){ 
				try {
					doc = new Document();
					tokens = csvRecord.split(",");
					doc.add(new LongPoint("_id",Long.parseLong( tokens[0]) ));
					doc.add(new StringField("id", tokens[0], Store.YES ));
					doc.add(new TextField("keywords_tok",tokens[1] +" " + tokens[2] +" " +tokens[3] , Store.NO) );
					doc.add(new StringField("keywords_untok",tokens[1] +", " + tokens[2] +", " +tokens[3] , Store.YES) );
					doc.add(new StringField("name",tokens[1], Store.YES));
					doc.add(new StringField("city",tokens[2], Store.YES));
					doc.add(new StringField("country",tokens[3], Store.YES));
					doc.add(new IntPoint("rating",  Integer.parseInt(tokens[4])));
					doc.add(new IntPoint("bookings", Integer.parseInt( tokens[5])));
					indexWriter.updateDocument(new Term("id", tokens[0]), doc); 
					docCount++;
				} catch (Exception e) {
					pasreErrorCount++;
				}
				
			}
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		finally{ 
			System.out.println(String.format("%d Documents Processed. %d Errors Found", docCount, pasreErrorCount));
			try {
				indexWriter.commit();
				if(fileReader!=null)
					fileReader.close();
				if(indexWriter != null)
					indexWriter.close();  
				if(br != null)
					br.close();	

			} catch (IOException e) { 
				e.printStackTrace();
			}
		} 
	}

	public void buildIndex(String indexDir, String dataDir, boolean indexNGrams) throws Exception {
		deleteDir(new File(indexDir));
		Directory fsDir  = FSDirectory.open(Paths.get(indexDir)); 
		buildIndex(fsDir,   dataDir,   indexNGrams);
	}

	public void deleteDir(File file) throws IOException{
		if(file.exists()){
			if(file.isDirectory()){
				File[] files = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteDir(files[i]);
				}
				Files.delete(file.toPath());
			}else
				Files.delete(file.toPath());
		}
	}

	public static void main(String[] args) throws Exception{ 
		LuceneIndexer  luceneIndexer  = new LuceneIndexer();
		luceneIndexer.buildIndex("indexDir2/", "dataDir/hotels.csv", false);
	}
}
