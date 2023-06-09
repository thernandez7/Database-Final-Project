import java.sql.*;
import java.util.ArrayList;

public class QueryDao {
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
	
 public QueryDao()
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
		ResultSet resultSet = statement.executeQuery("select * from QUERIES");
		  
		while (resultSet.next())
		{
			Query query = new Query(resultSet.getString("username"),resultSet.getString("query"),resultSet.getString("topurl"));
			result.add(query);
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
  // SELECT BY query
  /////////////////////////////////////////////////////////////
  public Query selectByQuery(String queryPassed)
  {
	Query result = null;
	try {
		//System.out.println("In selecByUsername()...");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from QUERIES where query='"+ queryPassed+"'");
		  
		if (resultSet.next())
		{
		  result = new Query(resultSet.getString("username"),resultSet.getString("query"),resultSet.getString("topurl"));
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
 public void insert(Query query)
 {
	try {
		//System.out.println("In insert()...");
		Statement statement = connection.createStatement();
		String sql;
		if (query.topURL!= null)
		{
			sql = "insert into QUERIES values ("+
								"'"+query.username+"',"+
								"'"+query.queryInput+"',"+
								"'"+query.topURL+ "')";
		}
		else //is null- no results for query
		{
			sql = "insert into QUERIES values ("+
								"'"+query.username+"',"+
								"'"+query.queryInput+"',"+
								""+query.topURL+ ")";// will be null
			System.out.println("No results from search");
		}
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
 public void delete(Query query)
 {
	 try {
		//System.out.println("In delete()...");
		Statement statement = connection.createStatement();
		statement.executeUpdate("delete from QUERIES where query='"+ query.queryInput+"'");
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}
 }
}
