package com.sample.search.indexing;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;


public class HotelDb2Indexer extends AbstractCsvLuceneIndexer {

	public HotelDb2Indexer(String indexDir, String dataDir) {
		super(indexDir, dataDir); 
	}

	@Override
	protected Document parseRecord(String csvRecord) {
		Document doc = new Document();
		String tokens[] = csvRecord.toLowerCase().split(",");
		doc.add(new StoredField("id",Long.parseLong( tokens[1]) )); 
		doc.add(new StringField("city",tokens[0], Store.YES));
		doc.add(new TextField("type",tokens[2], Store.YES));
		doc.add(new StoredField("price", Integer.parseInt( tokens[3])));
		doc.add(new SortedNumericDocValuesField("price_idx", Integer.parseInt( tokens[3])));
		return doc;
	}
 
	public static void main(String[] args) throws Exception{
		HotelDb2Indexer indexer = new HotelDb2Indexer("indexDir/hoteldb2" , "dataDir/hoteldb.csv" );
		indexer.buildIndex();
	}
}
