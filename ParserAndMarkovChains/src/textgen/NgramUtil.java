package textgen;

import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

public class NgramUtil {
	
	public static String toNgram(List<String> words,int n, int pos){
		String ngram = words.get(pos-n+1);
		for(int i = pos-n+2; i <= pos; ++i)
			ngram += " " + words.get(i);
		return ngram;
	}
	
	public static String toNgram(List<String> words,int n){
		return toNgram(words,n,words.size()-1);
	}
	
	
	
	
	//Generate map index for n grams
	public static String toNgramTags(List<TaggedWord> words,int n, int pos){
		String ngram = words.get(pos-n+1).tag();
		for(int i = pos-n+2; i <= pos; ++i){
			ngram += " " + words.get(i).tag();
		}
		return ngram;
	}
	
	public static String toNgramTags(List<TaggedWord> words, int n){
		return toNgramTags(words,n,words.size()-1);
	}
	
	public static String toNgramWords(List<TaggedWord> words,int n, int pos){
		String ngram = words.get(pos-n+1).word();
		for(int i = pos-n+2; i <= pos; ++i){
			ngram += " " + words.get(i).word();
		}
		return ngram;
	}
	
	public static String toNgramWords(List<TaggedWord> words,int n){
		return toNgramWords(words,n,words.size()-1);
	}
	
	public static MarkovChain genPosMarkovNgram(List<TaggedWord> list, int n){
		MarkovChain chain = new MarkovChain();
		for(int i = n-1; i < list.size()-1;++i){
			String ngram = toNgramTags(list,n,i);
			chain.Add(ngram, list.get(i+1).tag());
		}
		return chain;
	}
	
	public static void teachPosMarkovNgram(MarkovChain chain,List<TaggedWord> list, int n){
		for(int i = n-1; i < list.size()-1;++i){
			String ngram = toNgramTags(list,n,i);
			chain.Add(ngram, list.get(i+1).tag());
		}
	}
	
	
	public static HashMap<String,MarkovChain> genMarkovWordGramMap(List<TaggedWord> list,int n){
		HashMap<String,MarkovChain> augmap = new HashMap<String,MarkovChain>();
		for(int i = n-1; i < list.size()-1; ++i){
			String ngram = toNgramWords(list,n,i);
			TaggedWord next = list.get(i+1);
			if(!augmap.containsKey(next.tag()))
				augmap.put(next.tag(),new MarkovChain());
			augmap.get(next.tag()).Add(ngram, next.word());
		}
		return augmap;
	}
	
	public static void teachMarkovWordGramMap(HashMap<String,MarkovChain> augmap, List<TaggedWord> list,int n){
		for(int i = n-1; i < list.size()-1; ++i){
			String ngram = toNgramWords(list,n,i);
			TaggedWord next = list.get(i+1);
			if(!augmap.containsKey(next.tag()))
				augmap.put(next.tag(),new MarkovChain());
			augmap.get(next.tag()).Add(ngram, next.word());
		}
	}
	
	
}
