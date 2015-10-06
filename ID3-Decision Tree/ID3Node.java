package marshalp.ID3.com;

import java.util.HashMap;
import java.util.Map;

public class ID3Node {

	private int value;
	// private String edge;
	private DataSet dataset;
	private Map<String, ID3Node> children;
	private HashMap<String, Integer> label_count;

	public ID3Node(int value, DataSet dataset) {
		this.value = value;
		label_count = new HashMap<String, Integer>();
		children = new HashMap<String, ID3Node>();
		this.dataset = dataset;
	}
	
	

	public HashMap<String, Integer> getLabel_count() {
		return label_count;
	}

	public void setLabel_count(HashMap<String, Integer> label_count) {
		this.label_count = label_count;
	}

	public Map<String, ID3Node> getChildren() {
		return this.children;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public DataSet getDataset() {
		return dataset;
	}

	public void setDataset(DataSet dataset) {
		this.dataset = dataset;
	}

	public void setChildren(Map<String, ID3Node> children) {
		this.children = children;
	}
	
	public HashMap<String, Integer> getLabels(){
		
		return this.label_count;
		
	}

}
