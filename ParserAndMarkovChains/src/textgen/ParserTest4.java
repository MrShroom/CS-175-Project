package textgen;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import MySQLToBagOfWords.BagOfWordUtilites;

public class ParserTest4 {
	public static final int NSIZE = 4;
	
	
	public static MarkovChain<String> markov;
	public static BagOfObjects<String> tokenCountBOW;
	public static BagOfObjects<String> startBOW;
	
	public static Random rand = new Random();
	
	public static void main(String[] args){
		
		
		
		markov = new MarkovChain<String>();
		startBOW = new BagOfObjects<String>();
		tokenCountBOW = new BagOfObjects<String>();
		
		Set<String> categories = new HashSet<String>();
		categories.add("Burgers");
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(1);
		Set<String> listOfReview = BagOfWordUtilites.getSetOfReviews(categories, stars, 1000);
		
		
		
		for(String review : listOfReview)
		{
			List<String> tokens = new ArrayList<String>();
			for(String token : review.toLowerCase().split("[^a-zA-Z0-9\\.,;\\?!']")){
				if(token.length()>0)
					tokens.add(token);
			}
			startBOW.Add(tokens.get(0));
			for(int i = 1; i <= NSIZE; ++i){
				NgramUtil.teachStringMarkovNgram(markov, tokens, i);
				NgramUtil.teachTokenCountBOW(tokenCountBOW, tokens, i);
			}
		}
		listOfReview = null;
		
		try{
			List<String> tokens = new ArrayList<String>();
			Scanner sc = new Scanner(new File("res/hemingway"));
			while(sc.hasNextLine()){
				for(String token : sc.nextLine().toLowerCase().split("[^a-zA-Z0-9\\.,;\\?!']")){
					if(token.length()>0)
						tokens.add(token);
				}	
			}
			sc.close();
			for(int i = 1; i <= NSIZE; ++i){
				//NgramUtil.teachStringMarkovNgram(markov, tokens, i);
				//NgramUtil.teachTokenCountBOW(tokenCountBOW, tokens, i);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		List<String> words = new ArrayList<String>();
		words.add(startBOW.GetRandom());
//		boolean flag = false;
		
		
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
		
		for(int i = 0; i < 600; ++i){
			String w = calculateNextWord(words);
			words.add(w);
		}
		
		System.out.print(words.get(0));
		for(int i = 1; i < words.size(); ++i){
			System.out.print(" " + words.get(i));
		}
		
		
	}
	
	public static String calculateNextWord(List<String> words){
		List<String> ngrams = new ArrayList<String>();
		List<Double> probBreaks = new ArrayList<Double>();
		double probBreakSum = 0;
		
		System.out.println("Words size: " + words.size());
		for(int i = 1; i <= NSIZE && i <= words.size(); ++i){
			String ngram = NgramUtil.toNgram(words, i);
			int count = tokenCountBOW.Get(ngram);
			double value = Math.log(1+count)*i*i*i;
			probBreakSum += value;
			probBreaks.add(value);
			ngrams.add(ngram);
		}
		
		if(probBreakSum <= 0){
			System.out.println("No probBreakSum");
			return startBOW.GetRandom();
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
		if(markov.HasNext(ngram)){
			return markov.GetNext(ngram);
		}else{
			System.out.println("No markov entry");
			return startBOW.GetRandom();
		}
		//return markov.HasNext(ngram) ? markov.GetNext(ngram) : startBOW.GetRandom();
	}
}
