package edu.nyu.cs.cs2580.ranking.feature;

import java.util.Vector;
import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.Index;
import edu.nyu.cs.cs2580.Properties;

public abstract class UnigramFeatureGeneratorTest extends TestCase {

	protected Vector<String> queryWords;
	protected Index idx;
    
	@BeforeClass
	public void setUp() {
		idx = new Index(Properties.CORPUS);
        queryWords = new Vector<String>();
        queryWords.add("amazon");
        queryWords.add("a9");
        queryWords.add("advertising");
        queryWords.add("subsidiary");
        queryWords.add("search");
        queryWords.add("google");
        queryWords.add("random");
	}
	
	@Test
	public abstract void test();
	
	protected void outputTermInfo(UnigramFeatureGenerator gen) {
        for (String word : queryWords) {
            System.out.println(word);
            System.out.println("Document Frequency " + Document.documentFrequency(word));
            System.out.println("Term Frequency " + gen.getTermFrequency(word));
            System.out.println("Total Term Frequency " + Document.termFrequency(word));
            System.out.println("****");
        }
    }
    
    protected double computeTFIDF(int tf, int df, int n) {
    	return tf * (1 + Math.log((double)n / df) / Math.log(2));
    }

    protected double likelihood(int tf, int docTf, int corpusTf, int allTf) {
    	return (1 - Properties.LAMDA) * ((double)tf / docTf) + Properties.LAMDA * ((double)corpusTf / allTf);
    }
}
