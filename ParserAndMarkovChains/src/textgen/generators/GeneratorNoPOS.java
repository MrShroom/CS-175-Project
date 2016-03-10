package textgen.generators;

import java.util.ArrayList;
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
import textgen.datastructs.MarkovChainKT;

public class GeneratorNoPOS extends Generator {

	
	BagOfObjects<Integer> vocab;
	BagOfObjects<Integer> startWords;
	MarkovChainKT<List<Integer>,Integer> chain;
	
	
	@Override
	public Generator train() {
		vocab = new BagOfObjects<Integer>();
		startWords = new BagOfObjects<Integer>();
		chain = new MarkovChainKT<List<Integer>,Integer>();
		
		
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
			for (List<TaggedWord> sentence : sentences) {
				if (sentence.isEmpty())
					continue;

				int start = Wordtab.atoi(sentence.get(0).word());
				startWords.Add(start);
			}
			List<TaggedWord> twod = ParserUtil.getFlatTaggedWordListString(review);

			MemorySafeUtil.trainNoPosVocab(vocab, twod);
			for (int i = 1; i <= ngramSize; ++i) {
				MemorySafeUtil.trainNoPosChain(chain, twod, i);
			}
		}

		trained = true;
		return this;
	}

	@Override
	public String generateReview(){
		if(!trained)
			return "Error: Generator is not trained.";
		
		StringBuilder builder = new StringBuilder();
		
		List<String> words = new ArrayList<String>();
		int n = 3+rand.nextInt(5);
		for(int i = 0; i < n; ++i){
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
			
			if(!Pattern.compile("[\\.\\?,!':;]").matcher(words.get(i)).find()&&!words.get(i-1).equals("$"))
				builder.append(" ");
			builder.append(words.get(i));
		}
		
		return builder.toString();
	}
	
	
	private Random rand = new Random();
	
	private List<String> generateSentence(){
		int sentSize = 6+rand.nextInt(10);
		
		List<Integer> best = new ArrayList<Integer>();
		int highscore = 0;
		for(int i = 0; i < 400; ++i){
			int score = 0;
			List<Integer> temp = new ArrayList<Integer>();
			for(int j = 0; j < sentSize; ++j){
				Pair<Integer,Integer> pair = calculateNextWord(temp);
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
		List<String> sent = new ArrayList<String>();
		for(int id : best)
			sent.add(Wordtab.itoa(id));
		return sent;
	}
	
	//Pair<id,score>
	private Pair<Integer,Integer> calculateNextWord(List<Integer> words){
		Pair<Integer,Integer> pair = new Pair<Integer,Integer>(1,1);
		
		if(words.isEmpty()){
			pair.setFirst(startWords.GetRandom());
			return pair;
		}
		
		
		ngramBoost = !ngramBoost;
		for(int i = Math.min(words.size(),ngramSize-(ngramBoost?1:0));i>0;--i){
			List<Integer> ngram = MemorySafeUtil.toNgram(words, i);
			if(chain.HasNext(ngram)){
				//System.out.println(i + "-gram");
				pair.setFirst(chain.GetNext(ngram));
				//pair.setSecond((int)Math.pow(i+1,3));
				pair.setSecond((i+1)*3);
				return pair;
			}
		}
		//System.out.println("No Ngram");
		pair.setFirst(vocab.GetRandom());
		return pair;
	}
}
