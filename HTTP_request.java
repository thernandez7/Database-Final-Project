import java.net.*;
import java.net.MalformedURLException;
public class HTTP_request
{
	public static void main(String [] args)
	{
		//We can create an HttpUrlConnection instance using the openConnection() 
		//method of the URL class. Note that this method only creates a connection
		// object but doesn't establish the connection yet.
		try
		{
		URL url = new URL("http://web.stonehill.edu/compsci/");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}


	}

}