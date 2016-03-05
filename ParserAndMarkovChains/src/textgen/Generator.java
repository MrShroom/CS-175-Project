package textgen;

public abstract class Generator {

	public boolean verbose = true;
	
	
	protected boolean trained = false;	
	protected String category = "Restaurants";
	protected int star = 5;
	protected int startReview = 0;
	protected int reviewCount = 1000;
	protected int ngramSize = 3;
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
	
	public abstract Generator train();
	
	public abstract String generateReview();
}
