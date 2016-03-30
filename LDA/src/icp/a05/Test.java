package icp.a05;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Map.Entry;

/**
 * Auxiliary class for testing.
 * 
 * @author Tereza Stanglova
 *
 */
public class Test {
	
	private static final int TOPIC_COUNT = 50;
	private static final String FILE_NAME = "data/train.txt";
	private static final String FILE_NAME_TEST = "data/test.txt";
	private static final int NUMBER_OF_ITERATIONS		= 100;
	
	public static void testDocument() {
		Random r = new Random();
        int randNum;
		String [] sentence = {"Svět", "se", "rozloučil", "s", "rokem", "1998", "více", "méně", "tradičně", "." };
		Document doc = new Document(sentence.length, TOPIC_COUNT);
		
        for (int i = 0; i < sentence.length; i++) {
        	randNum = r.nextInt(TOPIC_COUNT); // <0, 50)
        	doc.addNewWord(i, sentence[i], randNum);
        }
        
        System.out.println(doc.toString());
	}
	
	public static void testDataRead() throws IOException {
		int topicCount = 15;
		LDADataset dataset = new LDADataset(FILE_NAME_TEST, topicCount);
		dataset.readDataset();
		
		System.out.println("Docs size: " + dataset.getDocs().size());
		for(Document d : dataset.getDocs()) {
			System.out.println(d.toString());
		}
		
		System.out.println("Topic counts: " + Arrays.toString(dataset.getTopicCounts())); //checked
		
		for(Entry<String, Word> entry : dataset.getWordTopicCounts().entrySet()) {
			String key = entry.getKey();
			Word value = entry.getValue();
			if(key.length() > 7 ) {
				System.out.println(key + "\t" + value.toString());
			} else {
				System.out.println(key + "\t\t" + value.toString());
			}
		}
		
	}
	
	public static void testDataSampling() throws IOException {
		int numberOfIterations = 10;
		int topicCount = 30;
		LDAModel model = new LDAModel(FILE_NAME_TEST, numberOfIterations, topicCount);
		model.GibbsSampling();
	}
	
	public static void testDocumentTrain() throws IOException {
		LDADataset dataset = new LDADataset(FILE_NAME, TOPIC_COUNT);
		dataset.readDataset();
		System.out.println("Docs size: " + dataset.getDocs().size());
		System.out.println("Word-topic counts size: " + dataset.getWordTopicCounts().size());
		System.out.println("Topic counts size: " + dataset.getTopicCounts().length);
		System.out.println("Count of word in documents: " + dataset.getNumberOfWordInDocs());
	}
	
	public static void testDocProbSumOneIter() throws IOException {
		int numberOfIterations = 1;
		LDAModel model = new LDAModel(FILE_NAME, numberOfIterations, TOPIC_COUNT);
		model.GibbsSampling();
		System.out.println("Working? - " + model.getDocProbSum());
	}
	
	public static void testWordProbSumOneIter() throws IOException {
		int numberOfIterations = 1;
		LDAModel model = new LDAModel(FILE_NAME, numberOfIterations, TOPIC_COUNT);
		model.GibbsSampling();
		System.out.println("Working? - " + model.getWordProbSum());
	}
	
	// ************* ITERS ********************
	
	public static void testGibbsSamplingOneIter() throws IOException {
		int numberOfIterations = 1;
		LDAModel model = new LDAModel(FILE_NAME_TEST, numberOfIterations, TOPIC_COUNT);
		model.GibbsSampling();
	}
	
	public static void testGibbsSamplingTenIters() throws IOException {
		int numberOfIterations = 10;
		LDAModel model = new LDAModel(FILE_NAME, numberOfIterations, TOPIC_COUNT);
		model.GibbsSampling();
	}
	
	public static void testGibbsSampling() throws IOException {
		LDAModel model = new LDAModel(FILE_NAME, NUMBER_OF_ITERATIONS, TOPIC_COUNT);
		model.GibbsSampling();
	}
	
	
}
