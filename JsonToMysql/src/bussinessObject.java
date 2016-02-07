import java.util.HashSet;
import java.util.Set;

public class bussinessObject 
{
	private String bussiness_id;
	private String name;
	private double stars;
	private int review_count;
	private Set<String> categories;
	
	public bussinessObject() {
		super();
		this.bussiness_id = "";
		this.name = "";
		this.stars = 0;
		this.review_count = 0;
		this.setCategories(new HashSet<String>());
	}

	public bussinessObject(String bussiness_id, String name, double stars, int review_count) {
		super();
		this.bussiness_id = bussiness_id;
		this.name = name;
		this.stars = stars;
		this.review_count = review_count;
		this.setCategories(new HashSet<String>());
	}
	
	public String getBussiness_id() {
		return bussiness_id;
	}
	public void setBussiness_id(String bussiness_id) {
		this.bussiness_id = bussiness_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getStars() {
		return stars;
	}
	public void setStars(double stars) {
		this.stars = stars;
	}
	public Integer getReview_count() {
		return review_count;
	}
	public void setReview_count(int review_count) {
		this.review_count = review_count;
	}

	public Set<String> getCategories() {
		return categories;
	}

	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "bussinessObject [bussiness_id=" + bussiness_id + ", name=" + name + ", stars=" + stars
				+ ", review_count=" + review_count + ", categories=" + categories + "]";
	}
	
	

}
