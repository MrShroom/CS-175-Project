package textgen;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import MySQLToBagOfWords.BagOfWordUtilites;
import edu.stanford.nlp.ling.TaggedWord;

public class ParserTest4 {
	public static final int NSIZE = 3;
	
	public static void main(String[] args){
		
		
		
		MarkovChain<String> markov =new MarkovChain<String>();
		
		Set<String> categories = new HashSet<String>();
		categories.add("Restaurants");
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(1);
		Set<String> listOfReview = BagOfWordUtilites.getSetOfReviews(categories, stars, 1000);
		
		BagOfObjects<String> startBOW = new BagOfObjects<String>();
		
		for(String review : listOfReview)
		{
			List<String> tokens = new ArrayList<String>();
			for(String token : review.toLowerCase().split("[^a-zA-Z0-9\\.,;\\?!']")){
				if(token.length()>0)
					tokens.add(token);
			}
			startBOW.Add(tokens.get(0));
			for(int i = 1; i <= NSIZE; ++i)
				NgramUtil.teachStringMarkovNgram(markov, tokens, i);
		}
		
		List<String> words = new ArrayList<String>();
		words.add(startBOW.GetRandom());
		boolean flag = false;
		
		
		for(int i = 0; i < 600; ++i){
			flag = false;
			for(int j = NSIZE; j > 0; j--){
				if(j > words.size())
					continue;
				String ngram = NgramUtil.toNgram(words, j);
				if(markov.HasNext(ngram)){
					words.add(markov.getNext(ngram));
					flag = true;
					break;
				}
			}
			if(!flag)
				words.add(startBOW.GetRandom());
		}
		
		System.out.print(words.get(0));
		for(int i = 1; i < words.size(); ++i){
			System.out.print(" " + words.get(i));
		}
		
		
	}
}