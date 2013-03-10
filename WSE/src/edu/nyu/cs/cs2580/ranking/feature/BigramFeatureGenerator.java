package edu.nyu.cs.cs2580.ranking.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;

public class BigramFeatureGenerator extends FeatureGenerator<WordPair> {

    protected Map<WordPair, Integer> docWordPairs;

    public BigramFeatureGenerator(int numOfDocs) {
        super(numOfDocs);
    }

    @Override
    protected void createFeatureKeys(Vector<String> keywords) {
        for (int i = 0; i < keywords.size()-1; i++) {
            String word1 = keywords.get(i);
            String word2 = keywords.get(i+1);
            WordPair pair = new WordPair(word1, word2);
            featureKeys.add(pair);
        }
    }

    @Override
    protected void loadDocument(Vector<String> terms) {
        docWordPairs = new HashMap<WordPair, Integer>();
        for (int i = 0; i < terms.size() - 1; i++) {
            WordPair pair = new WordPair(terms.get(i), terms.get(i+1));
            if (!docWordPairs.containsKey(pair)) {
            	docWordPairs.put(pair, 1);
            } else {
            	docWordPairs.put(pair, docWordPairs.get(pair)+1);
            }
        }
    }

    @Override
    public Map<WordPair, Double> getFeatureMap(Vector<String> terms) {
        Map<WordPair, Double> bigramFeatureMap = new HashMap<WordPair, Double>();
        loadDocument(terms);
        for (Entry<WordPair, Integer> entry : docWordPairs.entrySet()) {
        	WordPair pair = entry.getKey();
        	if (featureKeys.contains(pair)) {
        		bigramFeatureMap.put(pair, (double)docWordPairs.get(pair));
        	}
        }
        return bigramFeatureMap;
    }
    
}
                
