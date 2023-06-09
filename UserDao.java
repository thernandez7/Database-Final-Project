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
		 driver = (java.sql.Driver) Class.forName(driverName).newInstance();
		 connection = DriverManager.getConnection(connectionURL);
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
		//System.out.println("Trying to disconnect from database...");
		connection.close();
		//System.out.println("Disconnection successful...");
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
		ResultSet resultSet = statement.executeQuery("select * from USERS");
		  
		while (resultSet.next())
		{
			User user = new User(resultSet.getString("username"),resultSet.getString("password"),resultSet.getString("name"),resultSet.getString("admin"));
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
		ResultSet resultSet = statement.executeQuery("select * from USERS where username='"+ (username)+"'");
		  
		if (resultSet.next())
		{
		  result = new User(resultSet.getString("username"),resultSet.getString("password"),resultSet.getString("name"),resultSet.getString("admin"));
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
		String sql = "insert into USERS values ("+
								"'"+user.username+"',"+
								"'"+user.password+"',"+
								"'"+user.name+"',"+
								"'"+user.admin+"')";

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

			String us= user.username;//new values
			String ps= user.password;
			String fn= user.name;
			String ad= user.admin;
			
		if (!us.equals(oldusername))//changing username so must change in tables that reference as a foreign key
		{
			//insert new username into users- this will be the reference of the other tables
			String sql = "insert into USERS values ("+
									"'"+us+"',"+
									"'"+ps+"',"+
									"'"+fn+"',"+
									"'"+ad+"')";
			statement.executeUpdate(sql);

			//change the webcrawl and queries reference to username
			String sql1 = "UPDATE WEBCRAWL SET "+
								  "username='"+us+"'"+
								  " WHERE username = '"+ oldusername+"'";//reference new username 

			statement.executeUpdate(sql1);

			String sql2 = "UPDATE QUERIES SET "+
								  "username='"+us+"'"+
								  " WHERE username = '"+ oldusername+"'";//reference new username
			statement.executeUpdate(sql2);
		 	statement.executeUpdate("delete from USERS where username='"+ oldusername+"'");
		}
		else
		{
			String sql3 = "UPDATE USERS SET "+
									"username='"+us+"',"+
									"password='"+ps+"',"+
									"name='"+fn+"',"+
									"admin='"+ad+"' WHERE username = '"+ oldusername+"'";

			//System.out.println("update(): "+sql);
			statement.executeUpdate(sql3);
		}
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
	
		statement.executeUpdate("delete from URLS where crawlNum= (select crawlNum from webcrawl where username='"+ user.username+"')");//delete from urls all urls with the crawlnums did by the user
		statement.executeUpdate("delete from  WEBCRAWL where username='"+ user.username+"'");//clear webcrawls done by users
		statement.executeUpdate("delete from QUERIES where username='"+ user.username+"'");//clear queries done by user
		statement.executeUpdate("delete from USERS where username='"+ user.username+"'");//finally delete user
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
 }
}
