package edu.nyu.cs.cs2580.ranking.feature;


import java.util.Map;
import org.junit.Test;
import edu.nyu.cs.cs2580.Document;

public class LMFeatureGeneratorTest extends UnigramFeatureGeneratorTest {


	@Test
	public void test() {
		UnigramFeatureGenerator gen = new LMFeatureGenerator(idx.numDocs());
		gen.setQueryWords(queryWords);
        Document doc = idx.getDoc(1);
        Map<String, Double> featureMap = gen.getFeatureMap(doc.get_body_vector());
        
        assertEquals(featureMap.get("amazon"), likelihood(35, gen.totalTerms(), idx.termFrequency("amazon"), idx.termFrequency()), 0.00000001);
        assertEquals(featureMap.get("a9"), likelihood(31, gen.totalTerms(), idx.termFrequency("a9"), idx.termFrequency()), 0.00000001);
        assertEquals(featureMap.get("advertising"), likelihood(12, gen.totalTerms(), idx.termFrequency("advertising"), idx.termFrequency()), 0.00000001);
        assertEquals(featureMap.get("subsidiary"), likelihood(2, gen.totalTerms(), idx.termFrequency("subsidiary"), idx.termFrequency()), 0.00000001);
        assertEquals(featureMap.get("search"), likelihood(45, gen.totalTerms(), idx.termFrequency("search"), idx.termFrequency()), 0.00000001);
        assertEquals(featureMap.get("google"), likelihood(1, gen.totalTerms(), idx.termFrequency("google"), idx.termFrequency()), 0.00000001);
        assertEquals(featureMap.get("random"), likelihood(0, gen.totalTerms(), idx.termFrequency("random"), idx.termFrequency()), 0.00000001);
	}
}
