package textgen.datastructs;

import java.util.HashMap;

/**
 * Map of ngram/POS-target pairs to successor counts.
 * 
 * @param <K>
 * Represents the current state type. Is usually a list of integer ids.
 * 
 * @param <P>
 * Represents the part-of-speech tag target. Is usually an integer id.
 * 
 * @param <T>
 * Represents the next state type. Is usually an integer id.
 * 
 */
public class MarkovChainPOS<K,P,T> {
	
	//Map of ngrams/POS-tags to count of successors
	public HashMap<K,HashMap<P,BagOfObjects<T>>> map = new HashMap<K,HashMap<P,BagOfObjects<T>>>();
	
	public MarkovChainPOS(){
		
	}
	
	
	/**
	 * Incrememnts count for successor word to ngram.
	 * Gives the part of speech of the successor word as well.
	 * 
	 * @param current
	 * ngram successor is getting mapped to
	 * 
	 * @param pos
	 * Part of Speech tag given to the successor.
	 * 
	 * @param next
	 * The successor word to the ngram
	 */
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
	
	/**
	 * Randomly generate a new word based on current ngram, and desired part of speech.
	 * 
	 * @param pos
	 * Part of speech being targeted.
	 * 
	 * @param current
	 * ngram successor is being generated for.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T GetNext(P pos, K current){
		if(map.containsKey(current))
			return map.get(current).get(pos).GetRandom();
		if(current instanceof String)
			return (T)"<Error: NoNextState>";
		return null;
	}
	
	/**
	 * Check if markov chain has seen a given ngram, and has seen a successor with the given tag.
	 * 
	 * @param pos
	 * Part of speech being targeted.
	 * 
	 * @param current
	 * ngram chain is checking.
	 * 
	 * @return
	 */
	public boolean HasNext(P pos, K current){
		return map.containsKey(current) && map.get(current).containsKey(pos);
	}
	
}
