package MySQLToBagOfWords;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * 
 * 
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
/**
 * This class is used Access the MySQl Database and retrieve reviews. 
 * 
 * @author Shaun McThomas
 *
 */
public class BagOfWordUtilites 
{
	private static Connection dBConnects;//Database connection 

	//compile Regex for small speed up
	private final static Pattern replaceRegexPattern = Pattern.compile("[^A-Za-z0-9]+");
	
	//compile Regex for small speed up
	private final static Pattern singleQoute = Pattern.compile("\'|`");
	
	//Set of words to remove from BOW
	private static HashSet<String> stopwords = new HashSet<String>();
	
	/**
	 * function to create connection to database
	 * @return the DB connection
	 */
	public static Connection setMySQLDB()
	{
		//Data base connection settings
		String url = "jdbc:mysql://shaunmcthomas.me:3306/cs175DB";
		String user = "user2";
        String password = "password";
        
        Connection dBConnects = null;
        
        try 
        {
        	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
        	dBConnects = DriverManager.getConnection(url, user, password);
        	
        } catch (SQLException ex) 
        {
            Logger lgr = Logger.getLogger(BagOfWordUtilites.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            System.exit(-1);
        }
        return dBConnects;
	}

	/**
	 * This function will query the database to get a collection reviews based on the given input 
	 * parameters. Then will count the words in these reviews to produce a bag of words for the collection.
	 * 
	 * <p>Note: Selecting all 2.2 million review by leaving categories, and stars empty and maxNumberOfReviewToUse to 
	 * 			0 is not a good idea and will take long time to process.</p>
	 *  
	 * @param categories				A set of categories(strings) which all reviews will share. Note call {@link #getSetOfCatagories(int) getSetOfCatagories} to get list
	 * 									of available categories. Pass in either null or empty set to select all categories
	 * 
	 * @param stars						A set of star ratings(integers) which all reviews will have been rated.For example pass in {1,2} to 
	 * 									get only review that have been rated with 1 or 2 stars.
	 *  
	 * @param maxNumberOfReviewToUse	The max number of review to use in bag of words(0 for no limit)
	 * 
	 * @return							A Map representation of a Bag of words (word-:Frequency of word)
	 */
	public static Map<String, Integer> getBagOfWords(Set<String> categories, Set<Integer> stars, Integer maxNumberOfReviewToUse)
	{
		Set<String> reviews = getSetOfReviews(categories, stars, maxNumberOfReviewToUse);
		Map<String, Integer> output = new HashMap<String, Integer>();
		for(String currentReview : reviews)
		{
			for(String token: tokenizeText(currentReview))
			{
				if(output.containsKey(token))
					output.put(token, output.get(token)+1);
				else
					output.put(token, 1);
			}
		}
		return output;
	}
	
	/**
	 * This function will query the database to get a collection reviews based on the given input 
	 * parameters. Then will count the words in these reviews to produce a bag of words for the collection normalized.
	 * All entries in map should add up to ~1(may be  slightly off due to rounding errors). 
	 * 
	 * <p>Note: Selecting all 2.2 million review by leaving categories, and stars empty and maxNumberOfReviewToUse to 
	 * 			0 is not a good idea and will take long time to process.</p>
	 *  
	 * @param categories				A set of categories(strings) which all reviews will share. Note call {@link #getSetOfCatagories(int) getSetOfCatagories} to get list
	 * 									of available categories. Pass in either null or empty set to select all categories
	 * 
	 * @param stars						A set of star ratings(integers) which all reviews will have been rated.For example pass in {1,2} to 
	 * 									get only review that have been rated with 1 or 2 stars.
	 *  
	 * @param maxNumberOfReviewToUse	The max number of review to use in bag of words(0 for no limit)
	 * 
	 * @return							A Map representation of a Bag of words (word-:percentage word appeared in collection)  
	 */
	public static Map<String, Double> getBagOfWordsNormilaized(Set<String> categories, Set<Integer> stars, Integer maxNumberOfReviewToUse)
	{
		Map<String, Integer> bow = getBagOfWords(categories,  stars,  maxNumberOfReviewToUse);
		int totalNumberOfwords = 0;
		for(String i : bow.keySet())
		{
			totalNumberOfwords += bow.get(i);
		}
		Map<String, Double> output = new HashMap<String, Double>();
		
		for(String i : bow.keySet())
		{
			output.put(i, (double)bow.get(i)/(double)totalNumberOfwords);
		}
		return output;
	}
	

	/**
	 * This function will query the database to return a collection reviews based on the given input 
	 * parameters.
	 * 
	 * <p>Note: Selecting all 2.2 million review by leaving categories, and stars empty and maxNumberOfReviewToUse to 
	 * 			0 is not a good idea and will take long time to process.</p>
	 * 
	 * @param categories				A set of categories(strings) which all reviews will share. Note call {@link #getSetOfCatagories(int) getSetOfCatagories} to get list
	 * 									of available categories. Pass in either null or empty set to select all categories
	 * 
	 * @param stars						A set of star ratings(integers) which all reviews will have been rated.For example pass in {1,2} to 
	 * 									get only review that have been rated with 1 or 2 stars.
	 *  
	 * @param maxNumberOfReviewToUse	The max number of review to use in bag of words(0 for no limit)
	 * 
	 * @return							A set of reviews  
	 */
	public static Set<String> getSetOfReviews(Set<String> categories, Set<Integer> stars, Integer maxNumberOfReviewToUse)
	{
		return getSetOfReviews(categories, stars, 0, maxNumberOfReviewToUse);
	}
	
	/**
	 * This function will query the database to return a collection of reviews based on the given input 
	 * parameters.
	 * 
	 * <p>Note: Selecting all 2.2 million reviews by leaving categories and stars empty, and start and stop to 
	 * 			0 is not a good idea and will take long time to process.</p>
	 * 
	 * @param categories				A set of categories(strings) which all reviews will share. Note call {@link #getSetOfCatagories(int) getSetOfCatagories} to get list
	 * 									of available categories. Pass in either null or empty set to select all categories
	 * 
	 * @param stars						A set of star ratings(integers) which all reviews will have been rated.For example pass in {1,2} to 
	 * 									get only review that have been rated with 1 or 2 stars.
	 *  
	 * @param start                 	The start index of chunk of reviews(0 for all)
	 * 
	 * @param numberOfReviews          The number of reviews (0 for no limit)
	 * 
	 * @return							A set of reviews  
	 */
	public static Set<String> getSetOfReviews(Set<String> categories, Set<Integer> stars, Integer start,Integer numberOfReviews )
	{
		dBConnects = setMySQLDB();
		Statement st = null;
	    ResultSet rs = null;
		String statement = "SELECT review_text" +
						   " FROM reviews";
		boolean whereAdded = false;
		if(!(stars == null || stars.isEmpty()))
		{
			statement += " WHERE ";
			whereAdded = true;			
			for(Integer str : stars)
				statement += " reviews.stars=" + str + " AND ";
			statement = statement.substring(0,statement.lastIndexOf(" AND "));
		}
		
		if(!(categories == null || categories.isEmpty()))
		{
			if (!whereAdded)
				statement += " WHERE ";
			else 
				statement += " AND ";
			whereAdded = true;	
			for(String cat : categories)
			{
				statement += " business_id IN (SELECT business_id FROM is_in_catagory WHERE ";
				statement += " is_in_catagory.category=\'" + cat + "\' AND ";
			}
			statement = statement .substring(0,statement.lastIndexOf(" AND "));
			for(int i = categories.size();i>0 ; i--)
				statement += " )";
		}
		
		if( numberOfReviews > 0)
			statement += " LIMIT " + start +"," + numberOfReviews;
		statement += " ;";
						   
		Set<String> output = new HashSet<String>();
		try
		{			
			st = dBConnects.createStatement();
			rs = st.executeQuery(statement);
			while (rs.next()) 
			{
				output.add(rs.getString(1));	
			}
			dBConnects.close();
			return output;
		} catch (SQLException e) 
		{
			e.printStackTrace();			
		}
		return null;
	}
	
	public static int countSetOfReviews(Set<String> categories, Set<Integer> stars )
	{
		dBConnects = setMySQLDB();
		Statement st = null;
	    ResultSet rs = null;
		String statement = "SELECT count(*)" +
						   " FROM reviews";
		boolean whereAdded = false;
		if(!(stars == null || stars.isEmpty()))
		{
			statement += " WHERE ";
			whereAdded = true;			
			for(Integer str : stars)
				statement += " reviews.stars=" + str + " AND ";
			statement = statement .substring(0,statement.lastIndexOf(" AND "));
		}
		
		if(!(categories == null || categories.isEmpty()))
		{
			if (!whereAdded)
				statement += " WHERE ";
			else 
				statement += " AND ";
			whereAdded = true;	
			for(String cat : categories)
			{
				statement += " business_id IN (SELECT business_id FROM is_in_catagory WHERE ";
				statement += " is_in_catagory.category=\'" + cat + "\' AND ";
			}
			statement = statement .substring(0,statement.lastIndexOf(" AND "));
			for(int i = categories.size();i>0 ; i--)
				statement += " )";
		}
		
		statement += " ;";
						   
		int output = 0;
		try
		{			
			st = dBConnects.createStatement();
			rs = st.executeQuery(statement);
			
			if(rs.next()) 
			{
				output = rs.getInt(1);	
			}
			dBConnects.close();
			return output;
		} catch (SQLException e) 
		{
			e.printStackTrace();			
		}
		return 0;
	}
	
	/**
	 * Query the database for set of most occurring categories.  
	 * 
	 * @param numberOfCategories 	The number of category label you wish to see.
	 * @return						a Set of category labels
	 */
	public static Set<String> getSetOfCatagories(int numberOfCategories)
	{
		dBConnects = setMySQLDB();
		Statement st = null;
	    ResultSet rs = null;
		String statement = "SELECT category, count(business_id) AS cnt " +
						   " FROM is_in_catagory Group BY category "+ 
						   	"ORDER BY cnt DESC"+  ((numberOfCategories > 0) ? " LIMIT " + numberOfCategories: "") + ";";
		Set<String> output = new HashSet<String>();
		try
		{			
			st = dBConnects.createStatement();
			rs = st.executeQuery(statement);
			
			while (rs.next()) 
			{
				output.add(rs.getString(1));	
			}
			dBConnects.close();
			return output;
		} catch (SQLException e) 
		{
			e.printStackTrace();			
		}
		return null;
	}
	
	private static List<String> tokenizeText(String input) 
	{
		if(stopwords.isEmpty())
		{
			try {
				Scanner in = new Scanner(new File("stopwords"));
				while(in.hasNext())
				{
					stopwords.add(singleQoute.matcher(in.nextLine().trim().toLowerCase()).replaceAll(""));
				}
				stopwords.add("");
				in.close();	
			} catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		input = singleQoute.matcher(input).replaceAll("").trim();
		input = replaceRegexPattern.matcher(input.toLowerCase()).replaceAll(" ").trim();//Change case to lower and remove all non word charters
		ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(input.split(" ")));//Create new Array list to hold tokens
		tokens.removeAll(stopwords);
		return tokens;
	}
}
