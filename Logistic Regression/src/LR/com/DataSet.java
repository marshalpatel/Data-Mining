package LR.com;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataSet implements Serializable
{
	List<Example> rows;
	int size;
	
	public DataSet(){
		rows = new ArrayList<Example>();
	}
	
	public List<Example> getExamples(){
		
		return this.rows;
	}

	public Example getExample(int i) {
		return this.rows.get(i);
	}
	
	public int getSize(){
		return rows.size();
	}
	
	public void addRows(Example row){
		
		this.rows.add(row);
		
	}
}
