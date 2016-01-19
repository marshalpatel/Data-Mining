package naivebayes.marshalp.com;

import java.util.*;

public class DataSet {

	List<Example> rows;
	
	public DataSet(){
		rows = new ArrayList<Example>();
	}
	
	public int getSize(){
		return this.rows.size();
	}
	
	public List<Example> getRows(){
		return this.rows;
	}
	
	public void addRows(Example ex){
		
		this.rows.add(ex);
	}
}
