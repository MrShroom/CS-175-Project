package textgen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class ParserUtil {

	
	//===========================================================
	//  Old unfinished code using heavy-weight parse-tree model.
	//===========================================================
	
	// private static List<String> getPosRoots(Tree tree) {
	// List<String> list = new ArrayList<String>();
	// _gpr(tree, list);
	// return list;
	// }
	//
	// private static void _gpr(Tree tree, List<String> list) {
	// if (tree.children().length == 1 && tree.children()[0].isLeaf())
	// list.add(tree.value());
	// else
	// for (Tree t : tree.children())
	// _gpr(t, list);
	// }
	//
	// private static LexicalizedParser lp = null;
	// private static void InitParser(){
	// String parserModel =
	// "edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz";
	// lp = LexicalizedParser.loadModel(parserModel);
	// }

	//Stanford part of speech tagger object.
	private static MaxentTagger tagger = null;

	//Tagger needs to be initiallized if null
	private static void InitTagger() {
		tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
	}

	
	//Converts a reader to a list of TaggedWords. is called by all other tagger methods.
	public static List<TaggedWord> getFlatTaggedWordListReader(Reader reader){
		if(tagger == null)
			InitTagger();
		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(reader);
		List<TaggedWord> list = new ArrayList<TaggedWord>();
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			list.addAll(tSentence);
		}
		return list;
	}
	
	//Converts a text file into a list of TaggedWords, which are token/partofspeechtag pairs.
	public static List<TaggedWord> getFlatTaggedWordListPath(String path) {
		
		List<TaggedWord> list = new ArrayList<TaggedWord>();
		try {
			list = getFlatTaggedWordListReader(new BufferedReader(new FileReader(path)));
			return list;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return new ArrayList<TaggedWord>();

	}
	
	//Generate taggedword list from plain string.
	public static List<TaggedWord> getFlatTaggedWordListString(String str) {
		return getFlatTaggedWordListReader(new StringReader(str));
	}
	
	public static List<String> getSentenceStructStrings(Reader reader){
		if(tagger == null)
			InitTagger();
		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(reader);
		List<String> list = new ArrayList<String>();
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			if(tSentence.isEmpty())
				continue;
			String posstr = tSentence.get(0).tag();
			for(int i = 1; i < tSentence.size();++i)
				posstr += " " + tSentence.get(i).tag();
			list.add(posstr);
		}
		return list;
	}
	
	
	public static List<String> getSentenceStructStrings(String str){
		return getSentenceStructStrings(new StringReader(str));
	}
	
	public static List<List<TaggedWord>> getSentenceStructList(Reader reader){
		if(tagger == null)
			InitTagger();
		List<List<HasWord>> sentences = MaxentTagger.tokenizeText(reader);
		List<List<TaggedWord>> list = new ArrayList<List<TaggedWord>>();
		for (List<HasWord> sentence : sentences) {
			List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			list.add(tSentence);
		}
		return list;
	}
	
	public static List<List<TaggedWord>> getSentenceStructList(String str){
		return getSentenceStructList(new StringReader(str));
	}
	
	public static List<List<TaggedWord>> getSentenceStructListPath(String path){
		try{
			return getSentenceStructList(new BufferedReader(new FileReader(path)));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static List<String> getSentenceStructStringsPath(String path){
		try{
			return getSentenceStructStrings(new BufferedReader(new FileReader(path)));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static List<String> sentenceToList(String sentence){
		return Arrays.asList(sentence.split("\\s+"));
	}
	
	//Converts a reader to a list of TaggedWords. is called by all other tagger methods.
		public static List<List<TaggedWord>> getTaggedWordListReader(Reader reader){
			if(tagger == null)
				InitTagger();
			List<List<HasWord>> sentences = MaxentTagger.tokenizeText(reader);
			List<List<TaggedWord>> list = new ArrayList<List<TaggedWord>>();
			for (List<HasWord> sentence : sentences) {
				List<TaggedWord> tSentence = tagger.tagSentence(sentence);
				list.add(tSentence);
			}
			return list;
		}
		
		//Converts a text file into a list of TaggedWords, which are token/partofspeechtag pairs.
		public static List<List<TaggedWord>> getTaggedWordListPath(String path) {
			
			try {
				List<List<TaggedWord>> list = getTaggedWordListReader(new BufferedReader(new FileReader(path)));
				return list;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			return new ArrayList<List<TaggedWord>>();

		}
		
		//Generate taggedword list from plain string.
		public static List<List<TaggedWord>> getTaggedWordListString(String str) {
			return getTaggedWordListReader(new StringReader(str));
		}
	
}
