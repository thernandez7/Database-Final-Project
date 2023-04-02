import java.sql.*;
import java.util.ArrayList;

public class WebcrawlDao {
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
	
 public WebcrawlDao()
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
		ResultSet resultSet = statement.executeQuery("select * from Webcrawl");
		  
		while (resultSet.next())
		{
			Webcrawl webcrawl = new Webcrawl(resultSet.getString("timeRun"),resultSet.getString("username"),resultSet.getInt("crawlNum"));
			result.add(webcrawl);
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
  // SELECT BY crawlnum***
  /////////////////////////////////////////////////////////////
  public Webcrawl selectByCrawlNum(int crawlNum)
  {
	Webcrawl result = null;
	try {
		//System.out.println("In selecByUsername()...");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from webcrawl where crawlNum='"+ crawlNum+"'");
		  
		if (resultSet.next())
		{
		  result = new Webcrawl(resultSet.getString("timeRun"),resultSet.getString("username"),resultSet.getInt("crawlNum"));
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
 public void insert(Webcrawl webcrawl)
 {
	try {
		//System.out.println("In insert()...");
		Statement statement = connection.createStatement();
		String sql = "insert into Webcrawl values ("+
								"'"+webcrawl.timeRun+"',"+
								"'"+webcrawl.username+"',"+
								"'"+String.valueOf(webcrawl.crawlNum)+"')";

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
// DELETE 
/////////////////////////////////////////////////////////////
 public void delete(Webcrawl webcrawl)
 {
	 try {
		//System.out.println("In delete()...");
		Statement statement = connection.createStatement();
		statement.executeUpdate("delete from Webcrawl where crawlNum='"+ webcrawl.crawlNum+"'");
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
 }
}
