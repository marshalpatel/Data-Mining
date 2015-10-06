package marshalp.ID3.com;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ID3 {

	private ID3Node root;

	ID3Global globalObj = ID3Global.getInstance();

	public void printID3() {
		traverseID3(root);
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

	public int classifyID3(DataSet dataset,
			HashMap<String, HashMap<String, Integer>> confusionMatrix,
			int treeDepth) {

		initializeConfusionMatrix(confusionMatrix);

		List<Example> rows = dataset.getExamples();
		int correctOutput = 0;

		for (Example row : rows) {
			String predictedClassLabel = predictClassLabel(root, row, treeDepth);

			String expectedClassLabel = row.getAttribute(
					globalObj.getClassVariable()).getValue();

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
			// System.out.println(predictedClassLabel);
		}
		// printMatrix(confusionMatrix);
		return correctOutput;
	}

	@SuppressWarnings("unchecked")
	public String predictClassLabel(ID3Node root, Example row, int treeDepth) {
		String predictedClassLabel = null;

		if (root.getValue() == -1) {
			DataSet tempDS = root.getDataset();

			Example firstRow = tempDS.getExamples().get(0);

			predictedClassLabel = firstRow.getAttribute(
					globalObj.getClassVariable()).getValue();

			return predictedClassLabel;
		}

		if (treeDepth == 0) {
			predictedClassLabel = getMaxCountClassLabel(root);

			return predictedClassLabel;
		}

		Attribute currentNode = row.getAttribute(root.getValue());

		Map<String, ID3Node> currentNodeChildren = root.getChildren();

		Iterator<?> itr = currentNodeChildren.entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry<String, ID3Node> child = (Map.Entry<String, ID3Node>) itr
					.next();

			if (((String) child.getKey()).equals((String) currentNode.getValue())) {
				predictedClassLabel = predictClassLabel(
						(ID3Node) child.getValue(), row, treeDepth - 1);
				break;
			}
		}

		if (predictedClassLabel == null) {
			predictedClassLabel = getMaxCountClassLabel(root);
		}

		return predictedClassLabel;
	}

	@SuppressWarnings("unchecked")
	public String getMaxCountClassLabel(ID3Node root) {
		String maxCountClassLabel = null;

		int maxCount = Integer.MIN_VALUE;

		Iterator<?> itrCount = root.getLabel_count().entrySet().iterator();

		while (itrCount.hasNext()) {
			Map.Entry<String, Integer> labelCount = (Map.Entry<String, Integer>) itrCount
					.next();

			if (maxCount < (int) labelCount.getValue()) {
				maxCount = (int) labelCount.getValue();
				maxCountClassLabel = (String) labelCount.getKey();
			}
		}
		return maxCountClassLabel;
	}

	public void traverseID3(ID3Node root) {

		System.out.println("Parent Node: " + root.getValue());
		System.out.println();

		Iterator<?> itr = root.getChildren().entrySet().iterator();

		while (itr.hasNext()) {
			Map.Entry<String, ID3Node> item = (Map.Entry<String, ID3Node>) itr
					.next();

			System.out.println("\tEdge Value: " + item.getKey());
			System.out
					.println("\tChildren Node: " + item.getValue().getValue());
			System.out.println();
		}

		itr = root.getChildren().entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ID3Node> item = (Map.Entry<String, ID3Node>) itr
					.next();

			traverseID3((ID3Node) item.getValue());
		}
	}

	/*
	 * public int getNumberOfLeafNodes(ID3Node root) { LinkedList<ID3Node> queue
	 * = new LinkedList<ID3Node>();
	 * 
	 * int maxSize = 0;
	 * 
	 * queue.offer(root);
	 * 
	 * while (queue.size() > 0) { int size = queue.size(); if (size > maxSize) {
	 * maxSize = size; }
	 * 
	 * while (size > 0) { ID3Node currentNode = queue.poll();
	 * 
	 * Iterator<?> itr = currentNode.getChildren().entrySet().iterator();
	 * 
	 * while (itr.hasNext()) { Map.Entry<String, ID3Node> item =
	 * (Map.Entry<String, ID3Node>) itr.next();
	 * 
	 * queue.offer((ID3Node) item.getValue()); } size--; } } return maxSize; }
	 */
	/*
	 * @SuppressWarnings("unchecked") public void traverseID3(ID3Node root) {
	 * LinkedList<ID3Node> queue = new LinkedList<ID3Node>(); LinkedList<String>
	 * edgequeue = new LinkedList<String>();
	 * 
	 * 
	 * 
	 * queue.offer(root); edgequeue.offer(null);
	 * 
	 * while (queue.size() > 0) { int size = queue.size();
	 * 
	 * while (size > 0) { ID3Node currentNode = queue.poll(); String edge =
	 * edgequeue.poll(); int ss = getNumberOfLeafNodes(currentNode);
	 * 
	 * for (int i = 0 ; i < (ss-1) * 2 ; i++) { System.out.print(" "); }
	 * 
	 * System.out.print(currentNode.getValue());
	 * 
	 * if (edge != null) System.out.print(":" + edge + " "); else
	 * System.out.print(" ");
	 * 
	 * if (size == 0) { System.out.println(); }
	 * 
	 * Iterator<?> itr = currentNode.getChildren().entrySet().iterator();
	 * 
	 * while (itr.hasNext()) { Map.Entry<String, ID3Node> item =
	 * (Map.Entry<String, ID3Node>) itr.next();
	 * 
	 * queue.offer((ID3Node) item.getValue()); edgequeue.offer((String)
	 * item.getKey()); } size--; } System.out.println(); } }
	 */
	public ID3Node trainID3(DataSet ds,
			HashMap<Integer, HashSet<String>> attMap, int treeDepth) {

		if (ds.getSize() == 0 || treeDepth == -1)
			return null;

		// Compute Entropy of the data set
		double entropy_parent = calculateEntropy(ds);

		if (entropy_parent == 0) {
			ID3Node node = new ID3Node(-1, ds);
			countClassLabels(ds, node.getLabels());
			return node;
		}

		// Compute Info-gain
		double entropy_children = 0;
		double information_gain = Double.MIN_VALUE;
		int next_node_id = -1;
		Iterator<Map.Entry<Integer, HashSet<String>>> itr = attMap.entrySet()
				.iterator();

		List<DataSet> children_list = null;
		//
		while (itr.hasNext()) {
			entropy_children = 0.0;
			Map.Entry<Integer, HashSet<String>> item = itr.next();

			if ((int) item.getKey() == globalObj.getClassVariable())
				break;

			List<DataSet> dsList = createPartition(item.getKey(), ds);
			// System.out.print(item.getKey() + ": ");
			for (DataSet d : dsList) {
				// System.out.print(d.getSize() + " ");
				double probability = (double) d.getSize() / ds.getSize();
				entropy_children += probability * calculateEntropy(d);

			}
			// System.out.println();

			// System.out.println("Entropy_Children: " + entropy_children);
			if (information_gain < (entropy_parent - entropy_children)) {
				information_gain = entropy_parent - entropy_children;
				next_node_id = item.getKey();
				children_list = dsList;
			}
		}

		// System.out.println(next_node_id);
		ID3Node newNode = new ID3Node(next_node_id, ds);
		countClassLabels(ds, newNode.getLabels());

		if (root == null) {
			root = newNode;
			// return root;
		}

		// System.out.println(root.getValue());

		HashMap<Integer, HashSet<String>> tMap = new HashMap<Integer, HashSet<String>>(
				attMap);

		// tMap = (HashMap<Integer, HashSet<String>>) attMap.clone();
		tMap.remove(next_node_id);

		// System.out.println(next_node_id);
		for (DataSet d : children_list) {
			// System.out.println(d.getExamples().size());
			if (d.getExamples().size() > 0) {
				String edgeValue = d.getExamples().get(0)
						.getAttribute(next_node_id).getValue();

				ID3Node child = trainID3(d, tMap, treeDepth - 1);
				if (child != null)
					newNode.getChildren().put(edgeValue, child);
			}
		}

		return newNode;
	}

	public double calculateEntropy(DataSet dataset) {

		double entropy = 0.0;
		HashMap<String, Integer> classLabels = new HashMap<String, Integer>();

		countClassLabels(dataset, classLabels);

		Set<Map.Entry<String, Integer>> set = classLabels.entrySet();
		Iterator<Map.Entry<String, Integer>> itr = set.iterator();

		while (itr.hasNext()) {
			Map.Entry<String, Integer> item = itr.next();

			// System.out.println(item.getKey() + ": " + item.getValue());
			// System.out.println(item.getKey() + ": " + item.getValue());
			if ((int) item.getValue() > 0 && dataset.getSize() > 0)
				entropy += -((double) (int) item.getValue() / dataset.getSize())
						* ((Math.log10((double) (int) item.getValue()
								/ dataset.getSize())) / Math.log10(2));
		}
		// System.out.println("Entropy:" + entropy);
		return entropy;
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

	public List<DataSet> createPartition(int attrId, DataSet dataset) {
		HashSet<String> attributeValues = globalObj.getAttributeValues(attrId);
		List<DataSet> dataSetList = new ArrayList<DataSet>();
		Iterator<String> itr = attributeValues.iterator();

		List<Example> rows = dataset.getExamples();
		while (itr.hasNext()) {
			DataSet tempDataSet = new DataSet();
			String tempAttrValue = itr.next();

			for (Example row : rows) {
				if (row.getAttribute(attrId).value.equals(tempAttrValue)) {
					tempDataSet.addRows(row);
				}
			}
			dataSetList.add(tempDataSet);
		}

		for (DataSet ds : dataSetList) {
			for (Example row : ds.getExamples()) {
				for (Attribute att : row.getAttributes()) {
					// System.out.print(att + " ");
				}
				// System.out.println();
			}
			// System.out.println("------------------------------------");
		}
		return dataSetList;
	}

}
