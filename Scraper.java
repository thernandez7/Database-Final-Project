import java.util.*;
import java.lang.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.sql.*;  //for Clob type
import java.io.*;  

public class Scraper
{
	public static void main(String [] args) throws IOException
	{
		String homepage = "http://web.stonehill.edu/compsci/";
		Scraper test = new Scraper();
		// System.out.println("Title Test");
		// System.out.println(test.titleFinder(homepage));
		System.out.println("Scraper Test");
		System.out.println(test.textScraper(homepage));
		// System.out.println("Crawler Test");
		// test.webCrawler(homepage, homepage,1);
	}

	public Clob textScraper(String url) throws IOException 
	{
		Document doc = Jsoup.connect(url).timeout(6000).get(); // http://web.stonehill.edu/compsci/ComputerScienceCourses.htm
		Elements body = doc.select("tbody");
		String total = "";
		for(Element e : body.select("tr")) {
			String text = e.text(); 
			total += text + " ";
		}
		return stringToClob(total);
	}

	public String titleFinder(String url) throws IOException 
	{
		Document doc = Jsoup.connect(url).timeout(6000).get(); // http://web.stonehill.edu/compsci/ComputerScienceCourses.htm
		String title = "";
		Elements body = doc.select("head");
		for(Element e : body.select("title")) {
			title = e.text(); 
		}
		// System.out.println("Title - " + title);
		return title;
	}

	public void webCrawler(String url, String homepage, int crawlNum) throws IOException 
	{
		if(checkURL(url)) {
			MainProgram mp = new MainProgram();
			// call titleFinder and set ptitle equal to result
			String ptitle = titleFinder(url);
			
			// Insert into DB
			mp.insertUrl(url,ptitle,textScraper(url),homepage,crawlNum);//fit with correct parameters
			Document doc = Jsoup.connect(url).timeout(6000).get(); // http://web.stonehill.edu/compsci/ComputerScienceCourses.htm
			// String homepage = "http://web.stonehill.edu/compsci/";
			Elements body = doc.select("tbody");
			System.out.println(url + " --- " + body.select("tr").size());
			int i = 0;
			for(Element e : body.select("tr")) {
				String addition = e.select("a").attr("href");
				if(addition.contains(":") || addition.contains(" ")) { continue; }
				String urls = homepage + addition;
				webCrawler(urls, homepage, crawlNum);
			}
		}
	}

	public boolean checkURL(String url) { // will change this to work with database
		MainProgram mp = new MainProgram();
		// System.out.println("run selectURL");
		String check = mp.SelectUrl(url);
		// System.out.println("-------" + check);
		if(check != "") // Already in DB
			return false; 
		return true; // Not in DB
	}
	
	public Clob stringToClob(String str) {  // From java2s.com  - converting a string to Clob
        if (null == str) {
            return null;
        } else {
            try {
                java.sql.Clob c = new javax.sql.rowset.serial.SerialClob(str.toCharArray());
				return c;
            } catch (Exception e) {
                return null;
            }
        }
	} 
}