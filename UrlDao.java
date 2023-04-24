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
	
 public UrlDao()
 {
	connectToDatabase();
 }

  /////////////////////////////////////////////////////////////
  // SELECT ALL
  /////////////////////////////////////////////////////////////
  public ArrayList<Url> selectAll()
  {
	ArrayList<Url> result = new ArrayList<Url>();
	  
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
		if (url.ptitle == null) {
			url.setPtitle("no page title");
		}
		if (url.text.length() == 0) {
			return;
		}
		//System.out.println("In insert()...");
		Statement statement = connection.createStatement();
		String sql = "insert into Urls values ("+
								"'"+url.urlLink+"',"+
								"'"+url.ptitle+"',"+
								text_clob_split(url.text.getSubString(1, (int)url.text.length()).replace("'", " "))+","+
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

//  Converts the passed text into a SQL call to create CLOBs and concatenate them
// 	Max string length for TO_CLOB call is 4000 chars, so string must be divided and appended 
// 	Dividing into chunks of 4000 wasn't working for some reason, so I lowered the char count to 3000 for each section
 public String text_clob_split(String textIn) {
	if (textIn.length() < 3000) {
		return ("TO_CLOB('" + textIn + "')");
	}
	else {
		String result = "";
		for (int i = 0; i < ((textIn.length() / 3000) + 1); i++) {
			if (i != 0) {
				result = result + " || ";
			}

			result = result +"TO_CLOB('" + textIn.substring(i * 3000, Math.min((i+1) * 3000, textIn.length())) + "')";
		}
		return result;
	}
 }

 public ArrayList<Url> search(String queryIn) {
	ArrayList<Url> results = new ArrayList<Url>();
	   try {
		//System.out.println("In selectAll()...");
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from Urls where dbms_lob.instr(text,'" + queryIn +"') >= 1");
		  
		while (resultSet.next())
		{
			Url url = new Url(resultSet.getString("urlLink"),resultSet.getString("ptitle"),resultSet.getClob("text"),resultSet.getString("startingUrl"),resultSet.getInt("crawlNum"));
			results.add(url);
		}
		resultSet.close();
		statement.close();
	}
	catch (Exception e)
	{
		e.printStackTrace();
	}

	return results;
 }

 public static void main(String[] args) throws SQLException {
	UrlDao uDao = new UrlDao();
	Statement s = uDao.connection.createStatement();
	s.executeUpdate("DELETE FROM URLS");
	System.out.println("Cleared urls");
 } 
}
