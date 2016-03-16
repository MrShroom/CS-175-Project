package textgen.depreciatedcode;

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
import textgen.MemorySafeUtil;
import textgen.ParserUtil;
import textgen.Wordtab;
import textgen.datastructs.BagOfObjects;
import textgen.datastructs.BagOfObjects_NoComp;
import textgen.datastructs.MarkovChainPOS;
import textgen.generators.Generator;

public class GeneratorPOS2 extends Generator {

	HashMap<Integer, BagOfObjects<Integer>> vocab = new HashMap<Integer, BagOfObjects<Integer>>();
	MarkovChainPOS<List<Integer>, Integer, Integer> chain = new MarkovChainPOS<List<Integer>, Integer, Integer>();
	BagOfObjects_NoComp<List<Integer>> sentenceStructs = new BagOfObjects_NoComp<List<Integer>>();
	HashMap<Integer, BagOfObjects<Integer>> startWords = new HashMap<Integer, BagOfObjects<Integer>>();
	BagOfObjects<Integer> reviewSizes = new BagOfObjects<Integer>();
	
	public Generator train() {

		ngramSize = 4;
		
		vocab = new HashMap<Integer, BagOfObjects<Integer>>();
		chain = new MarkovChainPOS<List<Integer>, Integer, Integer>();
		sentenceStructs = new BagOfObjects_NoComp<List<Integer>>();
		startWords = new HashMap<Integer, BagOfObjects<Integer>>();
		reviewSizes = new BagOfObjects<Integer>();
		
		
		Set<String> categories = new HashSet<String>();
		categories.add(category);
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(star);
		Set<String> listOfReview = BagOfWordUtilites.getSetOfReviews(categories, stars, startReview, reviewCount);

		System.out.println("Size: " + listOfReview.size());

		System.out.println("Training");
		int ctr = 0;
		int num = 0;
		for (String review : listOfReview) {
			if ((++ctr) % (listOfReview.size() / 100) == 0) {
				System.out.println(++num + "% ");
				System.out.println("MB Used: " + Runtime.getRuntime().totalMemory() / 1000000);
			}
			List<List<TaggedWord>> sentences = ParserUtil.getSentenceStructList(review);
			reviewSizes.Add(sentences.size());
			for (List<TaggedWord> sentence : sentences) {
				if (sentence.isEmpty())
					continue;

				int startpos = Wordtab.atoi(sentence.get(0).tag());
				if (!startWords.containsKey(startpos))
					startWords.put(startpos, new BagOfObjects<Integer>());
				startWords.get(startpos).Add(Wordtab.atoi(sentence.get(0).word()));

				List<Integer> list = new ArrayList<Integer>();
				for (TaggedWord tw : sentence)
					list.add(Wordtab.atoi(tw.tag()));
				sentenceStructs.Add(list);
			}
			List<TaggedWord> twod = ParserUtil.getFlatTaggedWordListString(review);

			MemorySafeUtil.trainVocabInt(vocab, twod);
			for (int i = 1; i <= ngramSize; ++i) {
				MemorySafeUtil.trainPosMarkovChains2(chain, twod, i);
			}
		}

		trained = true;
		return this;
	}
	
	
	public String generateReview(){
		if(!trained)
			return "Error: Generator is not trained.";
		
		StringBuilder builder = new StringBuilder();
		
		List<String> words = new ArrayList<String>();
		int numSent = reviewSizes.GetRandom();
		for(int i = 0; i < numSent; ++i){
			words.addAll(generateSentence());
		}
		
		//System.out.println();
		
		builder.append(words.get(0));
		for(int i = 1; i < words.size(); ++i){
			//char c = words.get(i).charAt(0);
			//if(".?,/".star)
			if(words.get(i).contains("`")||words.get(i).contains("\"")||words.get(i).contains("\'\'"))
				continue;
			if(words.get(i).equals("-LRB-")){
				builder.append(" (");
				continue;
			}
			if(words.get(i).equals("-RRB-")){
				builder.append(")");
				continue;
			}
			
			if(!Pattern.compile("[\\.\\?,!':;]").matcher(words.get(i)).find()&&!words.get(i-1).equals("$")&&!words.get(i-1).equals("-LRB-"))
				builder.append(" ");
			builder.append(words.get(i));
		}
		
		return builder.toString();
	}
	
