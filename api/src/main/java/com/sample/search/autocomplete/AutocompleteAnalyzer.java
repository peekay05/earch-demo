package com.sample.search.autocomplete;

import java.util.Arrays;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.util.CharArraySet;

public class AutocompleteAnalyzer extends Analyzer{

	private static final String[] ENGLISH_STOP_WORDS = {
			"a", "an", "and", "are", "as", "at", "be", "but", "by",
			"for", "i", "if", "in", "into", "is",
			"no", "not", "of", "on", "or", "s", "such",
			"t", "that", "the", "their", "then", "there", "these",
			"they", "this", "to", "was", "will", "with"
	};


	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer source = new StandardTokenizer();
		TokenStream filter = new StandardFilter(source);
		filter = new LowerCaseFilter(filter); 
		filter = new StopFilter(filter,
				new CharArraySet(Arrays.asList(ENGLISH_STOP_WORDS), true));  
		filter = new EdgeNGramTokenFilter( filter, 1, 20); 
		return new TokenStreamComponents(source, filter); 
	}

}
