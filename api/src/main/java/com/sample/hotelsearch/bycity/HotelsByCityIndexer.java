package com.sample.hotelsearch.bycity;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import com.sample.search.indexing.AbstractCsvLuceneIndexer;


public class HotelsByCityIndexer extends AbstractCsvLuceneIndexer {

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
		HotelsByCityIndexer indexer = new HotelsByCityIndexer();
		indexer.buildIndex( "byCityIndex" , "dataDir/hoteldb.csv" , false );
	}
}
