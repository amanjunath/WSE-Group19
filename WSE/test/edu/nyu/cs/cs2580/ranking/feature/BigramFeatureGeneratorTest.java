package edu.nyu.cs.cs2580.ranking.feature;

import java.util.Map;
import java.util.Vector;
import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.Index;
import edu.nyu.cs.cs2580.Properties;

public class BigramFeatureGeneratorTest extends TestCase {

	protected Vector<String> queryWords;
	protected Index idx;
    
	@BeforeClass
	public void setUp() {
		idx = new Index(Properties.CORPUS);
        queryWords = new Vector<String>();
        queryWords.add("a9");
        queryWords.add("com");
        queryWords.add("subsidiary");
        queryWords.add("of");
        queryWords.add("amazon");
        queryWords.add("advertising");
        queryWords.add("technology");
	}

	@Test
	public void test() {
		BigramFeatureGenerator gen = new BigramFeatureGenerator(idx.numDocs());
		gen.setQueryWords(queryWords);
        Document doc = idx.getDoc(1);
        Map<WordPair, Double> featureMap = gen.getFeatureMap(doc.get_body_vector());
        WordPair pair1 = new WordPair("a9", "com");
        WordPair pair3 = new WordPair("subsidiary", "of");
        WordPair pair4 = new WordPair("of", "amazon");
        WordPair pair5 = new WordPair("amazon", "advertising");
        WordPair pair6 = new WordPair("advertising", "technology");
        
        assertEquals(featureMap.get(pair1), 10.0, 0.000001);
        assertEquals(featureMap.get(pair3), 1.0, 0.000001);
        assertEquals(featureMap.get(pair4), 1.0, 0.000001);
        assertEquals(featureMap.get(pair5), 1.0, 0.000001);
        assertEquals(featureMap.get(pair6), 6.0, 0.000001);
		
        
//        outputWordPairInfo(doc.get_body_vector());
	}

	private void outputWordPairInfo(Vector<String> terms) {
		for (int i = 0; i < queryWords.size() - 1; i++) {
			String word1 = queryWords.get(i);
			String word2 = queryWords.get(i+1);
			WordPair pair = new WordPair(word1, word2);
			int count = 0;
			for (int j = 0; j < terms.size() - 1; j++) {
				if ((terms.get(j).equals(word1)) && (terms.get(j+1).equals(word2))) {
					count++;
				}
			}
			System.out.println(pair + " : " + count);
		}
	}
}
