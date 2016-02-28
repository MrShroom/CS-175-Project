package textgen;

import java.util.HashMap;

public class Wordtab {
	
	
	private static int idctr = 0;
	private static HashMap<String,Integer> atoiMap = new HashMap<String,Integer>();
	private static HashMap<Integer,String> itoaMap = new HashMap<Integer,String>();
	
	public static int atoi(String a){
		if(!wordRegistered(a))
			registerWord(a);
		return atoiMap.get(a);
	}
	
	public static String itoa(int i){
		return(itoaMap.get(i));
	}
	
	private static boolean wordRegistered(String word){
		return atoiMap.containsKey(word);
	}
	
	private static void registerWord(String word){
		int id = idctr++;
		atoiMap.put(word.intern(), id);
		itoaMap.put(id, word.intern());
	}
	
	
}
