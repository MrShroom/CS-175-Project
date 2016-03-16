package textgen.generators;

public abstract class Generator {

	
	//Prints messages to console during training and generation if true.
	public boolean verbose = true;
	
	//Flag that turns on after train() has been called
	protected boolean trained = false;	
	
	//Business type generator will learn from
	protected String category = "Restaurants";
	
	//Star rating generator will learn from
	protected int star = 5;
	
	//Start review index in database train() will start at
	protected int startReview = 0;
	
	//Number of reviews generator will learn from
	protected int reviewCount = 1000;
	
	//Maximum ngram size generator will use.
	protected int ngramSize = 3;
	
	//Convenient toggle some generators use to sequence max-ngramsizes.
	protected boolean ngramBoost = false;

	
	public Generator setCategory(String cat) {
		category = cat;
		return this;
	}

	public Generator setStar(int star) {
		this.star = star;
		return this;
	}

	public Generator setNgramSize(int size) {
		ngramSize = size;
		return this;
	}

	public Generator setReviewCount(int start, int count) {
		this.startReview = start;
		this.reviewCount = count;
		return this;
	}

	public Generator setReviewCount(int count) {
		return setReviewCount(0, count);
	}

	public Generator() {

	}
	
	/**
	 * Trains the generator based on set parameters
	 * 
	 * @return
	 */
	public abstract Generator train();
	
	
	/**
	 * Generate a full review string
	 * 
	 * @return
	 */
	public abstract String generateReview();
}
