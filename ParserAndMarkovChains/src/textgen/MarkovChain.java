package textgen;

import java.util.HashMap;

public class MarkovChain {
	public HashMap<String,BagOfObjects<String>> map = new HashMap<String,BagOfObjects<String>>();
	
	public MarkovChain(){
		
	}
	
	public void Add(String current, String next){
		if(map.containsKey(current)){
			map.get(current).Add(next);
		}else{
			map.put(current, new BagOfObjects<String>(next));
		}
	}
	
	public String getNext(String current){
		if(map.containsKey(current))
			return map.get(current).GetRandom();
		return "<ERROR: NoData>";
	}
	
	public boolean HasNext(String current){
		return map.containsKey(current);
	}
	
}
