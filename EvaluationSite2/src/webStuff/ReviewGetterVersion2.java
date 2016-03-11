package webStuff;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import MySQLToBagOfWords.BagOfWordUtilites;


public class ReviewGetterVersion2 
{
	public ReviewCompariosnClass getGenerateReview()
	{
		Connection  dBConnects = BagOfWordUtilites.setMySQLDB();
	    ResultSet rs = null;
		String statement = "SELECT * FROM GeneratedReview2 " +
				 "ORDER BY RAND() LIMIT 1;";
		ReviewCompariosnClass output = null;
		try
		{			
			java.sql.PreparedStatement preparedStatement = dBConnects.prepareStatement(statement);
			rs = preparedStatement.executeQuery();
			
			if(rs.next()) 
			{
				output = new ReviewCompariosnClass(rs.getInt("Stars"),rs.getString("Category"),rs.getString("gen_review_text_withPOS"),rs.getString("gen_review_text_NoPOS"),rs.getString("rand_review_text"));	
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
		ReviewGetterVersion2 myReviewGetter = new ReviewGetterVersion2();
		System.out.println(myReviewGetter.getGenerateReview());
	}	

}
