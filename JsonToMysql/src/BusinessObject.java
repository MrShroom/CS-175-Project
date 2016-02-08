import java.util.HashSet;
import java.util.Set;

/**
 * This class is a simple Object to represent a  business from
 * the yelp business data set.
 * 
 * @author Shaun McThomas
 * 
 */
public class BusinessObject 
{
	private String business_id;
	private String name;
	private double stars;
	private int review_count;
	private Set<String> categories;
	
	/**
	 *  Default constructor
	 */
	public BusinessObject() {
		super();
		this.business_id = "";
		this.name = "";
		this.stars = 0;
		this.review_count = 0;
		this.setCategories(new HashSet<String>());
	}

	/**
	 * Constructor to set basic elements of business.
	 * @param business_id
	 * @param name
	 * @param stars
	 * @param review_count
	 */
	public BusinessObject(String business_id, String name, double stars, int review_count) {
		super();
		this.business_id = business_id;
		this.name = name;
		this.stars = stars;
		this.review_count = review_count;
		this.setCategories(new HashSet<String>());
	}
	
	/**
	 * bussiness_id getter
	 * @return The business id as a String
	 */
	public String getBussiness_id() {
		return business_id;
	}
	
	/**
	 * bussiness_id setter
	 * @param business_id the business id 
	 */
	public void setBussiness_id(String business_id) {
		this.business_id = business_id;
	}
	
	/**
	 * Business Name getter
	 * @return business name as a String
	 */
	public String getName() {
		return name;
	}
	
	
	/**
	 * Business Name setter
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Average business star getter
	 * @return Average star rating for business as a Double 
	 */
	public Double getStars() {
		return stars;
	}
	/**
	 * Average business star setter
	 * @param stars
	 */
	public void setStars(double stars) {
		this.stars = stars;
	}
	/**
	 * Review count getter
	 * @return Review count for business as a Integer
	 */
	public Integer getReview_count() {
		return review_count;
	}
	/**
	 * Review count setter
	 * @param review_count
	 */
	public void setReview_count(int review_count) {
		this.review_count = review_count;
	}

	/**
	 * categories getter
	 * @return reference to categories Set
	 */
	public Set<String> getCategories() {
		return categories;
	}

	/**
	 * categories setter 
	 * @param categories
	 */
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "bussinessObject [bussiness_id=" + business_id + ", name=" + name + ", stars=" + stars
				+ ", review_count=" + review_count + ", categories=" + categories + "]";
	}
	
	

}
