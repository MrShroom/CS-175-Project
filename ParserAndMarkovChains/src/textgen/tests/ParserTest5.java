package textgen.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import MySQLToBagOfWords.BagOfWordUtilites;
import edu.stanford.nlp.ling.TaggedWord;
import textgen.NgramUtil;
import textgen.ParserUtil;
import textgen.datastructs.BagOfObjects;
import textgen.datastructs.MarkovChain;

public class ParserTest5 {
	
	public static final int NSIZE = 2;
	
	public static void main(String[] args){
		
		BagOfObjects<String> sentenceStructs = new BagOfObjects<String>();
		
//		MarkovChain<String> markov =new MarkovChain<String>();
		
		Set<String> categories = new HashSet<String>();
		categories.add("Restaurants");
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(1);
		Set<String> listOfReview = BagOfWordUtilites.getSetOfReviews(categories, stars, 3000);
		
		HashMap<String,BagOfObjects<String>> startBOWs = new HashMap<String,BagOfObjects<String>>();
		
		HashMap<String,MarkovChain<String>> posChain = new HashMap<String,MarkovChain<String>>();
		
		for(String review : listOfReview)
		{
			List<String> sentences = ParserUtil.getSentenceStructStrings(review);
			for(String str : sentences)
				if(str.length() > 14)
					sentenceStructs.Add(str);
			
			List<TaggedWord> words = ParserUtil.getFlatTaggedWordListString(review);
			for(int i = 1; i <= NSIZE; ++i)
				NgramUtil.teachMarkovWordGramMap(posChain, words, i);
			
			if(!startBOWs.containsKey(words.get(0).tag()))
				startBOWs.put(words.get(0).tag(), new BagOfObjects<String>());
			startBOWs.get(words.get(0).tag()).Add(words.get(0).word());
			
			//for(int i = 1; i <= NSIZE; ++i)
			//	NgramUtil.teachMarkovWordsFromTW(markov, words, i);
		}
		listOfReview = null;
		
		/*
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
			for(int i = 0; i <= NSIZE; ++i)
				NgramUtil.teachStringMarkovNgram(markov, tokens, i);
		}catch(Exception e){
			e.printStackTrace();
		}
		*/
		
		List<String> postags = new ArrayList<String>();
		for(int i = 0; i < 4; ++i){
			postags.addAll(ParserUtil.sentenceToList(sentenceStructs.GetRandom()));
		}
		
		boolean flag = false;
		List<String> words = new ArrayList<String>();
		words.add(startBOWs.get(postags.get(0)).GetRandom());
		for(int i = 0 ; i < postags.size()-1;++i){
			String pos = postags.get(i+1);
			flag = false;
			for(int j = NSIZE; j > 0; --j){
				if(j>words.size())
					continue;
				String ngram = NgramUtil.toNgram(words, j);
				if(posChain.get(pos).HasNext(ngram)){
					flag = true;
					words.add(posChain.get(pos).GetNext(ngram));
					break;
				}
			}
			if(!flag)
				words.add(".");
				//words.add(startBOWs.get(pos).GetRandom());
		}
		
		
		for(String str : words)
			System.out.print(str + " ");
	}
}
