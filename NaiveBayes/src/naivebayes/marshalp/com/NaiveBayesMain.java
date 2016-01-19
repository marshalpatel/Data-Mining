package naivebayes.marshalp.com;

import java.io.IOException;
import java.util.*;

public class NaiveBayesMain {
	
	public static HashMap<Integer,HashSet<String>> AttibuteValuesMap = new HashMap<Integer,HashSet<String>>();
	private String trainFile, testFile;
	private int classVariable;

	private static NaiveBayesMain NBMain;

	public void parseArguments(String[] args) {
		try {
			NBMain.setTrainFile(args[0]);
			NBMain.setTestFile(args[1]);
			NBMain.setClassVariable(Integer.parseInt(args[2]) - 1);

		} catch (NumberFormatException e) {
			System.out.println("Invalid Value for Learning Rate...");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Invalid Arguments...");
		}
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		DataSet ds, testDs;
		NBFileReader fr = new NBFileReader();

		NBMain = new NaiveBayesMain();
		NBMain.parseArguments(args);

		HashMap<String, HashMap<String, Integer>> confusionMatrix = new HashMap<String, HashMap<String,Integer>>();
		
		ds = fr.readFile(NBMain.getTrainFile());
		testDs = fr.readFile(NBMain.getTestFile());
		
		NaiveBayes nbobj = new NaiveBayes(ds,NBMain.getClassVariable());
		
		nbobj.setLabelCount(NBMain.getClassVariable());
		
		nbobj.trainNB(NBMain.getClassVariable());

		System.out.println("===================================================================");
		System.out.println("Predictions: ");
		System.out.println("===================================================================");
		System.out.println();
		int correctClass = nbobj.classifyNB(testDs, confusionMatrix);

		
		//train

	}

	public String getTrainFile() {
		return trainFile;
	}

	public void setTrainFile(String trainFile) {
		this.trainFile = trainFile;
	}

	public String getTestFile() {
		return testFile;
	}

	public void setTestFile(String testFile) {
		this.testFile = testFile;
	}

	public int getClassVariable() {
		return classVariable;
	}

	public void setClassVariable(int classVariable) {
		this.classVariable = classVariable;
	}
}
