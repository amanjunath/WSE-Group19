package edu.nyu.cs.cs2580.ranking.feature;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public abstract class FeatureGenerator<T> {

    protected int numOfDocs;
    protected Vector<T> featureKeys;

    protected FeatureGenerator(int numOfDocs) {
        this.numOfDocs = numOfDocs;
    }

    public void setQueryWords(Vector<String> queryWords) {
    	featureKeys = new Vector<T>();
        createFeatureKeys(queryWords);
    }
    
    protected abstract void createFeatureKeys(Vector<String> keywords);
    protected abstract void loadDocument(Vector<String> terms);


    public List<Double> getFeatureVector(Vector<String> terms) {
        List<Double> featureVector = new ArrayList<Double>();
        Map<T, Double> featureMap = getFeatureMap(terms);
        for (T featureKey : featureKeys) {
            if (!featureMap.containsKey(featureKey)) {
                featureVector.add((double) 0);
            } else {
                featureVector.add(featureMap.get(featureKey));
            }
        }
        return featureVector;
    }

    public abstract Map<T, Double> getFeatureMap(Vector<String> terms);
}

