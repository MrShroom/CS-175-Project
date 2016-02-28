package textgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import MySQLToBagOfWords.BagOfWordUtilites;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.util.Pair;

public class ParserTest9 {
	
	
	public static HashMap<Integer,BagOfObjects<Integer>> vocab = new HashMap<Integer,BagOfObjects<Integer>>();
	//public static HashMap<String,MarkovChainKT<List<Integer>,Integer>> augmap = new HashMap<String,MarkovChainKT<List<Integer>,Integer>>();
	public static MarkovChainPOS<List<Integer>,Integer,Integer> chain = new MarkovChainPOS<List<Integer>,Integer,Integer>();
	public static BagOfObjects_NoComp<List<Integer>> sentenceStructs = new BagOfObjects_NoComp<List<Integer>>();
	public static final int NSIZE = 2;
	
	public static HashMap<Integer,BagOfObjects<Integer>> startWords = new HashMap<Integer,BagOfObjects<Integer>>();
	
	
	public static void main(String[] args){
		Set<String> categories = new HashSet<String>();
		categories.add("Restaurants");
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(1);
		Set<String> listOfReview = BagOfWordUtilites.getSetOfReviews(categories, stars, 1000);
		
		
		for(String review : listOfReview)
		{
			List<List<TaggedWord>> sentences = ParserUtil.getSentenceStructList(review);
			for(List<TaggedWord> sentence : sentences){
				if(sentence.isEmpty())
					continue;
				
				int startpos = Wordtab.atoi(sentence.get(0).tag());
				if(!startWords.containsKey(startpos))
					startWords.put(startpos,new BagOfObjects<Integer>());
				startWords.get(startpos).Add(Wordtab.atoi(sentence.get(0).word()));
	
				List<Integer> list = new ArrayList<Integer>();
				for(TaggedWord tw : sentence)
					list.add(Wordtab.atoi(tw.tag()));
				sentenceStructs.Add(list);
			}
			List<TaggedWord> twod = ParserUtil.getFlatTaggedWordListString(review);
			
			
			MemorySafeUtil.trainVocabInt(vocab, twod);
			for(int i = 1; i <= NSIZE; ++i){
				MemorySafeUtil.trainPosMarkovChains2(chain, twod, i);
			}
		}
		
		
		List<String> words = new ArrayList<String>();
		for(int i = 0; i < 6; ++i){
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
		List<Integer> posTags;
		do{
			posTags = sentenceStructs.GetRandom();
		}while(posTags.size()<6 || posTags.size() > 16);
		
		List<Integer> best = new ArrayList<Integer>();
		int highscore = 0;
		for(int i = 0; i < 400; ++i){
			int score = 0;
			List<Integer> temp = new ArrayList<Integer>();
			for(int j = 0; j < posTags.size(); ++j){
				int posTarg = posTags.get(j);
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
	public static Pair<Integer,Integer> calculateNextWord3(List<Integer> words, int posTarg){
		Pair<Integer,Integer> pair = new Pair<Integer,Integer>(1,1);
		
		if(words.isEmpty()){
			pair.setFirst(startWords.get(posTarg).GetRandom());
			return pair;
		}
		
		for(int i = Math.min(words.size(),NSIZE);i>0;--i){
			List<Integer> ngram = MemorySafeUtil.toNgram(words, i);
			if(chain.HasNext(posTarg,ngram)){
				//System.out.println(i + "-gram");
				pair.setFirst(chain.GetNext(posTarg,ngram));
				pair.setSecond((int)Math.pow(i+1,3));
				return pair;
			}
		}
		//System.out.println("No Ngram");
		pair.setFirst(vocab.get(posTarg).GetRandom());
		return pair;
	}
}