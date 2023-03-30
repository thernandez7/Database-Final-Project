import java.sql.*;  
import java.io.*;    

public class Url
{
	public String urlLink;
	public String ptitle;
	public Clob text;
	
	public Url()
	{
		urlLink=null;
		ptitle=null;
		text=null;
	}
	
	public Url(String urlLink, String ptitle, Clob text)
	{
		this.urlLink= urlLink;
		this.ptitle= ptitle;
		this.text= text;
	}
	
	/* getters and setters for the attributes */
	public String getUrlLink() {
		return urlLink;
	}

	public void setUrlLink(String urlLink) {
		this.urlLink = urlLink;
	}

	public String getPtitle() {
		return ptitle;
	}

	public void setPtitle(String ptitle) {
		this.ptitle = ptitle;
	}

	public Clob getText() {
		return text;
	}

	public void setText(Clob text) {
		this.text = text;
	}

	public String toString()
	{
		return (urlLink + " " + ptitle + " " +text);
	}
}