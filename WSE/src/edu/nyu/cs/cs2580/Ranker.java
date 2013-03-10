package edu.nyu.cs.cs2580;

import java.util.Vector;
import java.util.Scanner;

import edu.nyu.cs.cs2580.ranking.score.CosineScorer;
import edu.nyu.cs.cs2580.ranking.score.LikelihoodScorer;
import edu.nyu.cs.cs2580.ranking.score.LinearScorer;
import edu.nyu.cs.cs2580.ranking.score.NumViewScorer;
import edu.nyu.cs.cs2580.ranking.score.PhraseScorer;
import edu.nyu.cs.cs2580.ranking.score.Scorer;

class Ranker {
  private Index _index;
  private Scorer _scorer;

  public Ranker(String index_source){
    _index = new Index(index_source);
  }
  
  public void createScorer(String ranker_type) {
	  if (ranker_type.equals("vsm")){
		  _scorer = new CosineScorer(_index.numDocs());
        } else if (ranker_type.equals("ql")){
          _scorer = new LikelihoodScorer(_index.numDocs());
        } else if (ranker_type.equals("phrase")){
          _scorer = new PhraseScorer(_index.numDocs());
        } else if (ranker_type.equals("numviews")){
          _scorer = new NumViewScorer();
        } else {
          _scorer = new LinearScorer(_index.numDocs(), _index);
        }
  }
  

  public Vector < ScoredDocument > runquery(String query){
	  // Build query vector
	  Scanner s = new Scanner(query);
	  Vector < String > qv = new Vector < String > ();
	  while (s.hasNext()){
		  String term = s.next();
		  qv.add(term);
	  }
	  _scorer.setQuery(qv);

	  Vector < ScoredDocument > retrieval_results = new Vector < ScoredDocument > ();
	  for (int i = 0; i < _index.numDocs(); ++i){
		  retrieval_results.add(runquery(i));
	  }
	  return retrieval_results;
  }
  

  public ScoredDocument runquery(int did){

    // Get the document vector. For hw1, you don't have to worry about the
    // details of how index works.
    Document d = _index.getDoc(did);

    // Score the document. Here we have provided a very simple ranking model,
    // where a document is scored 1.0 if it gets hit by at least one query term.
    double score = _scorer.getSimScore(d);

    return new ScoredDocument(did, d.get_title_string(), score);
  }
}
