package naivebayes.marshalp.com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class NBFileReader {
	
	
	public DataSet readFile(String filename) throws IOException{
		
		DataSet ds = new DataSet();
		
		FileReader fr = null;
		
		fr = new FileReader(filename);
		
		BufferedReader br = new BufferedReader(fr);
		
		String line="";
		
		int attrId = 0;
		
		while((line=br.readLine())!=null){
			
			Example ex = new Example();
			
			//System.out.println(line);
			
			String[] attributes = line.split(",");
			for(String att:attributes){
				
				Attribute a = new Attribute(att, attrId);
				
				ex.addAttribute(a);
				
				if(NaiveBayesMain.AttibuteValuesMap.containsKey(attrId)){
					
					HashSet<String> hs = NaiveBayesMain.AttibuteValuesMap.get(attrId);
					hs.add(att);
				}
				else{
					HashSet<String> hs = new HashSet<String>();
					hs.add(att);
					NaiveBayesMain.AttibuteValuesMap.put(attrId, hs);
				}
				
				attrId++;
			}
			//
			ds.addRows(ex);
			attrId = 0;
			
		}
		//System.out.println(ds.getSize());
		return ds;
	}

}
