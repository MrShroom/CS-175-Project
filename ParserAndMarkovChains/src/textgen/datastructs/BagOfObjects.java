package textgen.datastructs;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class BagOfObjects<T> {
	
	static Random rand = new Random();
	int total = 0;
	TreeMap<T,Integer> map = new TreeMap<T,Integer>();
	
	public BagOfObjects(){
		
	}
	
	public BagOfObjects(T s){
		Add(s);
	}
	
	public void Add(T s){
		++total;
		if(map.containsKey(s))
			map.put(s, map.get(s)+1);
		else
			map.put(s, 1);
	}
	
	public int Get(T s){
		if(map.containsKey(s))
			return map.get(s);
		return 0;
	}
	
	public T GetRandom(){
		
		int r = rand.nextInt(total);
		
		for(Map.Entry<T, Integer> e : map.entrySet()){
			r -= e.getValue();
			if(r < 0)
				return e.getKey();
		}
		
		return null;
	}
	
}
