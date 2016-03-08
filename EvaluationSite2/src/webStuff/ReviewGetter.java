package webStuff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import MySQLToBagOfWords.BagOfWordUtilites;
import textgen.Generator;
import textgen.GeneratorNoPOS;
import textgen.GeneratorPOS;

public class ReviewGetter 
{
	private Generator myGenerator = new GeneratorPOS();
	private String currentCategory = "Restaurants";
	private int starRating = 1;
	private boolean usePOS = true;
	private static Random rnd = new Random();
	
	public void usePOS(boolean usePos)
	{
		if(usePos)
		{
			usePOS = true;
			myGenerator = new GeneratorPOS();
		}
		else
		{
			usePOS = false;
			myGenerator = new GeneratorNoPOS();
		}
			
	}
	
	public void setRandomCategory()
	{
		Connection  dBConnects = BagOfWordUtilites.setMySQLDB();
		Statement st = null;
	    ResultSet rs = null;
		String statement = "SELECT category, count(business_id) AS cnt " +
						   " FROM is_in_catagory Group BY category "+ 
						   " having cnt >=100 " +
						   " ORDER BY RAND() LIMIT 1;";
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
		
		//logic was not develop properly to hand the single quote in category, for now let's just skip
		//might go back later. 
		if(this.getCurrentCategory().contains("\'"))
			setRandomCategory();
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
	
	public String getGenerateReview()
	{
		Connection  dBConnects = BagOfWordUtilites.setMySQLDB();
	    ResultSet rs = null;
		String statement = "SELECT review_text FROM GeneratedReview" +
				" WHERE GeneratedReview.Stars=" +  starRating + " AND " +
				"GeneratedReview.Category=? AND " +	
				"GeneratedReview.POS=" + (usePOS ? "TRUE ": "FALSE " )+
				 "ORDER BY RAND() LIMIT 1;";
		String output = null;
		try
		{			
			java.sql.PreparedStatement preparedStatement = dBConnects.prepareStatement(statement);
			preparedStatement.setString(1, currentCategory );
			rs = preparedStatement.executeQuery();
			
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
				dBConnects.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}
	
	public String getRandomReviewFromDB()
	{
		Connection  dBConnects = BagOfWordUtilites.setMySQLDB();
	    ResultSet rs = null;
		String statement = "SELECT review_text FROM reviews" +
						   " WHERE reviews.stars=" +  starRating + " AND " +
						   "business_id IN (SELECT business_id FROM is_in_catagory WHERE "+
						   " is_in_catagory.category=? )" +	
						   "ORDER BY RAND() LIMIT 1;";
		String output = null;
		try
		{			
			java.sql.PreparedStatement preparedStatement = dBConnects.prepareStatement(statement);
			preparedStatement.setString(1, currentCategory );
			rs = preparedStatement.executeQuery();
			
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
				dBConnects.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}

	public void submittFeedback(String ID, int reviewNumber)
	{
		Connection  dBConnects = BagOfWordUtilites.setMySQLDB();
	    String statement;
	    if(reviewNumber == 1)
	    {
	    	statement = "INSERT INTO EvaluateTable (Id , review1, review2)"
	    			+ " VALUES ( \'" + ID + "\' ,1 ,0 )" +
	    			" ON DUPLICATE KEY UPDATE  review1 = review1 + 1;";
	    }
	    else
	    {
	    	statement = "INSERT INTO EvaluateTable (Id , review1, review2)"
	    			+ " VALUES ( \'" + ID + "\' , 0, 1 )" +
	    			" ON DUPLICATE KEY UPDATE  review2 = review2 + 1;";
	    }
	    
		try
		{			
			dBConnects.createStatement().executeUpdate(statement);
			
		} catch (SQLException e) 
		{
			e.printStackTrace();			
		}
		finally
		{
			try {
				dBConnects.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void main (String [] args)
	{
		ReviewGetter myReviewGetter = new ReviewGetter();
		myReviewGetter.setRandomStarRating();
		myReviewGetter.setRandomCategory();
		System.out.println(myReviewGetter.getRandomReviewFromDB());
	}	

}
