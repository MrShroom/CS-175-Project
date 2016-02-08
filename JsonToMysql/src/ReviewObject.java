/**
 * This class is a simple Object to represent a  review from
 * the yelp business data set.
 * 
 * @author Shaun McThomas
 * 
 */
public class ReviewObject 
{
	private String review_id; 
	private String review_type;
	private String business_id;
	private Integer stars;
	private String review_text;
	
	/**
	 * Constructor to set basic elements of review.
	 * @param business_id
	 * @param name
	 * @param stars
	 * @param review_count
	 */
	public ReviewObject(String review_id, String review_type, String business_id, Integer stars, String review_text) {
		super();
		this.review_id = review_id;
		this.review_type = review_type;
		this.business_id = business_id;
		this.stars = stars;
		this.review_text = review_text;
	}
	
	/**
	 *  Default constructor
	 */
	public ReviewObject() {
		super();
		this.review_id = "";
		this.review_type = "";
		this.business_id = "";
		this.stars = 0;
		this.review_text = "";
	}

	/**
	 * review_id getter
	 * @return review_id as String
	 */
	public String getReview_id() {
		return review_id;
	}
	
	/**
	 * review_id setter
	 * @param review_id
	 */
	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}
	
	/**
	 * review_type setter
	 * @return review_type as a String
	 */
	public String getReview_type() {
		return review_type;
	}
	
	/**
	 * @param review_type
	 */
	public void setReview_type(String review_type) {
		this.review_type = review_type;
	}
	public String getBusiness_id() {
		return business_id;
	}
	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}
	public Integer getStars() {
		return stars;
	}
	public void setStars(Integer stars) {
		this.stars = stars;
	}
	public String getReview_text() {
		return review_text;
	}
	public void setReview_text(String review_text) {
		this.review_text = review_text;
	}

	@Override
	public String toString() {
		return "ReviewObject [review_id=" + review_id + ", review_type=" + review_type + ", business_id=" + business_id
				+ ", stars=" + stars + ", review_text=" + review_text + "]";
	}

}
