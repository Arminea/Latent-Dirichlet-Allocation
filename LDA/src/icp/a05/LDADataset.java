package icp.a05;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import icp.tools.BasicDataProvider;
import icp.tools.DataProvider;
import icp.tools.Vocabulary;

public class LDADataset {
	
	/** Vocabulary. */
	private Vocabulary vocabulary;
	
	/** Train filename. */
	private String fileName;
	
	/** Count of topics. */
	private int topics;
	
	/** List of documents. */
	private ArrayList<Document> docs;
	
	/** Map of counts of topics for each word. */
	private Map<String, Word> wordTopicCounts;
	
	/** Vector of words counts for each topic. */
	private int [] topicCounts;
	
	/**
	 * Constructor. Initializes data structures for LDA model.
	 * 
	 * @param fileName		filename
	 * @param topicCount	topic count
	 */
	public LDADataset(String fileName, int topicCount) {
		this.fileName = fileName;
		this.topics = topicCount;
		docs = new ArrayList<Document>(); // reads documents
		wordTopicCounts = new HashMap<String, Word>();
		topicCounts = new int [topicCount]; 
		
		for(int i = 0; i < topicCounts.length; i++) {
			topicCounts[i] = 0;
		}
	}
	
	/**
	 * Method for reading train dataset.
	 * 
	 * @throws IOException
	 */
	public void readDataset() throws IOException{
		DataProvider provider = new BasicDataProvider(fileName);
        vocabulary = new Vocabulary();
        Random r = new Random();
        int randTopic;
        
        String[] sentence = null;
	    while ((sentence = provider.next()) != null) {
	        Document doc = new Document(sentence.length, topics);
	        for (int i = 0; i < sentence.length; i++) {
	        	vocabulary.add(sentence[i]);
	        	
	        	randTopic = r.nextInt(topics); // <0, 50)
	        	doc.addNewWord(i, sentence[i], randTopic);
	        	
	        	saveNewWord(sentence[i], randTopic);
	        	topicCounts[randTopic]++;
	        }
	        docs.add(doc);
	    }
	}

	/**
	 * Method for saving a new word into hashmap.
	 * 
	 * @param word		word from corpus
	 * @param topic		random topic
	 */
	private void saveNewWord(String word, int topic) {
		if(!wordTopicCounts.containsKey(word)) {
			Word w = new Word(topics);
			w.increaseCount(topic);
			
			wordTopicCounts.put(word, w);
		} else {
			wordTopicCounts.get(word).increaseCount(topic);
		}
	}
	
	/**
	 * Returns a number of words in document.
	 * 
	 * @return
	 */
	public int getNumberOfWordInDocs() {
		int count = 0;
		for(int i = 0; i < docs.size(); i++) {
			count += docs.get(i).getDocumentSize();
		}
		return count;
	}
	
	// ********************* GETTERS **********************
	
	public int [] getTopicCounts() {
		return topicCounts;
	}
	
	public Map<String, Word> getWordTopicCounts() {
		return wordTopicCounts;
	}
	
	public ArrayList<Document> getDocs() {
		return docs;
	}
	
	public Vocabulary getVocabulary() {
		return vocabulary;
	}
}
