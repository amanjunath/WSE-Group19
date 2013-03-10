package edu.nyu.cs.cs2580.ranking.score;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import edu.nyu.cs.cs2580.Document;
import edu.nyu.cs.cs2580.Index;
import edu.nyu.cs.cs2580.Properties;

public class LinearScorer extends Scorer {
	
	private Map<String, Scorer> scorers;
	private Map<String, Double> scoreParams;
	private Map<Integer, Map<String, Double>> unnormalizedScores;
	private Map<String, Double> totalScores;
	private Index index;
	private int numOfDocs;

	public LinearScorer(int numOfDocs, Index _index) {
		type = "linear";
		loadScorers(numOfDocs);
		loadParameters();
	    index = _index;
	    this.numOfDocs = numOfDocs;
	    totalScores = new HashMap<String, Double>();
	}
	
	private void precomputeScores(int numOfDocs) {
		unnormalizedScores = new HashMap<Integer, Map<String, Double>>();
		for (int i = 0; i < numOfDocs; i++) {
			Document doc = index.getDoc(i);
			Map<String, Double> scoreByMetrics = getScoreByMetrics(doc);
			totalScores.put("cosine", (totalScores.containsKey("cosine") ? totalScores.get("cosine") : 0) + scoreByMetrics.get("cosine"));
			totalScores.put("lm", (totalScores.containsKey("lm") ? totalScores.get("lm") : 0) + scoreByMetrics.get("lm"));
			totalScores.put("phrase", (totalScores.containsKey("phrase") ? totalScores.get("phrase") : 0) + scoreByMetrics.get("phrase"));
			totalScores.put("numviews", (totalScores.containsKey("numviews") ? totalScores.get("numviews") : 0) + scoreByMetrics.get("numviews"));
			unnormalizedScores.put(i, scoreByMetrics);
		}
	}
	
	private void loadScorers(int numOfDocs) {
		scorers = new HashMap<String, Scorer>();
		scorers.put("cosine", new CosineScorer(numOfDocs));
		scorers.put("lm", new LikelihoodScorer(numOfDocs));
		scorers.put("phrase", new PhraseScorer(numOfDocs));
		scorers.put("numviews", new NumViewScorer());
	}
	
	private void loadParameters() {
		scoreParams = new HashMap<String, Double>();
		scoreParams.put("cosine", Properties.COSINE_BETA);
		scoreParams.put("lm", Properties.LM_BETA);
		scoreParams.put("phrase", Properties.PHRASE_BETA);
		scoreParams.put("numviews", Properties.NUMVIEW_BETA);
	}
	
	private Map<String, Double> getScoreByMetrics(Document doc) {
		Map<String, Double> scoreByMetrics = new HashMap<String, Double>();
		for (Entry<String, Scorer> entry : scorers.entrySet()) {
			scoreByMetrics.put(entry.getKey(), entry.getValue().getSimScore(doc));
		}
		return scoreByMetrics;
	}
	
	@Override
	public double getSimScore(Document doc) {
		double score = 0;
		Map<String, Double> scoreByMetrics = unnormalizedScores.get(doc._docid);
		for (Entry<String, Double> entry : scoreByMetrics.entrySet()) {
			if (!(totalScores.get(entry.getKey()) == 0)) {
				score += scoreParams.get(entry.getKey()) * (entry.getValue() / totalScores.get(entry.getKey()));
			}
		}
		return score;
	}

	@Override
	public void setQuery(Vector<String> queryWords) {
		for (Entry<String, Scorer> entry : scorers.entrySet()) {
			entry.getValue().setQuery(queryWords);
		}
		//This is special for linear model scorer since we need normalize all the scores
		precomputeScores(numOfDocs);
	}
}