	private static Random rand = new Random();
	
	
	/**
	 * Generates a sentence.
	 * 
	 * Chooses a
	 * 
	 * 
	 * @return
	 */
	private List<String> generateSentence(){
		
		//Choose a learned grammar to match.
		//Hard coded bounds on sentence size.
		//Sentences generated within these bounds look better from personal experimentation.
		List<Integer> posTags;
		do{
			posTags = sentenceStructs.GetRandom();
		}while(posTags.size()<6 || posTags.size() > 16);
		
		
		List<Integer> best = new ArrayList<Integer>();
		List<Integer> ngramSizes = new ArrayList<Integer>();
		
		int rmax = 0;
		for(int i = 1 ; i <= ngramSize; ++i)
			rmax += i;
		for(int i = 0; i < posTags.size(); ++i){
			int r = rand.nextInt(rmax);
			int choice = 0;
			for(; r >= 0; ++choice)
				r -= choice+1;
			//ngramSizes.add(rand.nextInt(ngramSize)+1);
			ngramSizes.add(choice);
		}
		System.out.println(ngramSizes);
		
		int bps = 0;
		for(int i = 0; i < ngramSizes.size();++i){
			int bp = (Math.min(i, ngramSizes.get(i))+1)*3;
			bps += bp;
		}
		
		System.out.println("Best Possible avg: " + (float)bps/(float)posTags.size());
		
		int highscore = 0;
		for(int i = 0; i < 400; ++i){
			int score = 0;
			List<Integer> temp = new ArrayList<Integer>();
			for(int j = 0; j < posTags.size(); ++j){
				int posTarg = posTags.get(j);
				Pair<Integer,Integer> pair = calculateNextWord(temp,ngramSizes.get(j),posTarg);
				score += pair.second();
				temp.add(pair.first());
			}
			if(score > highscore){
				highscore = score;
				best = temp;
			}
		}
		if(verbose){
			System.out.println("Avg Word Score: " + (float)highscore/(float)best.size());
		}
		List<String> sent = new ArrayList<String>();
		for(int id : best)
			sent.add(Wordtab.itoa(id));
		return sent;
	}
	
	//Pair<id,score>
	/**
	 * Generates a word based on most recent ngram, and target Part of Speech.
	 * Also provides a score for the word, based on how big of an ngram it matches.
	 * 
	 * @param words
	 * The list of current words in the sentence.
	 * 
	 * @param maxn 
	 * Largest ngram allowed
	 * A bound on how many words back the method can use.
	 * This is used to prevent overfitting.
	 * 
	 * @param posTarg
	 * Part of Speech Target
	 * The function must return a word that belongs to this function.
	 * 
	 * @return
	 * Returns a word-score pair. The score is (NgramSizeUsed+1)*3.
	 */
	private Pair<Integer,Integer> calculateNextWord(List<Integer> words, int maxn, int posTarg){
		Pair<Integer,Integer> pair = new Pair<Integer,Integer>(1,1);
		
		if(words.isEmpty()){
			pair.setFirst(startWords.get(posTarg).GetRandom());
			return pair;
		}
		
		
		ngramBoost = !ngramBoost;
		for(int i = Math.min(words.size(),maxn);i>0;--i){
			List<Integer> ngram = MemorySafeUtil.toNgram(words, i);
			if(chain.HasNext(posTarg,ngram)){
				//System.out.println(i + "-gram");
				pair.setFirst(chain.GetNext(posTarg,ngram));
				//pair.setSecond((int)Math.pow(i+1,3));
				pair.setSecond((i+1)*3);
				return pair;
			}
		}
		//System.out.println("No Ngram");
		pair.setFirst(vocab.get(posTarg).GetRandom());
		return pair;
	}

}


