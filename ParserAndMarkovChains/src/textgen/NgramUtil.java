package textgen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;
import textgen.datastructs.BagOfObjects;
import textgen.datastructs.MarkovChain;

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
	
	
	public static MarkovChain<String> genStringMarkovNgram(List<String> list, int n){
		MarkovChain<String> chain = new MarkovChain<String>();
		for(int i = n-1; i < list.size()-1;++i){
			String ngram = toNgram(list,n,i);
			chain.Add(ngram, list.get(i+1));
		}
		return chain;
	}
	
	public static void teachStringMarkovNgram(MarkovChain<String> chain, List<String> list, int n){
		for(int i = n-1; i < list.size()-1;++i){
			String ngram = toNgram(list,n,i);
			chain.Add(ngram, list.get(i+1));
		}	
	}
	
	public static MarkovChain<String> genMarkovWordsFromTW(List<TaggedWord> list, int n){
		MarkovChain<String> chain = new MarkovChain<String>();
		for(int i = n-1; i < list.size()-1;++i){
			String ngram = toNgramWords(list,n,i);
			chain.Add(ngram, list.get(i+1).tag());
		}
		return chain;
	}
	
	public static void teachMarkovWordsFromTW(MarkovChain<String> chain, List<TaggedWord> list, int n){
		for(int i = n-1; i < list.size()-1;++i){
			String ngram = toNgramWords(list,n,i);
			chain.Add(ngram, list.get(i+1).tag());
		}
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
	
	/**
	 * Generate a document frequency bow from corpus.
	 * Tokenizes based on given regex.
	 * 
	 * @param reviews corpus
	 * @param tokSplit regex call to split.
	 * @return bag of words of corpus
	 */
	public static BagOfObjects<String> dfBOWfromSplit(Iterable<String> reviews, String tokSplit){
		BagOfObjects<String> bow = new BagOfObjects<String>();
		
		for(String review : reviews){
			HashSet<String> seen = new HashSet<String>();
			for(String s : review.toLowerCase().split(tokSplit))
				if(s.length() > 0 && !seen.contains(s)){
					bow.Add(s);
					seen.add(s);
				}
		}
			
		return bow;
	}
	
	public static void teachTokenCountBOW(BagOfObjects<String> bow, List<String> list, int n){
		for(int i = n-1; i < list.size(); ++i)
			bow.Add(toNgram(list,n,i));
	}
	
	
	public static void teachTokenCountBOW_words(BagOfObjects<String> bow, List<TaggedWord> list, int n){
		for(int i = n-1; i < list.size(); ++i)
			bow.Add(toNgramWords(list,n,i));
	}
	
	public static void teachMarkovWordsTaggedW(MarkovChain<String> chain,List<TaggedWord> list,int n){
		for(int i = n-1; i < list.size()-1; ++i){
			String ngram = toNgramWords(list,n,i);
			chain.Add(ngram, list.get(i+1).word());
		}
	}
	
	public static void teachHashBowVocab(HashMap<String,BagOfObjects<String>> vocab,List<TaggedWord> list){
		for(TaggedWord tw : list){
			if(!vocab.containsKey(tw.tag()))
				vocab.put(tw.tag(), new BagOfObjects<String>());
			vocab.get(tw.tag()).Add(tw.word());
		}
	}

}
