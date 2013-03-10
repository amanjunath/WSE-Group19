package edu.nyu.cs.cs2580.ranking.feature;

public class WordPair {

    private final String word1;
    private final String word2;

    public WordPair(String word1, String word2) {
        this.word1 = word1;
        this.word2 = word2;
    }

    public String word1() {
        return word1;
    }

    public String word2() {
        return word2;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (!(obj instanceof WordPair)) {
    		return false;
    	}
    	WordPair pair = (WordPair)obj;
    	return word1.equals(pair.word1) && word2.equals(pair.word2);
    }
    
    @Override
    public int hashCode() {
    	return (word1 + " " + word2).hashCode();
    }
    
    @Override
    public String toString() {
    	return "<" + word1 + "," + word2 +">";
    }
}
