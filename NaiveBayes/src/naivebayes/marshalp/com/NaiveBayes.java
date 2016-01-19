package naivebayes.marshalp.com;

import java.util.*;

public class NaiveBayes {

	DataSet ds;
	HashMap<String, Double> priorProb;
	HashMap<String, Integer> labelCount;
	HashMap<String, DataSet> labelDataSetMap;
	HashMap<Integer, HashMap<String, HashMap<String, Double>>> probMap;
	int labelAttrId;

	public NaiveBayes(DataSet d, int label) {

		this.ds = d;
		this.labelCount = new HashMap<String, Integer>();
		this.labelDataSetMap = new HashMap<String, DataSet>();
		this.probMap = new HashMap<Integer, HashMap<String, HashMap<String, Double>>>();
		this.labelAttrId = label;

	}

	/*
	 * Method to set the label map: label value -> Count. Laplace smoothing not
	 * applied. Used for computing priors.
	 */

	public void initializeClassLabels(HashMap<String, Integer> classLabels) {
		HashSet<String> classLabelValues = NaiveBayesMain.AttibuteValuesMap
				.get(labelAttrId);

		Iterator<String> itr = classLabelValues.iterator();

		while (itr.hasNext()) {
			classLabels.put(itr.next(), 0);
		}
	}

