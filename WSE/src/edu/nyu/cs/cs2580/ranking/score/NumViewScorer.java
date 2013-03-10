package edu.nyu.cs.cs2580.ranking.score;

import java.util.Vector;

import edu.nyu.cs.cs2580.Document;

public class NumViewScorer extends Scorer{

    public double getSimScore(Document doc) {
    	type = "numviews";
        return doc.get_numviews();
    }

	@Override
	public void setQuery(Vector<String> queryWords) {
	}
}
