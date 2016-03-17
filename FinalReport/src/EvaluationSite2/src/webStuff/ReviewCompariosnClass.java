package webStuff;

public class ReviewCompariosnClass 
{
	private int starRating;
	private String catergory;
	private String reviewPOS;
	private String reviewNoPOS;
	private String reviewYelp;
	
	public ReviewCompariosnClass(int starRating, String catergory, String reviewPOS, String reviewNoPOS,
			String reviewYelp) {
		super();
		this.starRating = starRating;
		this.catergory = catergory;
		this.reviewPOS = reviewPOS;
		this.reviewNoPOS = reviewNoPOS;
		this.reviewYelp = reviewYelp;
	}
	
	public int getStarRating() {
		return starRating;
	}
	
	public void setStarRating(int starRating) {
		this.starRating = starRating;
	}
	public String getCatergory() {
		return catergory;
	}
	public void setCatergory(String catergory) {
		this.catergory = catergory;
	}
	public String getReviewPOS() {
		return reviewPOS;
	}
	public void setReviewPOS(String reviewPOS) {
		this.reviewPOS = reviewPOS;
	}
	public String getReviewNoPOS() {
		return reviewNoPOS;
	}
	public void setReviewNoPOS(String reviewNoPOS) {
		this.reviewNoPOS = reviewNoPOS;
	}
	public String getReviewYelp() {
		return reviewYelp;
	}
	public void setReviewYelp(String reviewYelp) {
		this.reviewYelp = reviewYelp;
	}

	@Override
	public String toString() {
		return "ReviewCompariosnClass [starRating=" + starRating + ", catergory=" + catergory + ", \nreviewPOS="
				+ reviewPOS + ", \nreviewNoPOS=" + reviewNoPOS + ", \nreviewYelp=" + reviewYelp + "]";
	}
	

}
