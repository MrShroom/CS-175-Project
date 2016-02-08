package textgen;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class ParserUtil {

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

	private static MaxentTagger tagger = null;

	private static void InitTagger() {
		tagger = new MaxentTagger("models/english-left3words-distsim.tagger");
	}

	public static List<TaggedWord> getFlatTaggedWordListPath(String path) {
		if(tagger == null)
			InitTagger();
		
		List<List<HasWord>> sentences;
		List<TaggedWord> list = new ArrayList<TaggedWord>();
		try {
			sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(path)));
			for (List<HasWord> sentence : sentences) {
				List<TaggedWord> tSentence = tagger.tagSentence(sentence);
				list.addAll(tSentence);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return list;

	}

}
