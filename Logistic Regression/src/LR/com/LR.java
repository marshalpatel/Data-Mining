package LR.com;

import org.apache.commons.lang3.SerializationUtils;

import java.util.*;

public class LR {

    LRGlobal globalObj = LRGlobal.getInstance();

    double averageWeights[];
    double[][] weights;

    public int classifyLR(DataSet testDataSet,
                          HashMap<String, HashMap<String, Integer>> confusionMatrix) {

//		ArrayList<DataSet> dataSets = handleMultinomialDataset(testDataSet);
        ArrayList<DataSet> dataSets = new ArrayList<>();
        dataSets.add(testDataSet);

        initializeConfusionMatrix(confusionMatrix);

        List<Example> rows = testDataSet.getExamples();
        int correctOutput = 0;
//
//        for (DataSet dataSet : dataSets) {
//            printDataSet(dataSet);
//        }

//		for (Example row : rows) {
        for (int i = 0; i < rows.size(); i++) {

            String predictedClassLabel = predictClassLabel(dataSets, i);

            String expectedClassLabel = rows.get(i).getAttribute(
                    globalObj.getClassVariable()).getValue();
//            System.out.println(expectedClassLabel);
//
//			System.out.print(confusionMatrix.get(expectedClassLabel));
//
            HashMap<String, Integer> predictedClassLabelList = new HashMap<String, Integer>(confusionMatrix.get(expectedClassLabel));
//            System.out.println(predictedClassLabelList);
//
            int predictedClassLabelCount = predictedClassLabelList.get(predictedClassLabel);

            predictedClassLabelList.put(predictedClassLabel,
                    ++predictedClassLabelCount);

            confusionMatrix.put(expectedClassLabel, predictedClassLabelList);

            if (predictedClassLabel.equals(expectedClassLabel)) {
                correctOutput++;
            }
            System.out.println(predictedClassLabel);
        }
//        printMatrix(confusionMatrix);
        return correctOutput;
    }

    public String predictClassLabel(ArrayList<DataSet> dataSets, int rowIndex) {
        int k = 0;
        double prediction = 0.0;
        double avgPrediction = 0.0;

        for (DataSet dataSet : dataSets) {
            prediction += getDotProduct(dataSet.getExample(rowIndex).getAttributes(), this.weights[k++]);
        }

        avgPrediction = prediction / dataSets.size();

        if (avgPrediction > 0)
            return "1";
        else
            return "0";
    }


    public void trainLR(DataSet inputDataSet, double learningRate, double convergenceValue, double numberOfIterations) {

        ArrayList<DataSet> dataSets = new ArrayList<DataSet>();
        dataSets.add(inputDataSet);

        int numberOfAttributes = inputDataSet.getExamples().get(0).getAttributes().size() - 1;

        double[][] gradients;

        double probabiltyRow = 0.0;
        double error = 0.0;

        boolean isConverged;

        weights = new double[dataSets.size()][numberOfAttributes + 1];
        int k = 0;
        for (DataSet dataSet : dataSets) {
            isConverged = false;
            int v = 0;
            while ((convergenceValue > 0 && !isConverged) || (numberOfIterations > 0 && v < numberOfIterations)) {
//            for (int v = 0; v < 30000; v++) {

                gradients = new double[dataSets.size()][numberOfAttributes + 1];

                for (Example row : dataSet.getExamples()) {
                    double dotProduct = getDotProduct(row.getAttributes(), weights[k]);
                    double classLabelValue = Double.parseDouble(row.getAttribute(globalObj.getClassVariable()).getValue());

                    double[] oldWeights = weights[k].clone();

                    probabiltyRow = 1 / (1 + Math.exp(-1 * dotProduct));
                    error = classLabelValue - probabiltyRow;

                    for (int i = 0; i < gradients[k].length; i++) {
                        if (i != globalObj.getClassVariable()) {
                            double attributeValue = Double.parseDouble(row.getAttributes().get(i).getValue());
                            gradients[k][i] += error * attributeValue;
                        }
                    }
                    updateWeights(weights[k], gradients[k], learningRate);

                    isConverged = checkConvergence(weights[k], oldWeights, convergenceValue);
                }
                v++;
            }
            k++;
        }
    }

    public boolean checkConvergence(double[] weights, double[] oldWeights, double convergenceValue) {
        boolean isConverged = true;

        for (int i = 0; i < weights.length; i++) {
            if (Math.abs(weights[i] - oldWeights[i]) > convergenceValue)
                isConverged = false;
        }

        return isConverged;
    }

