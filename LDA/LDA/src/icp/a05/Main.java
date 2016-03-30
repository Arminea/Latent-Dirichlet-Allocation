package icp.a05;

import java.io.IOException;
import java.util.List;

/**
 * Main class. Launches LDA, Gibbs sampling and writes 
 * a list of the most likely words for every topic on output.
 * 
 * @author Tereza Stanglova
 *
 */
public class Main {
	
	private static final String FILE_NAME = "data/train.txt";
	private static final int NUMBER_OF_ITERATION = 100;
	private static final int TOPIC_COUNT = 50;
	
	public static void main(String[] args) throws IOException {
		//Test.testDocumentTrain();
		//Test.testDataRead();
		//Test.testDataSampling();
		//Test.testWordProbSumOneIter();
		//Test.testGibbsSamplingOneIter();
		//Test.testGibbsSamplingTenIters();
		
		
		
		LDAModel model = new LDAModel(FILE_NAME, NUMBER_OF_ITERATION, TOPIC_COUNT);
		model.GibbsSampling();
		
		List<String>[] list = model.getTopicWords(20);
		
		for(List<String> topwords : list) {
			System.out.println(topwords.toString());
		} 
	}

}
