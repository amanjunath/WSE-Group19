package edu.nyu.cs.cs2580.ranking.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public abstract class UnigramFeatureGenerator extends FeatureGenerator<String> {

    protected int totalTerms;
    protected Map<String, Integer> termFrequency;

    protected UnigramFeatureGenerator(int numOfDocs) {
        super(numOfDocs);
    }

    protected void createFeatureKeys(Vector<String> keywords) {
        featureKeys = keywords;
    }

    @Override
    protected void loadDocument(Vector<String> terms) {
        totalTerms = 0;
        termFrequency = new HashMap<String, Integer>();
        for (String term : terms) {
            if (!termFrequency.containsKey(term)) {
                termFrequency.put(term, 1);
            } else {
                termFrequency.put(term, termFrequency.get(term)+1);
            }
            totalTerms++;
        }
    }

    protected abstract double getTermFeature(String term);

    @Override
    public Map<String, Double> getFeatureMap(Vector<String> terms) {
        Map<String, Double> featureMap = new HashMap<String, Double>();
        loadDocument(terms);
        for (String term : terms) {
        	if (!featureMap.containsKey(term)) {
        		double termFeature = getTermFeature(term);
        		featureMap.put(term, termFeature);
        	}
        }
        return featureMap;
    }
    
    public int totalTerms() {
    	return totalTerms;
    }
    
    public int getTermFrequency(String s) {
    	return termFrequency.containsKey(s) ? termFrequency.get(s) : 0;
    }

}

