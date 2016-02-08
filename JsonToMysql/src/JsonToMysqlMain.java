
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonToMysqlMain 
{
	private static Connection dBConnects;//Database connection 

	public static Connection setMySQLDB()//function to create connection to database
	{
		//Data base connection settings
		String url = "jdbc:mysql://shaunmcthomas.me:3306/cs175DB";
		String user = "user2";
        String password = "password";
        
        Connection dBConnects = null;
        
        try 
        {
        	dBConnects = DriverManager.getConnection(url, user, password);
        	
        } catch (SQLException ex) 
        {
            Logger lgr = Logger.getLogger(JsonToMysqlMain.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            System.exit(-1);
        }
        return dBConnects;
	}
	 

	public static void main(String[] args) 
	{
		dBConnects = setMySQLDB();//create DB connection 
		//parseBusinesses();//parse and load Business data into DB
		parseReviews();//parse and load Review data into DB
		try {
			dBConnects.close();//clean up
		} catch (SQLException e) 
		{	
			e.printStackTrace();
		}
	}
	
	public static void parseBusinesses()
	{
		try {
			//open file
			Scanner reader = new Scanner(new FileReader("../data/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_business.json"));
			
			while(reader.hasNext())//while more lines to process
			{
				//load line into JSON parser
				JsonParser businessParser = Json.createParser(new StringReader(reader.nextLine()));
				//create new business object
				BusinessObject currentBussiness = new BusinessObject();
				//move passed START_OBJECT
				businessParser.next();
				//set event to first real event
				Event event = businessParser.next();
		
				String keyName ="";
				
				while(event != JsonParser.Event.END_OBJECT)
				{
					switch (event)
					{
					case KEY_NAME:
						keyName = businessParser.getString();
						event = businessParser.next();
						break;
					case VALUE_NUMBER://same method will handle all these cases 
					case START_ARRAY:	
					case VALUE_STRING:
						set_atrib(keyName,currentBussiness,businessParser);
						event = businessParser.next();
						break;
					case START_OBJECT ://none of the data we want is in an object, we need to just move passed it
						int count = 1;
						while(count > 0)
						{
							event = businessParser.next();
							if(event == JsonParser.Event.END_OBJECT )
								count--;
							else if(event == JsonParser.Event.START_OBJECT )
								count++;	
							
						}
						if(event == JsonParser.Event.END_OBJECT )
							event = businessParser.next();
						break;
					default:
						event = businessParser.next();
						break;					
						
					}
					
				}
				System.out.println(currentBussiness);
				loadBusinessIntoDB(currentBussiness);//load the business into DB
			}
			reader.close();
			
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void loadBusinessIntoDB(BusinessObject currentBussiness) 
	{

		String statement = "INSERT INTO businesses (business_id, name, stars, review_count)" +
							" VALUES ( ?, ?, ?, ? );";
		try
		{
			java.sql.PreparedStatement preparedStatement = dBConnects.prepareStatement(statement);
			preparedStatement.setString(1, currentBussiness.getBussiness_id());
			preparedStatement.setString(2, currentBussiness.getName());
			preparedStatement.setString(3, currentBussiness.getStars().toString());
			preparedStatement.setString(4, currentBussiness.getReview_count().toString());
			preparedStatement.executeUpdate();

			for(String cat :currentBussiness.getCategories() )
			{
				statement = "INSERT INTO is_in_catagory (business_id, category)" +
						" VALUES ( ?, ?);";
				preparedStatement = dBConnects.prepareStatement(statement);
				preparedStatement.setString(1, currentBussiness.getBussiness_id());
				preparedStatement.setString(2, cat);
				preparedStatement.executeUpdate();
			}
			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}

	private static void set_atrib(String fieldName, BusinessObject currentBussiness, JsonParser data)
	{		
		switch (fieldName)
		{
		case "business_id"://bussiness_id
			currentBussiness.setBussiness_id( data.getString());
			break;
		case "name"://name
			currentBussiness.setName(data.getString());
			break;
		case "stars"://stars
			currentBussiness.setStars(Double.valueOf(data.getString()));
			break;
		case "review_count" ://review_count
			currentBussiness.setReview_count(data.getInt());
			break;
		case "categories"://category			
			for(Event event = data.next();event !=  JsonParser.Event.END_ARRAY; event= data.next())
			{
				if(event != JsonParser.Event.START_OBJECT  || event != JsonParser.Event.END_OBJECT )
					currentBussiness.getCategories().add(data.getString());				
			}
			break;			
		}		
	}
	
	//the functions below follow same format as above. 
	public static void parseReviews()
	{
		try {
			
			Scanner reader = new Scanner(new FileReader("../data/yelp_dataset_challenge_academic_dataset/yelp_academic_dataset_review.json"));
			
			while(reader.hasNext())
			{
				JsonParser reviewParser = Json.createParser(new StringReader(reader.nextLine()));
				ReviewObject currentReview = new ReviewObject();
				reviewParser.next();
				Event event = reviewParser.next();
				String keyName ="";
				while(event != JsonParser.Event.END_OBJECT)
				{
					switch (event)
					{
					case KEY_NAME:
						keyName = reviewParser.getString();
						event = reviewParser.next();
						break;
					case VALUE_NUMBER:
					case START_ARRAY:	
					case VALUE_STRING:
						set_atrib_review(keyName,currentReview,reviewParser);
						event = reviewParser.next();
						break;
						
					case START_OBJECT :
						int count = 1;
						while(count > 0)
						{
							event = reviewParser.next();
							if(event == JsonParser.Event.END_OBJECT )
								count--;
							else if(event == JsonParser.Event.START_OBJECT )
								count++;	
							
						}
						if(event == JsonParser.Event.END_OBJECT )
							event = reviewParser.next();
						break;
					default:
						event = reviewParser.next();
						break;					
						
					}
					
				}
				System.out.println(currentReview);
				loadReviewIntoDB(currentReview);
			}
			reader.close();
			
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	private static void loadReviewIntoDB(ReviewObject currentReview) 
	{

		
		String statement = "INSERT INTO reviews (review_id, review_type, business_id, stars, review_text)" +
							" VALUES ( ?, ?, ?, ?, ? );";
		try
		{
			java.sql.PreparedStatement preparedStatement = dBConnects.prepareStatement(statement);
			preparedStatement.setString(1, currentReview.getReview_id());
			preparedStatement.setString(2, currentReview.getReview_type());
			preparedStatement.setString(3, currentReview.getBusiness_id());
			preparedStatement.setString(4, currentReview.getStars().toString());
			preparedStatement.setString(5, currentReview.getReview_text());
			preparedStatement.executeUpdate();

			
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
	}


	private static void set_atrib_review(String fieldName, ReviewObject currentReview, JsonParser data)
	{		
		switch (fieldName)
		{
		case "review_id":
			currentReview.setReview_id( data.getString());
			break;
		case "stars":
			currentReview.setStars(data.getInt());
			break;
		case "text":
			currentReview.setReview_text(data.getString());
			break;
		case "type" :
			currentReview.setReview_type(data.getString());
			break;
		case "business_id":			
			currentReview.setBusiness_id(data.getString());
			break;			
		}		
	}
}