    public void printDataSet(DataSet dataSet) {
        int l = 1;
        for (Example row : dataSet.getExamples()) {
            System.out.print(l++ + "     ");
            for (Attribute attr : row.getAttributes()) {
                System.out.print(attr.attrId + ":" + attr.getValue() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public ArrayList<DataSet> handleMultinomialDataset(DataSet dataSet) {
        ArrayList<DataSet> binomialDatasets = new ArrayList<DataSet>();
        ArrayList<DataSet> queueDatasets = new ArrayList<DataSet>();

        queueDatasets.add(dataSet);
        binomialDatasets.add(dataSet);

        int attrId = 0;

        while (queueDatasets.size() > 0) {
            DataSet tempDataSet = queueDatasets.remove(0);

            int i;
            for (i = attrId; i < globalObj.getAttributeMap().size(); i++) {
                if (i != globalObj.getClassVariable()) {
                    if (globalObj.getAttributeValues(i).size() > 2) {

                        if (binomialDatasets.contains(tempDataSet))
                            binomialDatasets.remove(tempDataSet);

                        ArrayList<DataSet> dataSets = splitDataSets(tempDataSet, i, globalObj.getAttributeValues(i));
                        queueDatasets.addAll(dataSets);
                        binomialDatasets.addAll(dataSets);
                        break;
                    }
                }
            }
            attrId = i + 1;

        }
        return binomialDatasets;
    }


    public ArrayList<DataSet> splitDataSets(DataSet dataSet, int attrId, HashSet<String> attributeValues) {
        ArrayList<DataSet> dataSets = new ArrayList<DataSet>();

        Iterator itr = attributeValues.iterator();

        while (itr.hasNext()) {
            String attributeValueToSplit = (String) itr.next();

            DataSet tempDataSet = SerializationUtils.clone(dataSet);

            for (Example row : tempDataSet.getExamples()) {
                if (!row.getAttribute(attrId).getValue().equals(attributeValueToSplit))
                    row.getAttribute(attrId).setValue("0");
                else
                    row.getAttribute(attrId).setValue("1");
            }
            dataSets.add(tempDataSet);
        }
        return dataSets;
    }

    public double[] getAverage(double[][] weights) {
        double[] averageWeights = new double[weights[0].length];
        double[] sum = new double[weights[0].length];

        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights[i].length; j++) {
                sum[j] += weights[i][j];
//				System.out.println(j + " : " + sum[j]);
            }
        }

        for (int j = 0; j < averageWeights.length; j++) {
            averageWeights[j] = sum[j] / weights.length;

        }

        return averageWeights;
    }

    public void updateWeights(double[] weights, double[] gradients, double learningRate) {
        for (int i = 0; i < weights.length; i++) {
            if (i != globalObj.getClassVariable()) {
                weights[i] += learningRate * gradients[i];
            }
        }
    }

    public double getDotProduct(List<Attribute> attributes, double[] weights) {
        double dotProduct = 0.0;

        for (int i = 0; i < attributes.size(); i++) {
            if (i != globalObj.getClassVariable()) {
                double attributeValue = Double.parseDouble(attributes.get(i).getValue());
                dotProduct += (weights[i] * attributeValue);
            }
        }
        return dotProduct;
    }

    public void initializeConfusionMatrix(
            HashMap<String, HashMap<String, Integer>> confusionMatrix) {
        Iterator<?> itr = globalObj.getAttributeValues(
                globalObj.getClassVariable()).iterator();

        HashMap<String, Integer> classLabels = new HashMap<String, Integer>();
        initializeClassLabels(classLabels);

        while (itr.hasNext()) {
            confusionMatrix.put((String) itr.next(), classLabels);
        }
    }

    @SuppressWarnings("unchecked")
    public void printMatrix(HashMap<String, HashMap<String, Integer>> matrix) {

        Boolean columnLabelsPrinted = false;
        Iterator<?> outerItr = matrix.entrySet().iterator();

        while (outerItr.hasNext()) {
            Map.Entry<String, HashMap<String, Integer>> matrixRow = (Map.Entry<String, HashMap<String, Integer>>) outerItr
                    .next();

            Iterator<?> innerItr = ((HashMap<String, Integer>) matrixRow
                    .getValue()).entrySet().iterator();

            if (!columnLabelsPrinted) {
                Iterator<String> keyItr = ((HashMap<String, Integer>) matrixRow
                        .getValue()).keySet().iterator();
                System.out.print("   ");
                while (keyItr.hasNext()) {

                    System.out.print(keyItr.next() + "  ");
                }

                System.out.println();
                columnLabelsPrinted = true;
            }

            System.out.print(matrixRow.getKey() + "  ");
            while (innerItr.hasNext()) {
                Map.Entry<String, Integer> matrixColumn = (Map.Entry<String, Integer>) innerItr
                        .next();
                System.out.print(matrixColumn.getValue() + "  ");
            }
            System.out.println();
        }
    }


    public void initializeClassLabels(HashMap<String, Integer> classLabels) {
        HashSet<String> classLabelValues = globalObj
                .getAttributeValues(globalObj.getClassVariable());

        Iterator<String> itr = classLabelValues.iterator();

        while (itr.hasNext()) {
            classLabels.put(itr.next(), 0);
        }
    }

    public void countClassLabels(DataSet dataset,
                                 HashMap<String, Integer> classLabels) {

        initializeClassLabels(classLabels);
        List<Example> rows = dataset.getExamples();

        for (Example row : rows) {
            Attribute classLabelAttribute = row.getAttribute(globalObj
                    .getClassVariable());

            Integer classLabelCount = classLabels
                    .get(classLabelAttribute.value);

            classLabels.put(classLabelAttribute.value, ++classLabelCount);
        }
    }

}
