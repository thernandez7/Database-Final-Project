import java.net.*;
import java.io.*;
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

			//set request properties
			con.setRequestProperty("Content-Type", "application/json");

			//set timeout values
			con.setConnectTimeout(7000);
			con.setReadTimeout(7000);

			// //Read all cookies from responce 
			// String cookiesHeader = con.getHeaderField("Set-Cookie");
			// List<HttpCookie> cookies = HttpCookie.parse(cookiesHeader);
			// //add cookies to cookie store
			// cookies.forEach(cookie -> cookieManager.getCookieStore().add(null, cookie));

			// //to add the cookies to the request, we need to set the Cookie header, after closing and reopening the connection:
			// con.disconnect();
			// con = (HttpURLConnection) url.openConnection();
			// con.setRequestProperty("Cookie", 
			// StringUtils.join(cookieManager.getCookieStore().getCookies(), ";"));

			//When a request returns a status code 301 or 302, indicating a redirect, we can retrieve the 
			//Location header and create a new request to the new URL:
			int status = con.getResponseCode();
			if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM) 
			{
				String location = con.getHeaderField("Location");
				URL newUrl = new URL(location);
				con = (HttpURLConnection) newUrl.openConnection();
			}

			// read the response of the request and place it in a content String:
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
			}
			in.close();

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}