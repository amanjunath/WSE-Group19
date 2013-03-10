package edu.nyu.cs.cs2580.ranking.score;

import java.util.Map;
import java.util.Vector;
import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.ranking.feature.FeatureGenerator;

public abstract class TermScorer<T> extends Scorer{

    protected Vector<T> keys;
    protected FeatureGenerator<T> gen;

    public abstract double getSimScore(Map<T, Double> featureMap);
    
    @Override
    public double getSimScore(Document doc) {
    	Map<T, Double> features = gen.getFeatureMap(doc.get_body_vector());
    	return getSimScore(features);
    }
}
