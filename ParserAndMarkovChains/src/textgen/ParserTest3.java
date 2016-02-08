package textgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.stanford.nlp.ling.TaggedWord;

public class ParserTest3 {

	public static void main(String [] args){
		
		List<TaggedWord> words = ParserUtil.getFlatTaggedWordListPath("res/hemingway");
		//System.out.println(words);
		List<MarkovChain> posChains = new ArrayList<MarkovChain>();
		for(int i = 1 ;i <= 3; ++i){
			posChains.add(NgramUtil.genPosMarkovNgram(words, i));
		}
		List<HashMap<String,MarkovChain>> wordChains = new ArrayList<HashMap<String,MarkovChain>>();
		for(int i = 1; i <= 3; ++i){
			wordChains.add(NgramUtil.genMarkovWordGramMap(words,i));
		}
		
		
		
		List<String> poslist = new ArrayList<String>();
		poslist.add("JJ");
		for(int i = 0; i < 600; ++i){
			for(int j = posChains.size()-1; j >= 0; --j){
				if(poslist.size()<j+1)
					continue;
				String ngram = NgramUtil.toNgram(poslist, j+1);
				if(posChains.get(j).HasNext(ngram)){
					System.out.println("PosGram Size: " + (j+1));
					poslist.add(posChains.get(j).getNext(ngram));
					break;
				}
			}
		}
		
		List<String> wlist = new ArrayList<String>();
		wlist.add("known");
		for(int i = 0; i < poslist.size()-1; ++i){
			String nextPOS = poslist.get(i+1);
			for(int j = wordChains.size()-1; j >= 0; --j){
				if(wlist.size()<j+1)
					continue;
				if(!wordChains.get(j).containsKey(nextPOS))
					continue;
				String ngram = NgramUtil.toNgram(wlist, j+1);
				if(wordChains.get(j).get(nextPOS).HasNext(ngram)){
					System.out.println("WordGram Size: "+(j+1));
					wlist.add(wordChains.get(j).get(nextPOS).getNext(ngram));
					break;
				}
			}
		}
		
		
		System.out.print(wlist.get(0));
		for(int i = 1; i < wlist.size(); ++i){
			if(!wlist.get(i).matches("[\\.,\\?;]"))
				System.out.print(" ");
			System.out.print(wlist.get(i));
		}
		
		
		
	}
	
}
