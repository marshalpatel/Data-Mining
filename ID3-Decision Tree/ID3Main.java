package marshalp.ID3.com;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class ID3Main {
	
	private String trainFile, testFile;
	private int maxTreeDepth;
	private static ID3Main id3Main;
	static ID3Global obj = ID3Global.getInstance();
	
	public void parseArguments (String[] args) {
		try {
		id3Main.setTrainFile(args[0]);
		id3Main.setTestFile(args[1]);
		obj.setClassVariable(Integer.parseInt(args[2]) - 1);
		id3Main.setMaxTreeDepth(Integer.parseInt(args[3]));
		}
		catch (NumberFormatException e) {
			System.out.println("Invalid Value for Depth of Tree...");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Invalid Arguments...");
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		id3Main = new ID3Main();
		id3Main.parseArguments(args);

		ID3FileReader fr = new ID3FileReader();
		DataSet dataset, testDataset;
		
		
		HashMap<String, HashMap<String, Integer>> confusionMatrix;
		
		ID3 id3 = new ID3();

		try {
			dataset = fr.readFile(id3Main.getTrainFile(), false);
			testDataset = fr.readFile(id3Main.getTestFile(), true);
			
//			System.out.println(obj.getAttributeMap().size() - 1);
//			obj.setClassVariable(obj.getAttributeMap().size() - 1);
			
			id3.trainID3(dataset, obj.getAttributeMap(), id3Main.getMaxTreeDepth());
			
			int totalValues = testDataset.getSize();
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("////                          Task 1                            ////");
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println();
			
			for (int i = 0 ; i <= id3Main.getMaxTreeDepth() ; i++) {
				confusionMatrix = new HashMap<String, HashMap<String, Integer>>();
				int truePredictions = id3.classifyID3(testDataset, confusionMatrix, i);
				
				System.out.println("Number of Missclassification at Depth " + i + ": " + (totalValues - truePredictions));
				System.out.println("Accuracy at Depth " + i + ": " + ((((float) truePredictions / totalValues)) * 100));
				System.out.println("Error at Depth " + i + ": " + ((1 - ((float) truePredictions / totalValues)) * 100));
				System.out.println();
			}
			
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println("////                          Task 2                            ////");
			System.out.println("////////////////////////////////////////////////////////////////////");
			System.out.println();
			
			for (int i = 1 ; i <= 2 ; i++) {
				System.out.println("======== Depth " + i + " ========");
				
				id3.trainID3(dataset, obj.getAttributeMap(), i);
				
				confusionMatrix = new HashMap<String, HashMap<String, Integer>>();
				
				id3.classifyID3(testDataset, confusionMatrix, i);
				System.out.println("=> Confusion Matrix: ");
				id3.printMatrix(confusionMatrix);

				System.out.println();
				System.out.println("=> Decision Tree: ");
				id3.printID3();
				
			}

			System.out.println("//////////////////////////////////////////////////////////////////////////");
			System.out.println("////                    Overall Result for Max Depth                  ////");
			System.out.println("//////////////////////////////////////////////////////////////////////////");
			System.out.println();
			id3.trainID3(dataset, obj.getAttributeMap(), id3Main.getMaxTreeDepth());
			
			confusionMatrix = new HashMap<String, HashMap<String, Integer>>();
			int truePredictions = id3.classifyID3(testDataset, confusionMatrix, id3Main.getMaxTreeDepth());
			
			System.out.println("=> Confusion Matrix: ");
			id3.printMatrix(confusionMatrix);
			System.out.println();
			
			float accuracy = ((float) truePredictions / totalValues) * 100;
			
			System.out.println("=> Accuracy: " + accuracy);
			System.out.println();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error Reading Dataset...");
		}
	}

	/**
	 * @return the trainFile
	 */
	public String getTrainFile() {
		return trainFile;
	}

	/**
	 * @param trainFile the trainFile to set
	 */
	public void setTrainFile(String trainFile) {
		this.trainFile = trainFile;
	}

	/**
	 * @return the testFile
	 */
	public String getTestFile() {
		return testFile;
	}

	/**
	 * @param testFile the testFile to set
	 */
	public void setTestFile(String testFile) {
		this.testFile = testFile;
	}

	/**
	 * @return the maxTreeDepth
	 */
	public int getMaxTreeDepth() {
		return maxTreeDepth;
	}

	/**
	 * @param maxTreeDepth the maxTreeDepth to set
	 */
	public void setMaxTreeDepth(int maxTreeDepth) {
		this.maxTreeDepth = maxTreeDepth;
	}
}
