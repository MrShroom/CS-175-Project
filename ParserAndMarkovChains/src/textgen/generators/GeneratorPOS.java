package textgen.generators;

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

public class GeneratorPOS extends Generator {

	
	//Raw vocabulary. Maps part of speech to the counts of all words seen for that part of speech.
	HashMap<Integer, BagOfObjects<Integer>> vocab = new HashMap<Integer, BagOfObjects<Integer>>();
	
	//Markov Chain. For each seen ngram, remembers a list of successors mapped to part of speech targets.
	MarkovChainPOS<List<Integer>, Integer, Integer> chain = new MarkovChainPOS<List<Integer>, Integer, Integer>();
	
	//Contains counts of all seen sentence grammars.
	BagOfObjects_NoComp<List<Integer>> sentenceStructs = new BagOfObjects_NoComp<List<Integer>>();
	
	//Contains list of all words used to start sentences.
	HashMap<Integer, BagOfObjects<Integer>> startWords = new HashMap<Integer, BagOfObjects<Integer>>();
	
	
	/**
	 * Trains the generator on the database.
	 * 
	 * Resets the data structures if they've already been trained
	 * Loads Reviews based on parameters
	 * 
	 * Memorizes the structure of sentences.
	 * Memorizes raw vocabulary.
	 * Memorizes successors of all allowed ngrams.
	 * 
	 * @return
	 **/
	public Generator train() {

		
		//Reset data
		vocab = new HashMap<Integer, BagOfObjects<Integer>>();
		chain = new MarkovChainPOS<List<Integer>, Integer, Integer>();
		sentenceStructs = new BagOfObjects_NoComp<List<Integer>>();
		startWords = new HashMap<Integer, BagOfObjects<Integer>>();
		
		
		//Load reviews based on generator settings
		Set<String> categories = new HashSet<String>();
		categories.add(category);
		Set<Integer> stars = new HashSet<Integer>();
		stars.add(star);
		Set<String> listOfReview = BagOfWordUtilites.getSetOfReviews(categories, stars, startReview, reviewCount);

		System.out.println("Size: " + listOfReview.size());

		System.out.println("Training");
		int ctr = 0;
		int num = 0;
		//learn on each loaded review seperately
		for (String review : listOfReview) {
			if (verbose&&listOfReview.size() >= 100 && (++ctr) % (listOfReview.size() / 100) == 0) {
				System.out.println(++num + "% ");
				System.out.println("MB Used: " + Runtime.getRuntime().totalMemory() / 1000000);
			}
			
			//Memorize structures of sentences. Memorize which words start sentences.
			List<List<TaggedWord>> sentences = ParserUtil.getSentenceStructList(review);
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
			
			//Memorize vocabulary without ngram. Use vocab if ngram doesn't exist during generation.
			//Train markov chain to remember all allowed ngrams.
			List<TaggedWord> twod = ParserUtil.getFlatTaggedWordListString(review);
			MemorySafeUtil.trainVocabInt(vocab, twod);
			for (int i = 1; i <= ngramSize; ++i) {
				MemorySafeUtil.trainPosMarkovChains2(chain, twod, i);
			}
		}

		trained = true;
		return this;
	}
	
	public Random rand = new Random();
	
	
	/**
	 * Generates a new review.
	 * 
	 * Chooses random number of sentences for review to have,
	 * Then calls generateSentences() that many times.
	 * Lastly, takes the words generated, and formats the review into a single string.
	 * 
	 * @return
	 * Fully generated review string.
	 */
	public String generateReview(){
		if(!trained)
			return "Error: Generator is not trained.";
		
		//Use a stringbuilder to generate the full review string.
		StringBuilder builder = new StringBuilder();
		
		
		List<String> words = new ArrayList<String>();
	
		
		//We used to randomly select the number of sentences from randomly seen reviews.
		//However, there were outliers containing no words, as well as outliers containing
		//Many words. To fix this, we choose a random range we thought consistently looked nice.
		int n = 3+rand.nextInt(5);
		for(int i = 0; i < n; ++i){
			words.addAll(generateSentence());
		}
		
		
		
		//Format the review string so that it looks nice.
		builder.append(words.get(0));
		for(int i = 1; i < words.size(); ++i){
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
			
			if(!Pattern.compile("[\\.\\?,!':;]").matcher(words.get(i)).find()&&!words.get(i-1).equals("$")&&!words.get(i-1).equals("("))
				builder.append(" ");
			builder.append(words.get(i));
		}
		
		return builder.toString();
	}
	
	/**
	 * Generates a sentence.
	 * 
	 * Chooses a sentence grammar already seen at random
	 * Generates 400 sentence candidates, scored by how many ngrams they can match up.
	 * Returns highest scored sentence match.
	 * 
	 * 
	 * @return
	 * Array of words in generated sentence.
	 */
	private List<String> generateSentence(){
		
		//Choose a learned grammar to match.
		//Hard coded bounds on sentence size.
		//Sentences generated within these bounds look better from personal experimentation.
		List<Integer> posTags;
		do{
			posTags = sentenceStructs.GetRandom();
		}while(posTags.size()<6 || posTags.size() > 16);
		
		
		
		//Generate several hundred potential sentences.
		//Choose sentence that satisfied highest number of ngrams.
		List<Integer> best = new ArrayList<Integer>();
		int highscore = 0;
		for(int i = 0; i < 400; ++i){
			int score = 0;
			List<Integer> temp = new ArrayList<Integer>();
			for(int j = 0; j < posTags.size(); ++j){
				int posTarg = posTags.get(j);
				ngramBoost = !ngramBoost;
				Pair<Integer,Integer> pair = calculateNextWord(temp,ngramSize-(ngramBoost?1:0),posTarg);
				score += pair.second();
				temp.add(pair.first());
			}
			if(score > highscore){
				highscore = score;
				best = temp;
			}
		}
		if(verbose)
			System.out.println("Avg Word Score: " + (float)highscore/(float)best.size());
		
		//Replace integers in generated sentence with actual strings.
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
		
		
		//Get largest ngram allowed that contains an example of our ngram/posTarg pair.
		for(int i = Math.min(words.size(),maxn);i>0;--i){
			List<Integer> ngram = MemorySafeUtil.toNgram(words, i);
			if(chain.HasNext(posTarg,ngram)){
				pair.setFirst(chain.GetNext(posTarg,ngram));
				pair.setSecond((i+1)*(Wordtab.itoa(posTarg).contains(".") ?  6:3));
				return pair;
			}
		}
		//System.out.println("No Ngram");
		pair.setFirst(vocab.get(posTarg).GetRandom());
		return pair;
	}
	

}


