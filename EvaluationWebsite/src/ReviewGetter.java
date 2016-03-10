

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import MySQLToBagOfWords.BagOfWordUtilites;
import textgen.Generator;

public class ReviewGetter 
{
	private Generator myGenerator = new Generator();
	private String currentCategory = "Restaurants";
	private int starRating = 1;
	private static Random rnd = new Random();
	
	public void setRandomCategory()
	{
		Connection  dBConnects = BagOfWordUtilites.setMySQLDB();
		Statement st = null;
	    ResultSet rs = null;
		String statement = "SELECT category, count(business_id) AS cnt " +
						   " FROM is_in_catagory Group BY category "+ 
						   	"ORDER BY RAND() LIMIT 1;";
		try
		{			
			st = dBConnects.createStatement();
			rs = st.executeQuery(statement);
			
			if(rs.next()) 
			{
				setCurrentCategory(rs.getString(1));	
			}
			
		} catch (SQLException e) 
		{
			e.printStackTrace();			
		}
		finally
		{
			try {
				rs.close();
				st.close();
				dBConnects.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void setRandomStarRating()
	{ 
		setStarRatinge(rnd.nextInt(5)+ 1);
	}
	
	public String getCurrentCategory() {
		return currentCategory;
	}

	public void setCurrentCategory(String currentCategory) {
		this.currentCategory = currentCategory;
	}

	public int getStarRatinge() {
		return starRating;
	}

	public void setStarRatinge(int starRating) {
		this.starRating = starRating;
	}	
	
	public String generateReview()
	{
		myGenerator.setCategory(currentCategory);
		myGenerator.setStar(starRating);
		myGenerator.setReviewCount(1000);
		myGenerator.setNgramSize(3);
		myGenerator.train();
		return myGenerator.generateReview();		
	}
	
	public String getRandomReviewFromDB()
	{
		Connection  dBConnects = BagOfWordUtilites.setMySQLDB();
		Statement st = null;
	    ResultSet rs = null;
		String statement = "SELECT review_text FROM reviews" +
						   " WHERE reviews.stars=" +  starRating + " AND " +
						   "business_id IN (SELECT business_id FROM is_in_catagory WHERE "+
						   " is_in_catagory.category=\'" + currentCategory + "\' )" +	
						   "ORDER BY RAND() LIMIT 1;";
		String output = null;
		try
		{			
			st = dBConnects.createStatement();
			rs = st.executeQuery(statement);
			
			if(rs.next()) 
			{
				output = rs.getString(1);	
			}
			
		} catch (SQLException e) 
		{
			e.printStackTrace();			
		}
		finally
		{
			try {
				rs.close();
				st.close();
				dBConnects.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}

	public static void main (String [] args)
	{
		ReviewGetter myReviewGetter = new ReviewGetter();
		myReviewGetter.setRandomStarRating();
		System.out.println(myReviewGetter.getRandomReviewFromDB());
	}	

}
