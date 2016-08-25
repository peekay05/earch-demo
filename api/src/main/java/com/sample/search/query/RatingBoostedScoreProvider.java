package com.sample.search.query;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.queries.CustomScoreProvider;

public class RatingBoostedScoreProvider extends CustomScoreProvider{

	private String ratingField = "";
	private String bookingField = "";
	
	  
	public RatingBoostedScoreProvider(String ratingField, String bookingField, LeafReaderContext context) {
		super(context); 
		this.ratingField= ratingField;
		this.bookingField= bookingField;
	}

	@Override
	public float customScore(int docId, float subQueryScore, float
			valSrcScores[]) throws IOException {
		IndexReader r = context.reader();
		Document doc =  r.document(docId);
		 
		int rating = Integer.parseInt( doc.get(ratingField).trim());
		int bookings = Integer.parseInt( doc.get(bookingField).trim()); 
		return (float)((1 + rating/5)*( 1 +  Math.log(bookings)/10 )*subQueryScore); // New Score
	}
}
