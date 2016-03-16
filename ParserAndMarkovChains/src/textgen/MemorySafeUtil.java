package textgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;
import textgen.datastructs.BagOfObjects;
import textgen.datastructs.MarkovChainPOS;
import textgen.depreciatedcode.MarkovChainKT;

/**
 * Module of functions for converting sentences into lists of term-ids,
 * and training MarkovChains to remember successors of ngram/target-pos pairs
 * Is an updated version of ngramUtil which uses term-ids to save memory
 *
 *
 */
public class MemorySafeUtil {

	/**
	 * Generates ngram from list of word-ids
	 * 
	 * @param list
	 * List of term-ids
	 * A sequence will be taken from this list to generate an ngram
	 * 
	 * @param ngramsize
	 * size of ngram sequence to extract.
	 * 
	 * @param position
	 * end position of ngram to be generated.
	 * 
	 * @return
	 * List of term-ids, representing an ngram sequence.
	 */
	public static List<Integer> toNgram(List<Integer> list, int ngramsize, int position){
		List<Integer> ngram = new ArrayList<Integer>();
		for(int i = position-ngramsize+1; i < position+1;++i){
			ngram.add(list.get(i));
		}
		return ngram;
	}
	
	/**
	 * Generates ngram from list of stanford pos-tagger TaggedWords
	 * 
	 * @param list
	 * List of tagged words
	 * A sequence will be taken from this list to generate an ngram
	 * 
	 * @param ngramsize
	 * size of ngram sequence to extract.
	 * 
	 * @param position
	 * end position of ngram to be generated.
	 * 
	 * @return
	 * List of term-ids, representing an ngram sequence.
	 */
	public static List<Integer> toNgramTwod(List<TaggedWord> list, int ngramsize, int position){
		List<Integer> ngram = new ArrayList<Integer>();
		for(int i = position-ngramsize+1; i < position+1;++i){
			ngram.add(Wordtab.atoi(list.get(i).word()));
		}
		return ngram;
	}
	
	
	/**
	 * Generates ngram from list of word-ids.
	 * ngram by default is sampled from the end of the list.
	 * 
	 * @param list
	 * List of term-ids
	 * A sequence will be taken from this list to generate an ngram
	 * 
	 * @param ngramsize
	 * size of ngram sequence to extract.
	 * 
	 * @return
	 * List of term-ids, representing an ngram sequence.
	 */
	public static List<Integer> toNgram(List<Integer> list, int ngramsize){
		return toNgram(list,ngramsize,list.size()-1);
	}
	
	
	/**
	 * @depreciated
	 * version of trainPosMarkovChains2 that uses a map instead of specialized Markov Chain class.
	 */
	public static void trainPosMarkovChains(HashMap<String,MarkovChainKT<List<Integer>,Integer>> augmap, List<TaggedWord> words, int n){
		for(int i = n-1; i < words.size()-1;++i){
			String targPos = words.get(i+1).tag();
			if(!augmap.containsKey(targPos))
				augmap.put(targPos, new MarkovChainKT<List<Integer>,Integer>());
			augmap.get(targPos).Add(toNgramTwod(words,n,i), Wordtab.atoi(words.get(i+1).word()));
		}
	}
	
	
	/**
	 * Trains markov chain to memorize the successors of all ngrams in given list
	 * 
	 * 
	 * @param chain
	 * Markov Chain which memorizes successor-cound lists mapped to ngram/target-POS pairs.
	 * 
	 * @param words
	 * List of part of speech tagged words.
	 * 
	 * @param n
	 * Size of ngrams to memorize.
	 */
	public static void trainPosMarkovChains2(MarkovChainPOS<List<Integer>,Integer,Integer> chain, List<TaggedWord> words, int n){
		for(int i = n-1; i < words.size()-1; ++i){
			chain.Add(toNgramTwod(words,n,i), Wordtab.atoi(words.get(i+1).tag()), Wordtab.atoi(words.get(i+1).word()));
		}
	}
	
	/**
	 * @deprecated
	 * use trainVocabInt instead
	 * 
	 * Counts all words in given corpus.
	 * 
	 * @param vocab
	 * Map of pos tags to Bag of words to contain counts of all words seen with that pos tag.
	 * 
	 * @param words
	 * Collection of words to count. Usually is a review.
	 */
	public static void trainVocab(HashMap<String,BagOfObjects<Integer>> vocab, List<TaggedWord> words){
		for(TaggedWord tw : words){
			if(!vocab.containsKey(tw.tag()))
				vocab.put(tw.tag(), new BagOfObjects<Integer>());
			vocab.get(tw.tag()).Add(Wordtab.atoi(tw.word()));
		}
	}
	
	/**
	 * Counts all words in given corpus.
	 * 
	 * @param vocab
	 * Map of pos tag ids to Bag of words to contain counts of all words seen with that pos tag.
	 * 
	 * @param words
	 * Collection of words to count. Usually is a review.
	 */
	public static void trainVocabInt(HashMap<Integer,BagOfObjects<Integer>> vocab, List<TaggedWord> words){
		for(TaggedWord tw : words){
			if(!vocab.containsKey(tw.tag()))
				vocab.put(Wordtab.atoi(tw.tag()), new BagOfObjects<Integer>());
			vocab.get(Wordtab.atoi(tw.tag())).Add(Wordtab.atoi(tw.word()));
		}
	}
	
	
	
	/**
	 * Trains markov chain.
	 * 
	 * Same as trainPosMarkovChains2, but does not use POS-tags.
	 * Used for pos-tagless generators.
	 *
	 * @see trainPosMarkovChains2
	 */
	public static void trainNoPosChain(MarkovChainKT<List<Integer>,Integer> chain, List<TaggedWord> words, int n){
		for(int i = n-1; i < words.size()-1;++i){
			chain.Add(toNgramTwod(words, n, i),Wordtab.atoi(words.get(i+1).word()));
		}
	}
	
	
	/**
	 * Counts all words in given corpus.
	 * 
	 * Same as trainVocabInt, but does not use POS-tags.
	 * Used for pos-tagless generators.
	 * 
	 * @see trainVocabInt
	 */
	public static void trainNoPosVocab(BagOfObjects<Integer> bow, List<TaggedWord> words){
		for(TaggedWord tw : words)
			bow.Add(Wordtab.atoi(tw.word()));
	}
	

}