	public void initializeConfusionMatrix(
			HashMap<String, HashMap<String, Integer>> confusionMatrix) {
		Iterator<?> itr = NaiveBayesMain.AttibuteValuesMap.get(labelAttrId)
				.iterator();

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

	public int classifyNB(DataSet dataset,
			HashMap<String, HashMap<String, Integer>> confusionMatrix) {

		initializeConfusionMatrix(confusionMatrix);

		int correctOutput = 0;

		for (Example row : dataset.getRows()) {
			String predictedClassLabel = predictClassLabel(row);

			String expectedClassLabel = row.getAttribute(labelAttrId).getVal();

			HashMap<String, Integer> predictedClassLabelList = new HashMap<String, Integer>(
					confusionMatrix.get(expectedClassLabel));

			int predictedClassLabelCount = predictedClassLabelList
					.get(predictedClassLabel);

			predictedClassLabelList.put(predictedClassLabel,
					++predictedClassLabelCount);

			confusionMatrix.put(expectedClassLabel, predictedClassLabelList);

			if (predictedClassLabel.equals(expectedClassLabel)) {
				correctOutput++;
			}
		}
		return correctOutput;
	}

	public void initPGivenXMap(HashMap<String, Double> pYgivenX) {
		HashSet<String> classLabels = NaiveBayesMain.AttibuteValuesMap
				.get(labelAttrId);

		Iterator itr = classLabels.iterator();

		while (itr.hasNext()) {
			String classLabelTemp = (String) itr.next();
			pYgivenX.put(classLabelTemp, 1.0);
		}
	}

	public String predictClassLabel(Example row) {
		String predictedClassLabel = null;

		HashMap<String, Double> pYgivenX = new HashMap<String, Double>();
		initPGivenXMap(pYgivenX);

		HashSet<String> classLabel = NaiveBayesMain.AttibuteValuesMap
				.get(labelAttrId);
		Iterator itr;

		for (Attribute feature : row.getAttributes()) {
			if (feature.getAttId() != labelAttrId) {
				itr = classLabel.iterator();

				while (itr.hasNext()) {
					String classLabelTemp = (String) itr.next();
					double pYgivenXTemp = probMap.get(feature.getAttId())
							.get(feature.getVal()).get(classLabelTemp);

					if (pYgivenX.containsKey(classLabelTemp)) {
						double pYGivenXOld = pYgivenX.get(classLabelTemp);
						pYgivenX.put(classLabelTemp, pYgivenXTemp * pYGivenXOld);
					}
				}
			}
		}

		itr = classLabel.iterator();
		double probSum = 0.0;

		while (itr.hasNext()) {
			String classLabelTemp = (String) itr.next();
			double prob = priorProb.get(classLabelTemp)
					* pYgivenX.get(classLabelTemp);

			pYgivenX.put(classLabelTemp, prob);
			probSum += pYgivenX.get(classLabelTemp);
		}

		itr = classLabel.iterator();

		while (itr.hasNext()) {
			String classLabelTemp = (String) itr.next();
			double normProb = pYgivenX.get(classLabelTemp) / probSum;

			pYgivenX.put(classLabelTemp, normProb);
		}

		itr = classLabel.iterator();

		double maxValue = Double.MIN_VALUE;

		while (itr.hasNext()) {
			String classLabelTemp = (String) itr.next();
			
//			System.out.println(classLabelTemp + " : " + pYgivenX.get(classLabelTemp));
			if (maxValue < pYgivenX.get(classLabelTemp)) {
				maxValue = pYgivenX.get(classLabelTemp);
				predictedClassLabel = classLabelTemp;
			}
		}

//		System.out.println(predictedClassLabel);
		System.out.println(predictedClassLabel);
		return predictedClassLabel;
	}

	public void setLabelCount(int attrId) {

		HashSet<String> labelVals = NaiveBayesMain.AttibuteValuesMap
				.get(attrId);

		priorProb = new HashMap<String, Double>();

		Iterator<String> itr = labelVals.iterator();

		while (itr.hasNext()) {
			// this.labelCount.put(itr.next(), labelVals.size());
			// this.dsList.add(new DataSet());
			this.labelCount.put(itr.next(), 0);
		}

		for (Example row : this.ds.getRows()) {

			String attrValue = row.getAttribute(attrId).val;
			int count = 0;

			count = this.labelCount.get(attrValue) + 1;

			this.labelCount.put(attrValue, count);

			double priorProb = ((double) (count+1) / (ds.getRows().size() + labelVals.size()));
			this.priorProb.put(attrValue, priorProb);

		}

//		System.out
//				.println("p(y=0) --> "
//						+ (double) (this.labelCount.get("0") + 1)
//						/ (this.labelCount.get("0") + this.labelCount.get("1") + labelVals
//								.size()));
//		System.out
//				.println("p(y=1) --> "
//						+ (double) (this.labelCount.get("1") + 1)
//						/ (this.labelCount.get("0") + this.labelCount.get("1") + labelVals
//								.size()));
//		System.out
//				.println("--------------------------------------------------------");

		itr = labelVals.iterator();

		while (itr.hasNext()) {
			// this.labelCount.put(itr.next(), labelVals.size());
			// this.dsList.add(new DataSet());
			String attrVal = itr.next();
			DataSet temp = new DataSet();
			for (Example row : this.ds.getRows()) {

				if (row.getAttribute(attrId).val.equals(attrVal)) {
					temp.addRows(row);
				}
			}

			// this.dsList.add(temp);
			this.labelDataSetMap.put(attrVal, temp);

		}

		/*
		 * for(Example ex : this.dsList.get(0).getRows()){
		 * System.out.println(ex); }
		 */
	}

	/*
	 * Method to train Naive Bayes Model with Laplace correction. Populates the
	 * class the variable probMap with conditional probabilities
	 */

	public void trainNB(int labelAttrId) {

		// HashMap<Integer, HashMap<String,HashMap<String,Integer>>> CountMap;
		int attrCount = this.ds.getRows().get(0).getAttributes().size();

		for (int i = 0; i < attrCount - 1; i++) {

			this.probMap.put(i, null);

		}

		for (int i = 0; i < attrCount - 1; i++) {
			this.probMap.put(i, getAttributeCountMap(i));
		}
	}

	/*
	 * Support methods to populate probMap.
	 */

	private HashMap<String, HashMap<String, Double>> getAttributeCountMap(
			int attrId) {

		HashMap<String, HashMap<String, Double>> cMap = new HashMap<String, HashMap<String, Double>>();

		HashSet<String> hs = NaiveBayesMain.AttibuteValuesMap.get(attrId);

		Iterator<String> hsItr = hs.iterator();

		while (hsItr.hasNext()) {

			String s = hsItr.next();
			cMap.put(s, null);
		}

		hsItr = hs.iterator();

		while (hsItr.hasNext()) {

			String s = hsItr.next();
			cMap.put(s, getCountGivenLabel(s, attrId));
		}

		return cMap;

	}

	/*
	 * Support method to populate probMap
	 */
	private HashMap<String, Double> getCountGivenLabel(String s, int attrId) {

		HashMap<String, Double> probGivenLabel = new HashMap<String, Double>();
		HashSet<String> labelSet = NaiveBayesMain.AttibuteValuesMap
				.get(this.labelAttrId);
		Iterator<String> itr = labelSet.iterator();
		HashSet<String> hs = NaiveBayesMain.AttibuteValuesMap.get(attrId);

		while (itr.hasNext()) {

			String labelValue = itr.next();

			DataSet temp = this.labelDataSetMap.get(labelValue);
			int count = 0;

			for (Example ex : temp.getRows()) {

				if (ex.getAttribute(attrId).val.equals(s)) {
					count++;
				}

			}

			count += 1;
			double prob = (double) count
					/ (this.labelCount.get(labelValue) + hs.size());
			probGivenLabel.put(labelValue, prob);

		}

//		System.out.println("p(" + attrId + "=" + s + "|y=0) --> "
//				+ probGivenLabel.get("0"));
//		System.out.println("p(" + attrId + "=" + s + "|y=1) --> "
//				+ probGivenLabel.get("1"));
//		System.out
//				.println("--------------------------------------------------------");
		return probGivenLabel;
	}

}
