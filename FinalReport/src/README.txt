IMPORTANT!: If you run our code and want to set number of reviews above 2000, use the vm arguement "-mx1g" to give the program extra memory. Large review sets will cause out of memory errors otherwise.

GeneratorGUI is a folder containing an executable Jar file that implements a simple gui over our generator.
1. Set the star rating and review count to train on.
2. Press Train. Wait for text box to say training complete.
3. Press Generate as many times as you want to generate reviews.

In src/GeneratorLibrary/textgen:
The folder 'depreciatedcode' contians all of the modules, generators, and tests that are not used anywhere in our final project. Most of it is uncommented, and is only there to show the work we've done leading up to our final project.

generators/GeneratorPOS:
Is the flagship class that ties all other code together. Train() implements the training algorithm described in final report, and generate implements the generator()

generators/GeneratorGUI:
Simple swing gui that wraps around a GeneratorPOS.


MemorySafeUtil:
A module that provides utility functions for extracting ngram subsequences from larger word sequences.
Also contains the methods which increment the counts in our markov chains.
Is the used version of the depreciated NgramUtil module. It uses integer term-ids instead of strings to save memory.

ParserUtil:
A wrapper module for the stanford tagger. Loads the tagger, and handles the messy details behind changing a string to a tokenized list of tagged words.

Wordtab:
A small module that handles the mapping of token strings to integer term-ids. Was created to help save memory.

datastructs/BagOfObjects<T>
A term-counter. GetRandom() returns a random term with probability proportional to its count.
BagOfObjects_NoComp<T> uses a hashmap instead of a treemap.

datastructs/MarkovChainPOS:
A helper class which maps ngrams/targetPOStags to a list of successor-word counts.