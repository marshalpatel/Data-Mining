package marshalp.ID3.com;

public class Attribute {

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
