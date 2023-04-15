import java.sql.*;  
import java.io.*;    

public class Url
{
	public String urlLink;
	public String ptitle;
	public Clob text;
	public String startingUrl;
	public int crawlNum;
	
	public Url()
	{
		urlLink=null;
		ptitle=null;
		text=null;
		startingUrl= null;
		crawlNum= -1;
	}
	
	public Url(String urlLink, String ptitle, Clob textIn, String startingUrl, int crawlNum)
	{
		this.urlLink= urlLink;
		this.ptitle= ptitle;
		this.text = textIn;
		this.startingUrl=startingUrl;
		this.crawlNum=crawlNum;
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

	public String isStartingUrl() {
		return startingUrl;
	}

	public void setStartingUrl(String startingUrl) {
		this.startingUrl = startingUrl;
	}

	public int getCrawlNum() {
		return crawlNum;
	}

	public void getCrawlNum(int crawlNum) {
		this.crawlNum = crawlNum;
	}



	public String toString()
	{
		return (urlLink + " " + ptitle + " " +text+ " "+ startingUrl+ " "+ crawlNum);
	}
}