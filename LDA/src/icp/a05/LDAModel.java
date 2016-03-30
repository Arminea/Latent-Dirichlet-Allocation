package icp.a05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import icp.tools.Vocabulary;

/**
 * LDA model.
 * 
 * @author Tereza Stanglova
 *
 */
public class LDAModel implements TopicModel{

	/** Probability parameter for documents. */
	private static final double ALPHA 			= 0.5;
	
	/** Probability parameter for words. */
	private static final double BETA 			= 0.1;
	
	/** Period of perplexity computing. */
	private static final int PERPLEXITY_PERIOD 	= 10;
	
	/** List of documents. */
	private ArrayList<Document> docs;
	
	/** Map of counts of topics for each word. */
	private Map<String, Word> wordTopicCounts;
	
	/** Vector of words counts for each topic. */
	private int [] topicCounts;
	
	/** Vocabulary. */
	private Vocabulary vocabulary;
	
	/** Number of iterations. */
	private int numberOfIteration;
	
	/** Number of topics. */
	private int topicCount;
	
	/** Log. */
	private double log2 = 1 / Math.log(2);
	
	/**
	 * Constructor.
	 * 
	 * @param fileName				train file name
	 * @param numberOfIteration		number of iteration
	 * @param topicCount			number of topics
	 * @throws IOException			exception
	 */
	public LDAModel(String fileName, int numberOfIteration, int topicCount) throws IOException {
		this.numberOfIteration = numberOfIteration;
		this.topicCount = topicCount;
		LDADataset dataset = new LDADataset(fileName, topicCount);
		dataset.readDataset(); // important
		
		docs = dataset.getDocs();
		wordTopicCounts = dataset.getWordTopicCounts();
		topicCounts = dataset.getTopicCounts();
		vocabulary = dataset.getVocabulary();
	}
	
	/**
	 * Gibbs sampling.
	 */
	public void GibbsSampling() {
		
		for(int iteration = 0; iteration <= numberOfIteration; iteration++) {
			
			for(int i = 0; i < docs.size(); i++) { // iteration through documents
				Document doc = docs.get(i);
				for(int j = 0; j < doc.getDocumentSize(); j++) {
					int newTopic = getNewTopic(doc, j);
					doc.setTopicNumber(j, newTopic);
				} // end for each word in document
			} // end for each document
			
			if(iteration % PERPLEXITY_PERIOD == 0) {
				System.out.println(iteration + ". iteration ------------------");
				System.out.println("Perplexity: " + getPerplexity());
				System.out.println();
			}
			
		} // end of iterations
		
	}
	
	/**
	 * Return a new topic word given word.
	 * 
	 * @param doc			document
	 * @param wordIndex		index of word
	 * @return				new topic
	 */
	private int getNewTopic(Document doc, int wordIndex) {
		int oldTopic = doc.getWordTopic(wordIndex);
		String word = doc.getWord(wordIndex);
		int newTopic = 0;
		
		wordTopicCounts.get(word).decreaseCount(oldTopic);
		topicCounts[oldTopic]--;
		doc.decreaseTopicCountInDocument(oldTopic);
		
		double [] scores = new double[topicCount];
		double prob1, prob2;
		double alphaSum = topicCount * ALPHA;
		double betaSum = vocabulary.size() * BETA;
		
		for(int k = 0; k < topicCount; k++) {
			prob1 = (wordTopicCounts.get(word).getCount(k) + BETA) / (topicCounts[k] + betaSum);
			prob2 = (doc.getTopicCountsInDocument(k) + ALPHA) / (doc.getDocumentSize() + alphaSum);
			
			scores[k] = prob1 * prob2;
		}
		
		newTopic = sample(scores);
		
		wordTopicCounts.get(word).increaseCount(newTopic);
		topicCounts[newTopic]++;
		doc.increaseTopicCountInDocument(newTopic);
		
		return newTopic;
	}
	
	/**
	 * 
	 * 
	 * @param scores
	 * @return
	 */
	private int sample(double [] scores) {
		double sum = 0;
		for (double score : scores)
			sum += score;
		
		double sampleScore = Math.random() * sum ;
		
		int newTopic = -1;
		
		while ( sampleScore > 0.0) {
			newTopic++;
			sampleScore -= scores[newTopic];
		}
		
		return newTopic;
	}
	
	/**
	 * Returns an entropy.
	 * 
	 * @return
	 */
	private double getEntropy() {
		double entropy = 0.0;
		int count = 0;
		Document doc;
		double docProb, wordProb, prob = 0.0;
		double alphaSum = topicCount * ALPHA;
		double betaSum = vocabulary.size() * BETA;
		
		for(int i = 0; i < docs.size(); i++) {
			doc = docs.get(i);
			for(int j = 0; j < doc.getDocumentSize(); j++) {
				String word = doc.getWord(j);
				double tmp = 0.0;
				for(int k = 0; k < topicCount; k++) {
					wordProb = (wordTopicCounts.get(word).getCount(k) + BETA) / (topicCounts[k] + betaSum);
					docProb = (doc.getTopicCountsInDocument(k) + ALPHA) / (doc.getDocumentSize() + alphaSum);
					tmp += wordProb * docProb;
				}
				prob += Math.log( tmp ) * log2;
				count++;
			}
		}
		
		entropy = - (prob / (double) count);
		
		return entropy;
	}
	
