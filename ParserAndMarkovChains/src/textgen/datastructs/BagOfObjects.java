package textgen.datastructs;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Bag of words style counter of terms.
 * 
 * @param <T>
 * Object being counted. Usually an integer term-id
 */
public class BagOfObjects<T> {
	
	static Random rand = new Random();
	
	//size counter
	int total = 0;
	
	//Map of terms to count
	TreeMap<T,Integer> map = new TreeMap<T,Integer>();
	
	public BagOfObjects(){
		
	}
	
	
	public BagOfObjects(T s){
		Add(s);
	}
	
	
	
	/**
	 * Increments count of term
	 * 
	 * @param s
	 * Term being incremented
	 */
	public void Add(T s){
		++total;
		if(map.containsKey(s))
			map.put(s, map.get(s)+1);
		else
			map.put(s, 1);
	}
	
	/**
	 * Get count of term
	 * 
	 * @param s
	 * Term being counted
	 * 
	 * @return
	 */
	public int Get(T s){
		if(map.containsKey(s))
			return map.get(s);
		return 0;
	}
	
	/**
	 * Returns a term randomly.
	 * Odds of being returned are directly proportional to the term's count.
	 * 
	 * @return
	 */
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
