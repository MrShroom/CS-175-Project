package textgen.datastructs;

import java.util.HashMap;

//Wrapper for hashmap of current words to probability of next words using bagofobjects class.
public class MarkovChainKT<K,T> {
	public HashMap<K,BagOfObjects<T>> map = new HashMap<K,BagOfObjects<T>>();
	
	public MarkovChainKT(){
		
	}
	
	//Show markov chain an example of what comes next after a particular state.
	public void Add(K current, T next){
		if(map.containsKey(current)){
			map.get(current).Add(next);
		}else{
			map.put(current, new BagOfObjects<T>(next));
		}
	}
	
	//Randomly generate a new word based on current word.
	@SuppressWarnings("unchecked")
	public T GetNext(K current){
		if(map.containsKey(current))
			return map.get(current).GetRandom();
		if(current instanceof String)
			return (T)"<Error: NoNextState>";
		return null;
	}
	
	//Check if markov chain contains any examples of current ngram.
	//May be false if using a high-n ngram chain.
	public boolean HasNext(K current){
		return map.containsKey(current);
	}
	
}
