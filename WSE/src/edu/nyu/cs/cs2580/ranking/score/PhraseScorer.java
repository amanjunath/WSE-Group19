package edu.nyu.cs.cs2580.ranking.score;

import java.util.Map;
import java.util.Vector;
import edu.nyu.cs.cs2580.ranking.feature.BigramFeatureGenerator;
import edu.nyu.cs.cs2580.ranking.feature.WordPair;

public class PhraseScorer extends TermScorer<WordPair> {

	public PhraseScorer(int numOfDocs) {
		type = "phrase";
		gen = new BigramFeatureGenerator(numOfDocs);
	}
	
	@Override
    public double getSimScore(Map<WordPair, Double> featureMap) {
        int matchCount = 0;
        for (WordPair pair : keys) {
            matchCount += featureMap.containsKey(pair) ? featureMap.get(pair) : 0;
        }
        return matchCount;
    }

	@Override
	public void setQuery(Vector<String> queryWords) {
		this.keys = new Vector<WordPair>();
		for (int i = 0; i < queryWords.size() - 1; i++) {
			WordPair pair = new WordPair(queryWords.get(i), queryWords.get(i+1));
			this.keys.add(pair);
		}
		gen.setQueryWords(queryWords);
	}
}
