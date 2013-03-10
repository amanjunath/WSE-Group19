package edu.nyu.cs.cs2580.ranking.scorer;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

import edu.nyu.cs.cs2580.ranking.score.CosineScorer;
import edu.nyu.cs.cs2580.ranking.score.LikelihoodScorer;

public class LikelihoodScorerTest {

	@Test
	public void test() {
		LikelihoodScorer scorer = new LikelihoodScorer(1);
		Vector<String> query = new Vector<String>();
		query.add("bing");
		query.add("Windows");
		
		Map<String, Double> featureMap =  new HashMap<String, Double>();
		featureMap.put("bing", 0.5);
		featureMap.put("Microsoft", 0.4);
		featureMap.put("Seattle", 0.2);
		featureMap.put("OperatingSystem", 0.2);
		featureMap.put("Windows", 0.4);
		scorer.setQuery(query);
		assertEquals(scorer.getSimScore(featureMap), 0.5 * 0.4, 0.00000001);
	}

}
