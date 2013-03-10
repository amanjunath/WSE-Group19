package edu.nyu.cs.cs2580.ranking.score;

import java.util.Vector;

import edu.nyu.cs.cs2580.Document;

public abstract class Scorer {
	protected String type = "";
	
	public String type() {
		return type;
	}
	
	public abstract void setQuery(Vector<String> queryWords);
	public abstract double getSimScore(Document doc);
}
