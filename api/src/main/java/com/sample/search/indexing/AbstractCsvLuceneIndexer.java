package com.sample.search.indexing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class AbstractCsvLuceneIndexer {

	 

	Logger logger = LoggerFactory.getLogger(AbstractCsvLuceneIndexer.class);
	
	private String indexDir;
	
	private String dataDir;
	 
	public AbstractCsvLuceneIndexer(String indexDir, String dataDir  ){ 
		 this.indexDir = indexDir;
		 this.dataDir = dataDir;
	}	 

	private void buildIndex(Directory directory, String dataDir, boolean indexNGrams) throws Exception {

		logger.info("Reading records from the file: " +  dataDir);
		FileReader fileReader =null;;
		IndexWriter indexWriter = null;

		BufferedReader br = null; 
		long docCount = 0;
		long pasreErrorCount = 0;
		try {  

			IndexWriterConfig iwConf = new IndexWriterConfig(new StandardAnalyzer());
			iwConf.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			indexWriter= new IndexWriter(directory,iwConf);  
			br = new BufferedReader(new FileReader(new File(dataDir))); 
 
			String csvRecord = null; 
			while((csvRecord = br.readLine()) != null){ 
				try { 
					indexWriter.addDocument(parseRecord(csvRecord));
					docCount++;
				} catch (Exception e) {
					pasreErrorCount++;
					logger.error("Error in parsing record {}. Nested exception is ", csvRecord, e.getMessage());
				}
				
			}
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		finally{ 
			logger.info(String.format("%d Documents Processed. %d Errors Found", docCount, pasreErrorCount));
			try {
				indexWriter.commit();
				if(fileReader!=null)
					fileReader.close();
				if(indexWriter != null)
					indexWriter.close();  
				if(br != null)
					br.close();	

			} catch (IOException e) {
				logger.error("Error in indexing. nested exception is "+ e.getMessage());
				e.printStackTrace();
			}
		} 
	}

	public void buildIndex() throws Exception {
		deleteDir(new File(indexDir));
		Directory fsDir  = FSDirectory.open(Paths.get(indexDir)); 
		buildIndex(fsDir,   dataDir,   true);
	}

	protected abstract Document parseRecord(String record);
	
	
	/**
	 * Rcursively delete the index directory
	 * @param file
	 * @throws IOException
	 */
	private void deleteDir(File file) throws IOException{
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
 
}
