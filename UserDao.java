import java.sql.*;
import java.util.ArrayList;

public class UserDao {
  // ORACLE JDBC Driver
  String driverName    = "oracle.jdbc.driver.OracleDriver";

  // Connection to specific ORACLE database
  ////////////////////////////////////////////////
  // Put YOUR userid here -----------------------
  ////////////////////////////////////////////////
  String connectionURL = "jdbc:oracle:thin:thernandez2/csrocks55@csc325spring2023.cjjvanphib99.us-west-2.rds.amazonaws.com:1521:ORCL";
  Driver driver;
  Connection connection;

  public void setDriverName(String value){
    driverName = value;
  }

  public String getDriverName(){
    return driverName;
  }

  public void setConnectionURL(String value){
    connectionURL = value;
  }

  public String getConnectionURL(){
    return connectionURL;
  }

 /////////////////////////////////////////////////////////////
 // CONNECT TO DATABASE
 /////////////////////////////////////////////////////////////
 private void connectToDatabase()
 {
	 try {
		 System.out.println("Trying to connect to database getting driver..."); 
		 driver = (java.sql.Driver) Class.forName(driverName).newInstance();
		 
		 System.out.println("Driver loaded.  Connecting to database...");
		 connection = DriverManager.getConnection(connectionURL);
		 System.out.println("Connection successful!");
	 }
	 catch (Exception e)
	 {
		 e.printStackTrace();
	 }
 }

  /////////////////////////////////////////////////////////////
  // DISCONNECT FROM DATABASE.
  /////////////////////////////////////////////////////////////
  public void disconnectFromDatabase()
  {
	try {
		System.out.println("Trying to disconnect from database...");
		connection.close();
		System.out.println("Disconnection successful...");
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
  }

 // Finalize method disconnects on garbage collection
 public void finalize()
 {
	disconnectFromDatabase();
 }
	
 public UserDao()
 {
	connectToDatabase();
 }

  /////////////////////////////////////////////////////////////
  // SELECT ALL
  /////////////////////////////////////////////////////////////
  public ArrayList<Object> selectAll()
  {
	ArrayList<Object> result = new ArrayList<Object>();
	  
	   try {
		//System.out.println("In selectAll()...");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from ACCOUNT1");
		  
		while (resultSet.next())
		{
			User user = new User(resultSet.getString("username"),resultSet.getString("password"),resultSet.getString("firstname"),resultSet.getString("lastname"));
			result.add(user);
		}
		resultSet.close();
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}

	return result;
  }
	
  /////////////////////////////////////////////////////////////
  // SELECT BY USERNAME***
  /////////////////////////////////////////////////////////////
  public User selectByUsername(String username)
  {
	User result = null;
	try {
		//System.out.println("In selecByUsername()...");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from ACCOUNT1 where username='"+ (username)+"'");
		  
		if (resultSet.next())
		{
		  result = new User(resultSet.getString("username"),resultSet.getString("password"),resultSet.getString("firstname"),resultSet.getString("lastname"));
		}
		  
		resultSet.close();
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}

	return result;
  }

 /////////////////////////////////////////////////////////////
 // INSERT
 /////////////////////////////////////////////////////////////
 public void insert(User user)
 {
	try {
		//System.out.println("In insert()...");
		Statement statement = connection.createStatement();
		String sql = "insert into ACCOUNT1 values ("+
								"'"+user.username+"',"+
								"'"+user.password+"',"+
								"'"+user.firstname+"',"+
								"'"+user.lastname+"')";

		//System.out.println("insert(): "+sql);
		statement.executeUpdate(sql);
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
 }

 /////////////////////////////////////////////////////////////
 // UPDATE 
 /////////////////////////////////////////////////////////////
public void update(User user,String oldusername)//updates user 
 {
	  try {
		//System.out.println("In update()...");
		Statement statement = connection.createStatement();

			String us= user.username;
			String ps= user.password;
			String fn= user.firstname;
			String ln= user.lastname;
			

		statement.executeUpdate("delete from ACCOUNT1 where username='"+ oldusername+"'");
		String sql = "insert into ACCOUNT1 values ("+
								  "'"+us+"',"+
								  "'"+ps+"',"+
								  "'"+fn+"',"+
								  "'"+ln+"')";

		//System.out.println("update(): "+sql);
		statement.executeUpdate(sql);
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
 }

/////////////////////////////////////////////////////////////
// DELETE 
/////////////////////////////////////////////////////////////
 public void delete(User user)
 {
	 try {
		//System.out.println("In delete()...");
		Statement statement = connection.createStatement();
		statement.executeUpdate("delete from ACCOUNT1 where username='"+ user.username+"'");
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
 }
}
