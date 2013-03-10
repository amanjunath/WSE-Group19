package edu.nyu.cs.cs2580.ranking.score;

import java.util.Map;
import java.util.Vector;
import edu.nyu.cs.cs2580.ranking.feature.LMFeatureGenerator;

public class LikelihoodScorer extends TermScorer<String> {

	public LikelihoodScorer(int numOfDocs) {
		type = "lm";
		gen = new LMFeatureGenerator(numOfDocs);
	}
	
	@Override
    public double getSimScore(Map<String, Double> featureMap) {
        double joinProb = 0.0;
        for (String queryWord : keys) {
        	joinProb += featureMap.get(queryWord);
        }
        return Math.exp(joinProb);
    }
    
    @Override
	public void setQuery(Vector<String> queryWords) {
		this.keys = queryWords;
		gen.setQueryWords(queryWords);
	}
}
