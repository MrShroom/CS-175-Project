package textgen.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import MySQLToBagOfWords.BagOfWordUtilites;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.util.Pair;
import textgen.MemorySafeUtil;
import textgen.ParserUtil;
import textgen.Wordtab;
import textgen.datastructs.BagOfObjects;
import textgen.datastructs.MarkovChainKT;

public class ParserTest8 {
	
	
	public static HashMap<String,BagOfObjects<Integer>> vocab = new HashMap<String,BagOfObjects<Integer>>();
	public static HashMap<String,MarkovChainKT<List<Integer>,Integer>> augmap = new HashMap<String,MarkovChainKT<List<Integer>,Integer>>();
	public static BagOfObjects<String> sentenceStructs = new BagOfObjects<String>();
	public static HashMap<String,BagOfObjects<Integer>> startWords = new HashMap<String,BagOfObjects<Integer>>();
	public static final int NSIZE = 3;
	
	
	public static void main(String[] args){
		Set<String> categories = new HashSet<String>();
		categories.add("Restaurants");
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(1);
		Set<String> listOfReview = BagOfWordUtilites. getSetOfReviews(categories, stars, 1000);
		
		
		System.out.println("Size: " + listOfReview.size());
		for(String review : listOfReview)
		{
			List<String> sentences = ParserUtil.getSentenceStructStrings(review);
			for(String str : sentences)
				sentenceStructs.Add(str);
			List<TaggedWord> twod = ParserUtil.getFlatTaggedWordListString(review);
			
			
			MemorySafeUtil.trainVocab(vocab, twod);
			for(int i = 1; i <= NSIZE; ++i){
				MemorySafeUtil.trainPosMarkovChains(augmap, twod, i);
			}
		}
		
		
		List<String> words = new ArrayList<String>();
		for(int i = 0; i < 5; ++i){
			words.addAll(generateSentence());
		}
		
		System.out.print(words.get(0));
		for(int i = 1; i < words.size(); ++i){
			//char c = words.get(i).charAt(0);
			//if(".?,/".star)
			if(!Pattern.compile("[\\.\\?,!']").matcher(words.get(i)).find())
				System.out.print(" ");
			System.out.print(words.get(i));
		}
	}
	
	
	public static List<String> generateSentence(){
		List<String> posTags;
		do{
			posTags = ParserUtil.sentenceToList(sentenceStructs.GetRandom());
		}while(posTags.size()<6 || posTags.size() > 15);
		
		List<Integer> best = new ArrayList<Integer>();
		int highscore = 0;
		for(int i = 0; i < 400; ++i){
			int score = 0;
			List<Integer> temp = new ArrayList<Integer>();
			for(int j = 0; j < posTags.size(); ++j){
				String posTarg = posTags.get(j);
				Pair<Integer,Integer> pair = calculateNextWord3(temp,posTarg);
				score += pair.second();
				temp.add(pair.first());
			}
			if(score > highscore){
				highscore = score;
				best = temp;
			}
		}
		System.out.println("Avg Word Score: " + (float)highscore/(float)best.size());
		List<String> sent = new ArrayList<String>();
		for(int id : best)
			sent.add(Wordtab.itoa(id));
		return sent;
	}
	
	//Pair<id,score>
	public static Pair<Integer,Integer> calculateNextWord3(List<Integer> words, String posTarg){
		Pair<Integer,Integer> pair = new Pair<Integer,Integer>(1,1);
		for(int i = Math.min(words.size(),NSIZE);i>0;--i){
			List<Integer> ngram = MemorySafeUtil.toNgram(words, i);
			if(augmap.get(posTarg).HasNext(ngram)){
				//System.out.println(i + "-gram");
				pair.setFirst(augmap.get(posTarg).GetNext(ngram));
				pair.setSecond((int)Math.pow(i+1,3));
				return pair;
			}
		}
		//System.out.println("No Ngram");
		pair.setFirst(vocab.get(posTarg).GetRandom());
		return pair;
	}
}
