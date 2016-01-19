package LR.com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Example implements Serializable{

	int rowId;
	List<Attribute> attributes;

	public Example() {
		attributes = new ArrayList<Attribute>();
	}

	public List<Attribute> getAttributes() {
		return this.attributes;
	}

	public Attribute getAttribute(int attrId) {
		for (Attribute attribute : attributes) {
			if (attribute.attrId == attrId)
				return attribute;
		}
		return null;
	}

	public void addAttributes(Attribute attr) {
		this.attributes.add(attr);
	}

	public void printRow() {
		for (Attribute attr : attributes) {
			System.out.print(attr + " ");
		}
		System.out.println();
	}
}
