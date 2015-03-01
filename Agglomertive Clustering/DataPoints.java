package com.marshalp.aggclustering;

/*Class representing data points*/

public class DataPoints {

	private int pointId;
	private int yPoint;
	private int xValue;

	public DataPoints(int id, int y, int x) {

		this.yPoint = y;
		this.xValue = x;
		this.pointId = id;
	}

	public int getPointId() {
		return pointId;
	}

	public void setPointId(int pointId) {
		this.pointId = pointId;
	}

	public int getyPoint() {
		return yPoint;
	}

	public void setyPoint(int yPoint) {
		this.yPoint = yPoint;
	}

	public int getxValue() {
		return xValue;
	}

	public void setxValue(int xValue) {
		this.xValue = xValue;
	}

	/*Ovrriding inbuilt toString method to print the contents of the object*/
	
	@Override
	public String toString() {
		if (ACMain.isUser())
			return "User-ID:" + pointId + " Age:" + yPoint + " Profession:"
					+ xValue + "\n";
		else
			return "MovieId:" + pointId + " Genre:" + yPoint + " Rating:"
					+ xValue + "\n";
	}

	/*
	 * Overriding inbuilt equals method for deciding whether objects are
	 * identical while hashing
	 */

	@Override
	public boolean equals(Object obj) {

		if (obj != null) {
			DataPoints t;
			t = (DataPoints) obj;
			return (t.yPoint == this.yPoint && t.xValue == this.xValue && t.pointId == this.pointId);
		}

		return false;
	}

	/* Overriding hashcode method for generating unique hashcode */

	@Override
	public int hashCode() {
		return (this.pointId + "," + this.yPoint + "," + this.xValue)
				.hashCode();
	}

}
