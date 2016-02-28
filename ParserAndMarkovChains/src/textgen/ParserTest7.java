package textgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import MySQLToBagOfWords.BagOfWordUtilites;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.util.Pair;

public class ParserTest7 {
	public static final int NSIZE = 3;
	
	
	public static MarkovChain<String> markov;
	public static BagOfObjects<String> tokenCountBOW;
	public static HashMap<String,BagOfObjects<String>> startBOWs = new HashMap<String,BagOfObjects<String>>();
	public static BagOfObjects<String> sentenceStructs = new BagOfObjects<String>();
	public static HashMap<String,MarkovChain<String>> augmap = new HashMap<String,MarkovChain<String>>();
	public static HashMap<String,BagOfObjects<String>> vocab = new HashMap<String,BagOfObjects<String>>();
			
	public static Random rand = new Random();
	
	public static void main(String[] args){
		
		
		
		markov = new MarkovChain<String>();
		//startBOW = new BagOfObjects<String>();
		tokenCountBOW = new BagOfObjects<String>();
		
		Set<String> categories = new HashSet<String>();
		categories.add("Restaurants");
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(1);
		Set<String> listOfReview = BagOfWordUtilites.getSetOfReviews(categories, stars, 1000);
		
		
		
		for(String review : listOfReview)
		{
			List<String> sentences = ParserUtil.getSentenceStructStrings(review);
			for(String str : sentences)
				//if(str.length() > 30)
					sentenceStructs.Add(str);
			
			List<List<TaggedWord>> tsents = ParserUtil.getTaggedWordListString(review);
			
			for(List<TaggedWord> sent : tsents){
				TaggedWord tw = sent.get(0);
				if(!startBOWs.containsKey(tw.tag()))
					startBOWs.put(tw.tag(), new BagOfObjects<String>());
				startBOWs.get(tw.tag()).Add(tw.word());
			}
			
			
			List<TaggedWord> twod = ParserUtil.getFlatTaggedWordListString(review);
			
			NgramUtil.teachHashBowVocab(vocab, twod);
			
			/*
			for(int i = 0 ;i < twod.size()-1; ++i){
				if(i == 0 || twod.get(i).word().contains("[\\.\\?!]")){
					TaggedWord tw = twod.get(i+1);
					if(!startBOWs.containsKey(tw.tag()))
						startBOWs.put(tw.tag(), new BagOfObjects<String>());
					startBOWs.get(tw.tag()).Add(tw.word());
				}
			}
			*/
			
			
			for(int i = 1; i <= NSIZE; ++i){
				NgramUtil.teachTokenCountBOW_words(tokenCountBOW, twod, i);
				NgramUtil.teachMarkovWordGramMap(augmap, twod, i);
				NgramUtil.teachMarkovWordsTaggedW(markov,twod,i);
				//NgramUtil.teachStringMarkovNgram(markov, tokens, i);
				//NgramUtil.teachTokenCountBOW(tokenCountBOW, tokens, i);
			}
		}
		listOfReview = null;
		
		
		
//		for(int i = 0; i < 600; ++i){
//			flag = false;
//			for(int j = NSIZE; j > 0; j--){
//				if(j > words.size())
//					continue;
//				String ngram = NgramUtil.toNgram(words, j);
//				if(markov.HasNext(ngram)){
//					words.add(markov.GetNext(ngram));
//					flag = true;
//					break;
//				}
//			}
//			if(!flag)
//				words.add(startBOW.GetRandom());
//		}
		
		List<String> words = new ArrayList<String>();
		
		List<String> postags = new ArrayList<String>();
		for(int i = 0; i < 4; ++i){
			postags.addAll(ParserUtil.sentenceToList(sentenceStructs.GetRandom()));
		}
		
		
		for(int i = 0; i < postags.size(); ++i){
			String nextTag = postags.get(i);
			words.add(calculateNextWord2(words,nextTag));
		}
		
		
		words.clear();
		for(int i = 0; i < 4; ++i){
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
		}while(posTags.size()<6 || posTags.size() > 10);
		
		List<String> best = new ArrayList<String>();
		int highscore = 0;
		for(int i = 0; i < 400; ++i){
			int score = 0;
			List<String> temp = new ArrayList<String>();
			for(int j = 0; j < posTags.size(); ++j){
				String posTarg = posTags.get(j);
				Pair<String,Integer> pair = calculateNextWord3(temp,posTarg);
				score += pair.second();
				temp.add(pair.first());
			}
			if(score > highscore){
				highscore = score;
				best = temp;
			}
		}
		System.out.println("Avg Word Score: " + (float)highscore/(float)best.size());
		return best;
	}
	
	public static String calculateNextWord2(List<String> words, String posTarg){
		for(int i = Math.min(words.size(),NSIZE);i>0;--i){
			String ngram = NgramUtil.toNgram(words, i);
			if(augmap.get(posTarg).HasNext(ngram)){
				System.out.println(i + "-gram");
				return augmap.get(posTarg).GetNext(ngram);
			}
		}
		System.out.println("No Ngram");
		return vocab.get(posTarg).GetRandom();
	}
	
	public static Pair<String,Integer> calculateNextWord3(List<String> words, String posTarg){
		Pair<String,Integer> pair = new Pair<String,Integer>("",1);
		for(int i = Math.min(words.size(),NSIZE);i>0;--i){
			String ngram = NgramUtil.toNgram(words, i);
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
	
	public static String calculateNextWord(List<String> words, String posTarg){
		if(posTarg.equals(".")){
			System.out.println("Force Period");
			return ".";
		}
		
		List<String> ngrams = new ArrayList<String>();
		List<Double> probBreaks = new ArrayList<Double>();
		double probBreakSum = 0;
		
		System.out.println("Words size: " + words.size());
		for(int i = 1; i <= NSIZE && i <= words.size(); ++i){
			String ngram = NgramUtil.toNgram(words, i);
			int count = tokenCountBOW.Get(ngram);
			double value = Math.log(1+count)*i*i*i;
			if(augmap.containsKey(posTarg) && augmap.get(posTarg).HasNext(ngram))
				value *= 6;
			
			probBreakSum += value;
			probBreaks.add(value);
			ngrams.add(ngram);
		}
		
		if(probBreakSum <= 0){
			System.out.println("No probBreakSum");
			return startBOWs.get(posTarg).GetRandom();
		}
			
		
		
		double r = rand.nextDouble()*probBreakSum;
		int choice = 0;
		while(r >= probBreaks.get(choice)){
			r -= probBreaks.get(choice);
			++choice;
		}
		
		String ngram = ngrams.get(choice);
		System.out.println("Choice: " + choice);
		System.out.println("Ngram: " + ngram);
		if(augmap.containsKey(posTarg) && augmap.get(posTarg).HasNext(ngram)){
			return augmap.get(posTarg).GetNext(ngram);
		}else if(markov.HasNext(ngram)){
			return markov.GetNext(ngram);
		}else{
			System.out.println("No markov entry");
			return startBOWs.get(posTarg).GetRandom();
		}
		//return markov.HasNext(ngram) ? markov.GetNext(ngram) : startBOW.GetRandom();
	}
}
