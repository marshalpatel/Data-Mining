package naivebayes.marshalp.com;

public class Attribute {

	String val;
	int attId;
	
	public Attribute(String v, int id){
		
		this.val = v;
		this.attId = id;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

	public int getAttId() {
		return attId;
	}

	public void setAttId(int attId) {
		this.attId = attId;
	}
	
}
