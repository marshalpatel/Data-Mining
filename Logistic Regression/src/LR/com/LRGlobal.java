package LR.com;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class LRGlobal {

private static LRGlobal instance = new LRGlobal();
	
	private HashMap<Integer, HashSet<String>> attriList;
	private int classVariable;
	
	public LRGlobal() {
		this.attriList = new HashMap<Integer, HashSet<String>>();
	}
	
	public void printAttributeList() {
		Set printSet = this.attriList.entrySet();
		
		Iterator itr = printSet.iterator();
		
		while (itr.hasNext()) {
			Map.Entry item = (Map.Entry) itr.next();
			
			System.out.print(item.getKey() + ": ");
			
			Iterator itr2 = ((HashSet<String>)item.getValue()).iterator();
			
			while (itr2.hasNext()) {
				System.out.print(itr2.next() + " ");
			}
			
			System.out.println();
		}
	}

	public HashSet<String> getAttributeValues(int attrId) {
		return (HashSet<String>)attriList.get(attrId);
	}
	
	public void putAttribute(int attrId, HashSet<String> attributeValues) {
		attriList.put(attrId, attributeValues);
	}
	
	public static LRGlobal getInstance() {
		return instance;
	}

	public int getClassVariable() {
		return classVariable;
	}

	public void setClassVariable(int classVariable) {
		this.classVariable = classVariable;
	}

	public static void setInstance(LRGlobal instance) {
		LRGlobal.instance = instance;
	}
	
	public HashMap<Integer, HashSet<String>> getAttributeMap(){
		return attriList;
	}
	
}
