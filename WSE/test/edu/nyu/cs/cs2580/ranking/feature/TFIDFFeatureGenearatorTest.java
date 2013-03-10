package edu.nyu.cs.cs2580.ranking.feature;

import java.util.Map;
import org.junit.Test;
import edu.nyu.cs.cs2580.Document;

public class TFIDFFeatureGenearatorTest extends UnigramFeatureGeneratorTest {

	@Test
	public void test() {
        UnigramFeatureGenerator gen = new TFIDFFeatureGenerator(idx.numDocs());
        gen.setQueryWords(queryWords);
        Document doc = idx.getDoc(1);
        Map<String, Double> featureMap = gen.getFeatureMap(doc.get_body_vector());
        outputTermInfo(gen);
        assertEquals(featureMap.get("amazon"), computeTFIDF(35, 46, idx.numDocs()), 0.00000001);
        assertEquals(featureMap.get("a9"), computeTFIDF(31, 8, idx.numDocs()), 0.00000001);
        assertEquals(featureMap.get("advertising"), computeTFIDF(12, 89, idx.numDocs()), 0.00000001);
        assertEquals(featureMap.get("subsidiary"), computeTFIDF(2, 24, idx.numDocs()), 0.00000001);
        assertEquals(featureMap.get("search"), computeTFIDF(45, 364, idx.numDocs()), 0.00000001);
        assertEquals(featureMap.get("google"), computeTFIDF(1, 246, idx.numDocs()), 0.00000001);
	}
}
