package textgen.datastructs;

import java.util.HashMap;

//Wrapper for hashmap of current words to probability of next words using bagofobjects class.
public class MarkovChainPOS<K,P,T> {
	public HashMap<K,HashMap<P,BagOfObjects<T>>> map = new HashMap<K,HashMap<P,BagOfObjects<T>>>();
	
	public MarkovChainPOS(){
		
	}
	
	//Show markov chain an example of what comes next after a particular state.
	public void Add(K current, P pos, T next){
		if(map.containsKey(current)){
			if(map.get(current).containsKey(pos))
				map.get(current).get(pos).Add(next);
			else
				map.get(current).put(pos, new BagOfObjects<T>(next));
		}else{
			HashMap<P,BagOfObjects<T>> newmap = new HashMap<P,BagOfObjects<T>>();
			newmap.put(pos, new BagOfObjects<T>(next));
			map.put(current, newmap);
		}
	}
	
	//Randomly generate a new word based on current word.
	@SuppressWarnings("unchecked")
	public T GetNext(P pos, K current){
		if(map.containsKey(current))
			return map.get(current).get(pos).GetRandom();
		if(current instanceof String)
			return (T)"<Error: NoNextState>";
		return null;
	}
	
	//Check if markov chain contains any examples of current ngram.
	//May be false if using a high-n ngram chain.
	public boolean HasNext(P pos, K current){
		return map.containsKey(current) && map.get(current).containsKey(pos);
	}
	
}
