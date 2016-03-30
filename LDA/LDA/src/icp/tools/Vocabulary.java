package icp.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Vocabulary.
 * 
 * @author NLP group, Faculty of Applied Sciences, University of West Bohemia
 * 
 */
public class Vocabulary {

	private List<String> vocabulary;
	private Map<String, Integer> wordToKeyMap;
	private int counter = 0;
	public static final String SENTENCE = "<s>";
	
	
	public Vocabulary() {
		this.vocabulary = new ArrayList<String>();
		this.wordToKeyMap = new HashMap<String, Integer>();
		/*this.vocabulary.add(SENTENCE);
		wordToKeyMap.put(SENTENCE, 0);
		counter++;*/
	}

	
	public void add(String word) {
		if (!wordToKeyMap.containsKey(word)) {
			wordToKeyMap.put(word, counter);
			vocabulary.add(word);
			counter++;
		}
	}
	
	public String getWordByKey(int key) {
		if (key < 0 || key >= vocabulary.size()) throw new IllegalArgumentException("word key ("+key+") must range between 0 and "+(vocabulary.size()-1));
		return vocabulary.get(key);
	}
	
	public String[] getWordByKey(int key[]) {
		String[] words = new String[key.length];
		for (int i=0; i<words.length; i++) words[i] = getWordByKey(key[i]);
		return words;
	}
	
	public int getWordKey(String word) {
		Integer key = this.wordToKeyMap.get(word);
		return (key != null ? key : -1);
	}
	
	public int[] getWordKey(String[] words) {
		int[] keys = new int[words.length];
		for (int i=0; i<words.length; i++) keys[i] = getWordKey(words[i]);
		return keys;
	}
	
	public int size() {
		return this.vocabulary.size();
	}

	public List<String> getVocabulary() {
		return vocabulary;
	}
}
