package textgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

public class MemorySafeUtil {

	public static List<Integer> toNgram(List<Integer> list, int ngramsize, int position){
		List<Integer> ngram = new ArrayList<Integer>();
		for(int i = position-ngramsize+1; i < position+1;++i){
			ngram.add(list.get(i));
		}
		return ngram;
	}
	
	public static List<Integer> toNgramTwod(List<TaggedWord> list, int ngramsize, int position){
		List<Integer> ngram = new ArrayList<Integer>();
		for(int i = position-ngramsize+1; i < position+1;++i){
			ngram.add(Wordtab.atoi(list.get(i).word()));
		}
		return ngram;
	}
	
	public static List<Integer> toNgram(List<Integer> list, int ngramsize){
		return toNgram(list,ngramsize,list.size()-1);
	}
	
	
	public static void trainPosMarkovChains(HashMap<String,MarkovChainKT<List<Integer>,Integer>> augmap, List<TaggedWord> words, int n){
		for(int i = n-1; i < words.size()-1;++i){
			String targPos = words.get(i+1).tag();
			if(!augmap.containsKey(targPos))
				augmap.put(targPos, new MarkovChainKT<List<Integer>,Integer>());
			augmap.get(targPos).Add(toNgramTwod(words,n,i), Wordtab.atoi(words.get(i+1).word()));
		}
	}
	
	public static void trainPosMarkovChains2(MarkovChainPOS<List<Integer>,Integer,Integer> chain, List<TaggedWord> words, int n){
		for(int i = n-1; i < words.size()-1; ++i){
			chain.Add(toNgramTwod(words,n,i), Wordtab.atoi(words.get(i+1).tag()), Wordtab.atoi(words.get(i+1).word()));
		}
	}
	
	public static void trainVocab(HashMap<String,BagOfObjects<Integer>> vocab, List<TaggedWord> words){
		for(TaggedWord tw : words){
			if(!vocab.containsKey(tw.tag()))
				vocab.put(tw.tag(), new BagOfObjects<Integer>());
			vocab.get(tw.tag()).Add(Wordtab.atoi(tw.word()));
		}
	}
	
	public static void trainVocabInt(HashMap<Integer,BagOfObjects<Integer>> vocab, List<TaggedWord> words){
		for(TaggedWord tw : words){
			if(!vocab.containsKey(tw.tag()))
				vocab.put(Wordtab.atoi(tw.tag()), new BagOfObjects<Integer>());
			vocab.get(Wordtab.atoi(tw.tag())).Add(Wordtab.atoi(tw.word()));
		}
	}
	
	//MarkovChain<Integer>

}
