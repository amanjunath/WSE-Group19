package edu.nyu.cs.cs2580.ranking.feature;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.Properties;

public class LMFeatureGenerator extends UnigramFeatureGenerator {

    public LMFeatureGenerator(int numOfDocs) {
        super(numOfDocs);
    }

    @Override
    protected double getTermFeature(String term) {
        int tf = termFrequency.containsKey(term) ? termFrequency.get(term) : 0;
        int corpusTf = Document.termFrequency(term);

        double documentProb = (double)tf / totalTerms;
        double collectionProb = (double)corpusTf / Document.termFrequency();

        return Math.log((1 - Properties.LAMDA) * documentProb + Properties.LAMDA * collectionProb);
    }
    
    @Override
    public Map<String, Double> getFeatureMap(Vector<String> terms) {
        Map<String, Double> featureMap = new HashMap<String, Double>();
        loadDocument(terms);
        for (String queryWord : featureKeys) {
        	featureMap.put(queryWord, getTermFeature(queryWord));
        }
        return featureMap;
    }
}
