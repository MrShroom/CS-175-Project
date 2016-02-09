package textgen;

import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

public class NgramUtil {
	
	
	//Converts a group of tokens from an array to a single ngram string, for map indexing
	public static String toNgram(List<String> words,int n, int pos){
		String ngram = words.get(pos-n+1);
		for(int i = pos-n+2; i <= pos; ++i)
			ngram += " " + words.get(i);
		return ngram;
	}
	
	//Shortcut using the end of the array to make the ngram
	public static String toNgram(List<String> words,int n){
		return toNgram(words,n,words.size()-1);
	}
	
	
	//Create ngram of pos-tags from TaggedWord array
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
	
	//Create ngram of actual words from TaggedWord array
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
	
	
	//Create a markov chain from scratch using list of taggedwords. N is the size of the n-gram that chain uses.
	public static MarkovChain<String> genPosMarkovNgram(List<TaggedWord> list, int n){
		MarkovChain<String> chain = new MarkovChain<String>();
		for(int i = n-1; i < list.size()-1;++i){
			String ngram = toNgramTags(list,n,i);
			chain.Add(ngram, list.get(i+1).tag());
		}
		return chain;
	}
	
	//Show an existing markov chain more examples from a list of tokens.
	public static void teachPosMarkovNgram(MarkovChain<String> chain,List<TaggedWord> list, int n){
		for(int i = n-1; i < list.size()-1;++i){
			String ngram = toNgramTags(list,n,i);
			chain.Add(ngram, list.get(i+1).tag());
		}
	}
	
	//Create a hashmap of parts-of-speech to markovchains for that pos. n is the size of the ngram used by that chain.
	public static HashMap<String,MarkovChain<String>> genMarkovWordGramMap(List<TaggedWord> list,int n){
		HashMap<String,MarkovChain<String>> augmap = new HashMap<String,MarkovChain<String>>();
		for(int i = n-1; i < list.size()-1; ++i){
			String ngram = toNgramWords(list,n,i);
			TaggedWord next = list.get(i+1);
			if(!augmap.containsKey(next.tag()))
				augmap.put(next.tag(),new MarkovChain<String>());
			augmap.get(next.tag()).Add(ngram, next.word());
		}
		return augmap;
	}
	
	//Show an existing hashmap of POS to MarkovChains examples of text.
	public static void teachMarkovWordGramMap(HashMap<String,MarkovChain<String>> augmap, List<TaggedWord> list,int n){
		for(int i = n-1; i < list.size()-1; ++i){
			String ngram = toNgramWords(list,n,i);
			TaggedWord next = list.get(i+1);
			if(!augmap.containsKey(next.tag()))
				augmap.put(next.tag(),new MarkovChain<String>());
			augmap.get(next.tag()).Add(ngram, next.word());
		}
	}
	
	
}
