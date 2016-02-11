package textgen;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ParserTest2 {
	
	public static List<HashMap<String,BagOfObjects<String>>> wmaps = new ArrayList<HashMap<String,BagOfObjects<String>>>();
	public static List<String> tokens = new ArrayList<String>();
	
	
	public static HashMap<String,BagOfObjects<String>> genGramMap(List<String> tokens, int n){
		HashMap<String,BagOfObjects<String>> map = new HashMap<String,BagOfObjects<String>>();
		for(int i = n-1; i < tokens.size()-1;++i){
			String ngram = Ngram(tokens,n,i);
			if(map.containsKey(ngram))
				map.get(ngram).Add(tokens.get(i+1));
			else
				map.put(ngram, new BagOfObjects<String>(tokens.get(i+1)));
		}
		return map;
	}
	
	public static String TwoGram(String a, String b){
		return a + " " + b;
	}
	
	public static String Ngram(List<String> words,int n, int pos){
		String ngram = words.get(pos-n+1);
		for(int i = pos-n+2; i <= pos; ++i)
			ngram += " " + words.get(i);
		return ngram;
	}
	
	public static String Ngram(List<String> words, int n){
		return Ngram(words,n,words.size()-1);
	}
	
	public static void main(String [] args){
		try {
			Scanner sc = new Scanner(new File("res/hemingway"));
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				line.replaceAll("\\.", " \\. ");
				line.replaceAll(",", " , ");
				for(String s : line.toLowerCase().split("[^a-zA-Z0-9\\.,']+")){
					if(s.length() <= 0)
						continue;
					tokens.add(s);
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
		for(int i = 0;i < 4; ++i){
			wmaps.add(genGramMap(tokens,i+1));
		}
		
		PrintWriter pw = null;
		try {
			pw = new PrintWriter("res/output.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> text = new ArrayList<String>();
		text.add(wmaps.get(0).get("of").GetRandom());
		for(int i = 0; i < 200; ++i){
			for(int j = wmaps.size()-1; j >= 0; --j){
				if(text.size() < j+1)
					continue;
				String gram = Ngram(text,j+1);
				if(wmaps.get(j).containsKey(gram)){
					text.add(wmaps.get(j).get(gram).GetRandom());
					pw.println(j);
					break;
				}
			}
		}
		
		System.out.print(text.get(0));
		for(int i = 1; i < text.size(); ++i){
			System.out.print(" "+text.get(i));
		}
		
		pw.close();
	}
	
	
}
