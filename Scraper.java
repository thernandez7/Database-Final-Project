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

	public String textScraper(String url) throws IOException 
	{
		Document doc= null;
		try{
			doc = Jsoup.connect(url).timeout(6000).get(); // http://web.stonehill.edu/compsci/ComputerScienceCourses.htm
		}catch(Exception e)
		{
			//System.out.println("Faulty URL in scraper");
		}
		String total = "";
		if (doc !=null)//avoid null pointer
		{
			Elements body = doc.select("tbody");
			for(Element e : body.select("tr"))
			{
				String text = e.text(); 
				total += text + " ";
			}
		}
		return total;
	}

	public String titleFinder(String url) throws IOException 
	{
		Document doc= null;
		try{
			doc = Jsoup.connect(url).timeout(6000).get(); // http://web.stonehill.edu/compsci/ComputerScienceCourses.htm
		}catch(Exception e)
		{
			//System.out.println("Faulty URL");
		}
		String title = "Untitled Page"; //will be default title
		if (doc!=null)//avoid null pointer
		{
			Elements body = doc.select("head");
			for(Element e : body.select("title")) 
				title = e.text(); 
		}
		// System.out.println("Title - " + title);
		
		return title;//will return title or defualt if null
	}

	public void webCrawler(String url, String homepage, int crawlNum) throws IOException 
	{
		if(checkURL(url)) {
			UrlDao uDao = new UrlDao();
			// call titleFinder and set ptitle equal to result
			String ptitle = titleFinder(url);
			if (ptitle == "" || ptitle == null)//if null ptitle, change to default value
				ptitle = "Untitled Page";

			// Insert into DB
			if (homepage.equals(url))//this is the starting url
				uDao.insert(new Url(url,ptitle,stringToClob(textScraper(url)),"Yes",crawlNum));//a starting url
			else 
				uDao.insert(new Url(url,ptitle,stringToClob(textScraper(url)),"No",crawlNum));//not a starting url

			Document doc= null;
			try{
				doc = Jsoup.connect(url).timeout(6000).get(); // http://web.stonehill.edu/compsci/ComputerScienceCourses.htm
			}
			catch(Exception e)
			{
				//System.out.println("Faulty link in crawl");
			}

			// String homepage = "http://web.stonehill.edu/compsci/";
			if (doc!= null)//avoid null pointer
			{
				Elements body = doc.select("tbody");
				System.out.println(url + " --- " + body.select("tr").size());
				//int i = 0;
				for(Element e : body.select("tr")) 
				{
					String addition = e.select("a").attr("href");
					if(addition.contains(":") || addition.contains(" ")) { continue; }
					String urls = homepage + addition;
					webCrawler(urls, homepage, crawlNum);//will only call from working urls
				}
			}
		}
	}

	public boolean checkURL(String url) {
		MainProgram mp = new MainProgram();
		// System.out.println("run selectURL");
		String check = mp.SelectUrl(url);
		// System.out.println("-------" + check);
		if(check != "") // Already in DB
			return false; 
		return true; // Not in DB
	}
	
	public Clob stringToClob(String str) {  // From java2s.com  - converting a string to Clob
        if (str.equals(null)) {
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