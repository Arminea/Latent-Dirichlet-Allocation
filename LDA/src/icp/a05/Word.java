package icp.a05;

import java.util.Arrays;

/**
 * Word.
 * 
 * @author Tereza Stanglova
 *
 */
public class Word {
	
	/** List of topic counts for word. */
	private int [] count;
	
	/**
	 * Constructor.
	 * 
	 * @param topicCount	count of topics.
	 */
	public Word(int topicCount) {
		this.count = new int [topicCount];
		
		init();
	}
	
	/**
	 * Fill counts array with zeros.
	 */
	private void init() {
		for(int j = 0; j < count.length; j++)
			count[j] = 0;
	}
	
	/**
	 * Increases count of given topic.
	 * 
	 * @param topicNumber	index
	 */
	public void increaseCount(int topicNumber) {
		count[topicNumber]++;
	}
	
	/**
	 * Decreases count of given topic.
	 * 
	 * @param topicNumber	index
	 */
	public void decreaseCount(int topicNumber) {
		count[topicNumber]--;
	}
	
	/**
	 * Return count of topic.
	 * @param topicNumber	index
	 * @return				count
	 */
	public int getCount(int topicNumber) {
		return count[topicNumber];
	}
	
	/**
	 * Returns a string representation of this class.
	 */
	public String toString() {
		return Arrays.toString(count);
	}
}
