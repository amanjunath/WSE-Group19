package edu.nyu.cs.cs2580.ranking.scorer;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.junit.Test;

import edu.nyu.cs.cs2580.ranking.score.CosineScorer;

public class CosineScorerTest {

	@Test
	public void test() {
		CosineScorer scorer = new CosineScorer(1);
		Vector<String> query = new Vector<String>();
		query.add("bing");
		query.add("Windows");
		
		Map<String, Double> featureMap =  new HashMap<String, Double>();
		featureMap.put("bing", 1.0);
		featureMap.put("Microsoft", 2.0);
		featureMap.put("Seattle", 3.0);
		featureMap.put("OperatingSystem", 0.0);
		featureMap.put("Windows", 4.0);
		scorer.setQuery(query);
		assertEquals(scorer.getSimScore(featureMap), (5 / (Math.sqrt(2) * Math.sqrt(30))), 0.00000001);
	}
	
	

}
