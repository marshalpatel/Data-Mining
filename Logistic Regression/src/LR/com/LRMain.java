package LR.com;

import java.io.IOException;
import java.util.HashMap;

public class LRMain {

    private String trainFile, testFile;

    private static LRMain LRMain;

    private double learningRate;
    private boolean isRateDependent;
    private double convergenceValue;
    private double numberOfIteration;

     static LRGlobal obj = LRGlobal.getInstance();

    public void parseArguments(String[] args) {
        try {
            LRMain.setTrainFile(args[0]);
            LRMain.setTestFile(args[1]);
            obj.setClassVariable(Integer.parseInt(args[2]) - 1);
            LRMain.setLearningRate(Double.parseDouble(args[3]));
            LRMain.setIsRateDependent(Boolean.parseBoolean(args[4]));

            if (LRMain.isRateDependent()) {
                LRMain.setConvergenceValue(LRMain.getLearningRate());
            }
            else {
                // Default Value
//                LRMain.setConvergenceValue(0.0001);
                LRMain.setNumberOfIteration(Double.parseDouble(args[5]));
            }
//            System.out.println(LRMain.getConvergenceValue());

        } catch (NumberFormatException e) {
            System.out.println("Invalid Value for Learning Rate...");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid Arguments...");
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        LRMain = new LRMain();
        LRMain.parseArguments(args);

        LRFileReader fr = new LRFileReader();
        DataSet dataset, testDataset;


        HashMap<String, HashMap<String, Integer>> confusionMatrix;

        LR lr = new LR();

        try {
            dataset = fr.readFile(LRMain.getTrainFile(), false);
            testDataset = fr.readFile(LRMain.getTestFile(), true);

            lr.trainLR(dataset, LRMain.getLearningRate(), LRMain.convergenceValue, LRMain.numberOfIteration);

            int totalValues = testDataset.getSize();
            System.out.println("========================================================================");
            System.out.println("Predictions:     ");
            System.out.println("========================================================================");
            System.out.println();

//			for (int i = 0 ; i <= id3Main.getMaxTreeDepth() ; i++) {
            confusionMatrix = new HashMap<String, HashMap<String, Integer>>();
            int truePredictions = lr.classifyLR(testDataset, confusionMatrix);
//
//            System.out.println("Accuracy: " + ((((float) truePredictions / totalValues)) * 100));
//            System.out.println("Error : " + ((1 - ((float) truePredictions / totalValues)) * 100));
//            System.out.println();
//			}
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error Reading Dataset...");
        }
    }

    public double getConvergenceValue() {
        return convergenceValue;
    }

    public void setConvergenceValue(double convergenceValue) {
        this.convergenceValue = convergenceValue;
    }

    public boolean isRateDependent() {
        return isRateDependent;
    }

    public void setIsRateDependent(boolean isRateDependent) {
        this.isRateDependent = isRateDependent;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
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

    public double getNumberOfIteration() {
        return numberOfIteration;
    }

    public void setNumberOfIteration(double numberOfIteration) {
        this.numberOfIteration = numberOfIteration;
    }


}