	// ************************** PRINT *******************************
	
	public void printDebug() {
		
		System.out.println("wordTopicCounts ----------------------------------------------");
		System.out.println("wordTopicCounts size: " + wordTopicCounts.size());
		for(Entry<String, Word> entry : wordTopicCounts.entrySet()) {
			String key = entry.getKey();
			Word value = entry.getValue();
			if(key.length() > 7 ) {
				System.out.println(key + "\t" + value.toString());
			} else {
				System.out.println(key + "\t\t" + value.toString());
			}
		}
		
		System.out.println("\ntopicCounts --------------------------------------------------");
		System.out.println(Arrays.toString(topicCounts));
		
		System.out.println("\ndocs ---------------------------------------------------------");
		System.out.println("Docs size: " + docs.size());
		int count = 0;
		for(Document d : docs) {
			System.out.println(d.toString());
			count += d.getDocumentSize();
		}
		System.out.println("docs word: " + count);
	}
	
	// ************************** TEST ********************************
	
	/**
	 * Method should return 1.
	 * @return
	 */
	public boolean getWordProbSum() {
		double wordProbSum = 0.0;
		double wordProb;
		double betaSum = vocabulary.size() * BETA;
		
		for(int k = 0; k < topicCount; k++) {
			
			for(Entry<String, Word> entry : wordTopicCounts.entrySet()) {
				//String key = entry.getKey();
				Word value = entry.getValue();
				wordProb = (value.getCount(k) + BETA) / (topicCounts[k] + betaSum);
				wordProbSum += wordProb;
			}

			if (wordProbSum < 0.99d || wordProbSum > 1.01d) {
				System.out.println("Nekde neco spatne " + wordProbSum);
				return false;
			}
			wordProbSum = 0.0;
		}
		
		return true;
	}
	
	/**
	 * Method should return 1.
	 * @return
	 */
	public boolean getDocProbSum() {
		Document doc;
		double docProb;
		double docProbSum = 0.0;
		double alphaSum = topicCount * ALPHA;
		
		for(int i = 0; i < docs.size(); i++) {
			for(int k = 0; k < topicCount; k++) {
			doc = docs.get(i);
			docProb = (doc.getTopicCountsInDocument(k) + ALPHA) / (doc.getDocumentSize() + alphaSum);
			docProbSum += docProb;
			}
			if (docProbSum < 0.99d || docProbSum > 1.01d) {
				System.out.println("Nekde neco spatne. " + docProbSum);
				return false;
			}
			docProbSum = 0.0;
		}
		return true;
	}
	
	/**
	 * Nice method to sort maps by value.
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<V>> Map<K, V> sortByValues(final Map<K, V> map) {
	    Comparator<K> valueComparator =  new Comparator<K>() {
	        public int compare(K k1, K k2) {
	            int compare = map.get(k2).compareTo(map.get(k1));
	            if (compare == 0) return 1;
	            else return compare;
	        }
	    };
	    Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	}
	
	//************************** OVERRIDE *************************
	
	/**
	 * Returns a list of the most likely words for every topic.
	 * @param maxWords length of arraylist
	 * @return
	 */
	@Override
	public List<String>[] getTopicWords(int maxWords) {
		
		List<String>[] topWordsInTopic = new ArrayList[topicCount];
		
		for(int k = 0; k < topicCount; k++) {
			Map<String, Integer> tmpMap = new HashMap<String, Integer>();
			
			for(Entry<String, Word> entry : wordTopicCounts.entrySet()) {
				String key = entry.getKey();
				Word value = entry.getValue();
				
				tmpMap.put(key, value.getCount(k));
			}
			
			Map<String, Integer> tmpMapSorted = sortByValues(tmpMap);
			ArrayList<String> topwords = new ArrayList<String>();
			
			int pom = 0;
			for (Entry<String, Integer> entry : tmpMapSorted.entrySet()) {
				if(pom == maxWords)
					break;
				
				topwords.add(entry.getKey());
			    pom++;
			}
			System.out.println(topwords.toString());
			topWordsInTopic[k] = topwords;
		}
		
		return topWordsInTopic;
	}
	
	/**
	 * Returns actual perplexity for train data set.
	 * @return
	 */
	@Override
	public double getPerplexity() {
		double entropy = getEntropy();
		System.out.println("Entropy: " + entropy);
		return Math.pow(2, entropy);
	}

	/**
	 * Returns vocabulary.
	 * @return
	 */
	@Override
	public Vocabulary getVocabulary() {
		return vocabulary;
	}

}
