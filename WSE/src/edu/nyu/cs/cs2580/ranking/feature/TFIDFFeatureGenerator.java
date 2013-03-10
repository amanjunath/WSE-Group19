package edu.nyu.cs.cs2580.ranking.feature;

import edu.nyu.cs.cs2580.Document;

public class TFIDFFeatureGenerator extends UnigramFeatureGenerator {

    public TFIDFFeatureGenerator(int numOfDocs) {
        super(numOfDocs);
    }

    private double getTermIDF(String term) {
        int df = Document.documentFrequency(term);
        if (df == 0) {
            return 0;
        }
        double idf = (double)numOfDocs / df;
        idf = 1 + Math.log(idf) / Math.log(2);
        return idf;
    }

    @Override
    protected double getTermFeature(String term) {
        int tf = termFrequency.containsKey(term) ? termFrequency.get(term) : 0;
        double idf = getTermIDF(term);
        double tfidf = tf * idf;
        return tfidf;
    }
}
