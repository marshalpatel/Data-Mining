package naivebayes.marshalp.com;

import java.util.*;

public class Example {
	
	List<Attribute> attributes;
	
	
	public Example(){
		
		attributes = new ArrayList<Attribute>();
	}
	
	public List<Attribute> getAttributes(){
		return this.attributes;
	}
	
	public Attribute getAttribute(int attId){
		
		for(Attribute a:attributes){
			
			if(a.attId==attId)
				return a;
		}
		
		return null;
		
	}
	
	
	public void addAttribute(Attribute a){
		
		this.attributes.add(a);
		
	}
	
	@Override
	
	public String toString(){
		
		String t="";
		
		for(Attribute a:attributes){
			
			t = t + a.val+" ";
		}
		
		return t;
		
	}
	
	
}
