package com.sample.search.indexing;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedNumericDocValuesField;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.util.NumericUtils;


public class HotelDb1Indexer extends AbstractCsvLuceneIndexer {

	public HotelDb1Indexer(String indexDir, String dataDir) {
		super(indexDir, dataDir); 
	}

	@Override
	protected Document parseRecord(String csvRecord) {
		Document doc = new Document();
		String tokens[] = csvRecord.split(",");
		doc.add(new LongPoint("_id",Long.parseLong( tokens[0]) ));
		doc.add(new StoredField("id", Long.parseLong( tokens[0])));
		doc.add(new TextField("keywords_tok",tokens[1] +" " + tokens[2] +" " +tokens[3] , Store.NO) );
		doc.add(new StringField("keywords_untok",tokens[1] +", " + tokens[2] +", " +tokens[3] , Store.YES) );
		doc.add(new StringField("name",tokens[1], Store.YES));
		doc.add(new StringField("city",tokens[2], Store.YES));
		doc.add(new StringField("country",tokens[3], Store.YES));
		doc.add(new StoredField("rating",  Integer.parseInt(tokens[4])));
		doc.add(new StoredField("bookings", Integer.parseInt( tokens[5])));
//		document.add(new SortedNumericDocValuesField(name, NumericUtils.floatToSortableInt(-5.3f)));
		doc.add(new NumericDocValuesField("rating_idx",  Integer.parseInt(tokens[4])));
		doc.add(new NumericDocValuesField("bookings_idx", Integer.parseInt( tokens[5])));
		return doc;
	}

	
	  
//	public static void main(String[] args) throws Exception{ 
//		HotelDb1Indexer  luceneIndexer  = new HotelDb1Indexer("indexDir/hoteldb2", "dataDir/hotels.csv");
//		luceneIndexer.buildIndex();
//	}
}
