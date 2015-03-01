package com.marshalp.aggclustering;

/*Class representing the destination node and distance from a particular node*/

public class Pairs {
	
	private DataPoints dest;
	
	private float distance;

	public Pairs(DataPoints destPoint, float distance) {
		
		this.dest = destPoint;
		this.distance = distance;	
	}

	
	public DataPoints getDest() {
		return dest;
	}

	public void setDest(DataPoints dest) {
		this.dest = dest;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}
	
}
