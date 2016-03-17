package textgen;

import java.util.HashMap;

/**
 * Module that maps words to integer ids, and vice versa
 *
 */
public class Wordtab {
	
	//Next available id.
	private static int idctr = 0;
	
	//Map of words to ids
	private static HashMap<String,Integer> atoiMap = new HashMap<String,Integer>();
	
	//Map of ids to words
	private static HashMap<Integer,String> itoaMap = new HashMap<Integer,String>();
	
	/**
	 * Converts word to its id
	 * 
	 * @param a
	 * word string
	 * 
	 * @return
	 * id of word
	 */
	public static int atoi(String a){
		if(!wordRegistered(a))
			registerWord(a);
		return atoiMap.get(a);
	}
	
	//Converts id to its word
	/**
	 * Converts id to its word
	 * 
	 * @param id
	 * id of word
	 * 
	 * @return
	 * word string
	 */
	public static String itoa(int id){
		return(itoaMap.get(id));
	}
	
	/**
	 * Queries if word has id
	 * 
	 * @param word
	 * @return
	 */
	private static boolean wordRegistered(String word){
		return atoiMap.containsKey(word);
	}
	
	/**
	 * Associates a word with a new id
	 * 
	 * @param word
	 */
	private static void registerWord(String word){
		int id = idctr++;
		atoiMap.put(word.intern(), id);
		itoaMap.put(id, word.intern());
	}
	
	
}
