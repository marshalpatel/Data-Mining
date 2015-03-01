package com.marshalp.aggclustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/*Core class where clustering routine resides*/

public class PerformClustering {
	int newID = 0;
	private Map<DataPoints, Pairs> distMap;
	private Map<DataPoints, ArrayList<DataPoints>> cluster;

	/* Constructor */
	public PerformClustering() {

		distMap = new HashMap<DataPoints, Pairs>();
		cluster = new HashMap<DataPoints, ArrayList<DataPoints>>();

	}

	/* Method to create distence matric between all the data points */

	public void CreateDistanceMatrix(ArrayList<DataPoints> dataPoint) {

		float minDistance = Float.MAX_VALUE;
		float tDistance;
		DataPoints minPoint = null;

		for (int i = 0; i < dataPoint.size(); i++) {

			for (int j = i + 1; j < dataPoint.size(); j++) {

				tDistance = getDistance(dataPoint.get(i), dataPoint.get(j));
				if (minDistance > tDistance) {

					minDistance = tDistance;
					minPoint = dataPoint.get(j);
				}

			}

			distMap.put(dataPoint.get(i), minPoint == null ? null : new Pairs(
					minPoint, minDistance));
			minDistance = Float.MAX_VALUE;
			minPoint = null;
		}

		AgglomerativeClustering();

	}

	/* Method to compute distance between 2 data points */
	private float getDistance(DataPoints u1, DataPoints u2) {

		int tempId1 = u1.getyPoint() - u2.getyPoint();
		int tempId2 = u2.getxValue() - u1.getxValue();
		float distance = (float) Math.sqrt((tempId1 * tempId1)
				+ (tempId2 * tempId2));

		return distance;

	}

	/* Method to perform agglomerative clustering */

	private void AgglomerativeClustering() {

		float minDist = Float.MAX_VALUE;
		

		DataPoints tPoint1 = null;
		DataPoints tPoint2 = null;
		DataPoints newNode = null;

		ArrayList<DataPoints> leftChild, rightChild;

		System.out.println(distMap.size());

		for (Map.Entry<DataPoints, Pairs> entry : distMap.entrySet()) {
			if (entry.getKey().getPointId() > newID)
				newID = entry.getKey().getPointId();

		}
		newID++;

		/* Perform clustering up to 30 levels */

		while (distMap.size() > 30) {

			for (Map.Entry<DataPoints, Pairs> entry : distMap.entrySet()) {
				if (entry.getValue() != null) {

					if (minDist > entry.getValue().getDistance()) {

						minDist = entry.getValue().getDistance();
						tPoint2 = entry.getValue().getDest();
						tPoint1 = entry.getKey();
					}

				}

			}

			distMap.remove(tPoint1);
			distMap.remove(tPoint2);

			if (tPoint1 != null && tPoint2 != null)
				newNode = new DataPoints(newID,
						(tPoint1.getyPoint() + tPoint2.getyPoint()) / 2,
						(tPoint1.getxValue() + tPoint2.getxValue()) / 2);

			leftChild = cluster.get(tPoint1);
			if (leftChild == null) {
				leftChild = new ArrayList<DataPoints>();
				leftChild.add(tPoint1);

			}
			rightChild = cluster.get(tPoint2);
			if (rightChild == null) {
				rightChild = new ArrayList<DataPoints>();
				rightChild.add(tPoint2);

			}
			leftChild.addAll(rightChild);

			cluster.put(newNode, leftChild);

			cluster.remove(tPoint1);
			cluster.remove(tPoint2);
			updateDistanceMap(newNode);
			newID++;
			// reseting
			minDist = Float.MAX_VALUE;
			tPoint1 = null;
			tPoint2 = null;
		}

		System.gc();
		
		//Printing Clusters to screen.
		for (Map.Entry<DataPoints, ArrayList<DataPoints>> entry : cluster
				.entrySet()) {
			System.out.println("============================");
			System.out.println(entry.getValue());

		}
		
	}

	/* Method to update distance matrix after combining 2 data points and adding new node */
	private void updateDistanceMap(DataPoints nNode) {

		for (Map.Entry<DataPoints, Pairs> entry : distMap.entrySet()) {

			if (entry.getValue() != null) {
				if (!distMap.containsKey(entry.getValue().getDest())
						|| entry.getValue().getDistance() > getDistance(nNode,
								entry.getKey())) {

					entry.getValue().setDest(nNode);
					entry.getValue().setDistance(
							getDistance(nNode, entry.getKey()));
				}
			} else {

				distMap.put(entry.getKey(),
						new Pairs(nNode, getDistance(entry.getKey(), nNode)));
			}

		}
		distMap.put(nNode, null);
	}

}
