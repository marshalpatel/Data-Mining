package LR.com;

import java.io.Serializable;

public class Attribute implements Serializable {

	int attrId;
	String value;

	public Attribute(int id, String val) {
		this.value = val;
		this.attrId = id;
	}

	public String toString() {
		return this.value.toString();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
