package edu.nyu.cs.cs2580.ranking.score;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import edu.nyu.cs.cs2580.ranking.feature.TFIDFFeatureGenerator;

public class CosineScorer extends TermScorer<String> {

	public CosineScorer(int numOfDocs) {
		type = "cosine";
		gen = new TFIDFFeatureGenerator(numOfDocs);
	}
	
	@Override
    public double getSimScore(Map<String, Double> featureMap) {
        double product = 0;
        double queryL2Norm = Math.sqrt(keys.size());
        double featureL2Norm = 0;
        for (Entry<String, Double> entry : featureMap.entrySet()) {
        	double featureVal = entry.getValue();
        	if (keys.contains(entry.getKey())) {
        		product += featureVal;
        	}
        	featureL2Norm += featureVal * featureVal;
        }
        featureL2Norm = Math.sqrt(featureL2Norm);
        if ((queryL2Norm == 0) || (featureL2Norm == 0)) {
        	return 0;
        }
        return product / (queryL2Norm * featureL2Norm);
    }

	@Override
	public void setQuery(Vector<String> queryWords) {
		this.keys = queryWords;
		gen.setQueryWords(queryWords);
	}
}
