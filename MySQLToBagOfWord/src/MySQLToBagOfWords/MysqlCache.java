package MySQLToBagOfWords;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MysqlCache 
{
	private static Connection dBConnects;//Database connection 
	
	static final String WRITE_OBJECT_SQL = "INSERT INTO java_objects(name, object_value) VALUES (?, ?)";

	static final String READ_OBJECT_SQL = "SELECT object_value FROM java_objects WHERE id = ?";
	
	private static Connection setMySQLDB()
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
            Logger lgr = Logger.getLogger(BagOfWordUtilites.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
            System.exit(-1);
        }
        return dBConnects;
	}
	
	public static boolean isStored(String id)
	{
		dBConnects = setMySQLDB();
		Statement st = null;
	    ResultSet rs = null;
		String statement = "SELECT EXISTS(SELECT 1 FROM tempTable WHERE tempTable.id =\'" +id+"\');";
		try
		{			
			st = dBConnects.createStatement();
			rs = st.executeQuery(statement);
			
			if(rs.next()) 
			{
				return rs.getBoolean(0);	
			}
			
		} catch (SQLException e) 
		{
			e.printStackTrace();			
		}
		return false;
	}
}
