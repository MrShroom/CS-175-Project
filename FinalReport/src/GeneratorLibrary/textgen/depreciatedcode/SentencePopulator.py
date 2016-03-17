import MySQLtoBagOfWordsPython.MySQLToBagOfWords.BagOfWordUtilites as BoWU
import nltk
import random


def _fillInSentence(skeleton, bot_map, n):
    """
    Populates a given list of parts of speech (skeleton) with the correct types of words as found in bot_map (bag of terms map)
    Fails if skeleton contains a part of speech not found in the bag of terms. Will not be a problem when the skeletons are generated
    from the same data that bot_map is generated from.
    """
    sentence = ["" for i in range(n)]
    previousWords = []
    currentPoS = ""
    for i in range(len(skeleton)):
        currentPoS = skeleton[i]
        previousWords = sentence[i:i+n]
        try:#Will fail if previous two words do not occur at all in text used to make bag of terms
            wordOptions = bot_map[currentPoS][str(previousWords)]
            #sentence.append(max(wordOptions, key=wordOptions.get))
            sentence.append(random.choice([word for word in wordOptions for x in range(wordOptions[word])]))
        except KeyError:
            wordOptions = random.choice(list(bot_map[currentPoS].values()))
            sentence.append(random.choice([word for word in wordOptions for x in range(wordOptions[word])]))
    return sentence[n:]


def _generatePartOfSpeechBagsOfTerms(text, n):
    """
    Generates a dictionary of bags of terms; one for each part of speech:
    {"POS":{"["previous","words"]":{"word": frequency(int), ...}, ...], ...}, ...}
    """
    tokens_list = nltk.tokenize.word_tokenize(text)
    partOfSpeech_list = nltk.pos_tag(tokens_list)
    output = {}
    [tokens_list.insert(0,"") for i in range(n)]
    previousWords = []
    currentWord = ""
    currentPoS = ""
    #for every word in the text...
    for i in range(len(tokens_list)-n):
        #get current word and get preceding n words
        previousWords = tokens_list[i:i+n]
        currentWord = tokens_list[n+i]
        currentPoS = partOfSpeech_list[i][1]
        #if output has no entry for this pos, create one
        if currentPoS not in output:
            output[currentPoS] = {}
        #if pos dict has no entry for this list of n previous words, create one
        if str(previousWords) not in output[currentPoS]:
            output[currentPoS][str(previousWords)] = {currentWord: 1}
        #otherwise...
        else:
            #if pos dict has an entry for these previous words, but it doesn't correspond to the same current word, create a new entry
            if currentWord not in output[currentPoS][str(previousWords)]:
                output[currentPoS][str(previousWords)][currentWord] = 1
            #otherwise just increment existing entry
            else:
                output[currentPoS][str(previousWords)][currentWord] += 1
    return output


def Populate_Sentence(inputText, sentenceSkeleton, n):
    """
    :param inputText: raw string of text to generate a sentence from
    :param sentenceSkeleton: list of strings representing parts of speech
    :param n: number of previous words to take into account when deciding what words to use
    :return: list of words (strings) that together create a full sentence.
    """
    bagsOfTerms = _generatePartOfSpeechBagsOfTerms(inputText, n)
    return _fillInSentence(sentenceSkeleton, bagsOfTerms, n)



if __name__ == "__main__":
    prefix_n = 5 #or whatever
    sampleText = "My sister was walking through the garden in the morning when she saw a beautiful butterfly sitting on a pink flower. It had colorful wings which enhanced its beauty. She wanted to watch it closely so she walked towards it slowly. But before she could get closer, the insect flew away and sat on another flower. ‘Ah!’ my sister cried in disappointment."
    # generated = generatePartOfSpeechBagsOfTerms(sampleText, prefix_n)
    # for pos in generated:
    #     print(pos+":")
    #     for prev in generated[pos]:
    #         for word in generated[pos][prev]:
    #             print("\t"+prev+"--> \""+str(word)+"\", "+str(generated[pos][prev][word])+" times.")
    # print()

    skeleton = ["PRP", "VBD", "DT", "JJ", "NN", "IN", "NN", "VBG", "IN", "DT", "NN", "."]
    print(Populate_Sentence(sampleText, skeleton, prefix_n))