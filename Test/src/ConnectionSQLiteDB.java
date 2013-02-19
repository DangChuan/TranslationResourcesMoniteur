package trm;


import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.SQLException;
import java.sql.Statement;  


public class ConnectionSQLiteDB {
	
	public Connection getConnection(){
		Connection connection = null;
		try {  
	          Class.forName("org.sqlite.JDBC");  
	          connection = DriverManager  
	                  .getConnection("jdbc:sqlite:/home/nguyen/Chuan/newDb");   
	      } catch (Exception e) {  
	          e.printStackTrace();
	      }
		return connection;
	}
	
	public void closeConnection(Connection connection){
		if(connection!=null){
			try {  
	              connection.close();  
	          } catch (Exception e) {  
	              e.printStackTrace();  
	          }  
		}
	}
	
	
	public String getTypeId(int accountId, String service, int profileId){
		
		String uuid = "";
		Connection connection = null;  
		ResultSet resultSet = null;  
		Statement statement = null;
	      
		connection = getConnection();
		try {
			statement = connection.createStatement();
			//resultSet = statement.executeQuery("SELECT QUEUE FROM QUEUES WHERE ACCOUNT_ID = "+accountId+" AND PROFILE_ID="+profileId+" AND SERVICE_ID=(SELECT id FROM SERVICES WHERE NAME = '"+service+"')"); 
			resultSet = statement.executeQuery("SELECT QUEUE FROM QUEUES WHERE PROFILE_ID="+profileId+" AND SERVICE_ID=(SELECT id FROM SERVICES WHERE NAME = '"+service+"')");
			//resultSet = statement.executeQuery("SELECT id FROM SERVICES WHERE NAME = '"+service+"' ");
		      while (resultSet.next()) {  
		          System.out.println("UUID:"  
		                  + resultSet.getString("QUEUE")); 
		          uuid = resultSet.getString("QUEUE");
		          return uuid;
		      }  
		} catch (SQLException e) {
			e.printStackTrace();
		}  
		return uuid;
	}
	
	public static void main(String[] args) {  
		ConnectionSQLiteDB connectionBD = new ConnectionSQLiteDB();
		System.out.println("Value de serviceId : " + connectionBD.getTypeId(2, "Translate_en_fr", 0));
	  }  
	
} 


