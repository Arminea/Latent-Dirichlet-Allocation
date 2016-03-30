package icp.a05;

import icp.tools.Vocabulary;

import java.util.List;

/**
 * 
 * @author NLP group, Faculty of Applied Sciences, University of West Bohemia
 * 
 */
public interface TopicModel {

	/**
	 * Returns a list of the most likely words for every topic.
	 * @param maxWords length of arraylist
	 * @return
	 */
	public List<String>[] getTopicWords(int maxWords);
	
	/**
	 * Returns perplexity for train data set.
	 * @return
	 */
	public double getPerplexity();		
			
	/**
	 * Returns vocabulary.
	 * @return
	 */
	public Vocabulary getVocabulary();
	
}
