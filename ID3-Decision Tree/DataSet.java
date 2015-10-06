package marshalp.ID3.com;

import java.util.ArrayList;
import java.util.List;

public class DataSet {
	
	List<Example> rows;
	int size;
	
	public DataSet(){
		rows = new ArrayList<Example>();
	}
	
	public List<Example> getExamples(){
		
		return this.rows;
	}
	
	public int getSize(){
		return rows.size();
	}
	
	public void addRows(Example row){
		
		this.rows.add(row);
		
	}
}
