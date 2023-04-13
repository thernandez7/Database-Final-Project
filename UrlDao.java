import java.sql.*;
import java.util.ArrayList;

public class UrlDao {
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
	
 public UrlDao()
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
		ResultSet resultSet = statement.executeQuery("select * from Urls");
		  
		while (resultSet.next())
		{
			Url url = new Url(resultSet.getString("urlLink"),resultSet.getString("ptitle"),resultSet.getClob("text"),resultSet.getString("startingUrl"),resultSet.getInt("crawlNum"));
			result.add(url);
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
  // SELECT BY Urllink***
  /////////////////////////////////////////////////////////////
  public Url selectByUrlLink(String url)
  {
	Url result = null;
	try {
		//System.out.println("In selecByUsername()...");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from Urls where urlLink='"+ url+"'");
		  
		if (resultSet.next())
		{
		  result = new Url(resultSet.getString("urlLink"),resultSet.getString("ptitle"),resultSet.getClob("text"),resultSet.getString("startingUrl"),resultSet.getInt("crawlNum"));
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
 public void insert(Url url)
 {
	try {
		//System.out.println("In insert()...");
		Statement statement = connection.createStatement();
		String sql = "insert into Urls values ("+
								"'"+url.urlLink+"',"+
								"'"+url.ptitle+"',"+
								"'"+url.text.toString()+ "',"+
								"'"+url.startingUrl+ "',"+
								"'"+String.valueOf(url.crawlNum)+"')";

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
 public void delete(Url url)
 {
	 try {
		//System.out.println("In delete()...");
		Statement statement = connection.createStatement();
		statement.executeUpdate("delete from QUERIES where topUrl='"+ url.urlLink+"'");//delete queries with that url as topurl
		statement.executeUpdate("delete from Urls where url='"+ url.urlLink+"'");//then delete URLs
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
 }
}
