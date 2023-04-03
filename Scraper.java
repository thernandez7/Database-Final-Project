import java.util.*;
import java.lang.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import java.sql.*;  //for Clob type
import java.io.*;  

public class Scraper
{
	String[] urlsArr = new String[100]; // global array for urls
	int x = 0;

	public static void main(String [] args) throws IOException
	{
		String homepage = "http://web.stonehill.edu/compsci/";
		Scraper test = new Scraper();
		// System.out.println("Scraper Test");
		// test.textScraper(homepage);
		System.out.println("Crawler Test");
		test.webCrawler(homepage);
	}

	public String textScraper(String url) throws IOException 
	{
		Document doc = Jsoup.connect(url).timeout(6000).get(); // http://web.stonehill.edu/compsci/ComputerScienceCourses.htm
		Elements body = doc.select("tbody");
		System.out.println(url + " - " + body.select("tr").size());
		int i = 0;
		String total = "";
		for(Element e : body.select("tr")) {
			i++;
			String text = e.text(); 
			total += text + " ";
			System.out.println(i + ". " + text);
		}
		return total;
	}

	public void webCrawler(String url) throws IOException 
	{
		if(checkURL(url)) {
			urlsArr[x] = url;
			x++;
			Document doc = Jsoup.connect(url).timeout(6000).get(); // http://web.stonehill.edu/compsci/ComputerScienceCourses.htm
			String homepage = "http://web.stonehill.edu/compsci/";
			Elements body = doc.select("tbody");
			System.out.println(url + " --- " + body.select("tr").size());
			int i = 0;
			for(Element e : body.select("tr")) {
				String addition = e.select("a").attr("href");
				if(addition.contains(":") || addition.contains(" ")) { continue; }
				//i++;
				String urls = homepage + addition;
				//System.out.println(i + ". " + urls);
				webCrawler(urls);
			}
		}
	}

	public boolean checkURL(String url) // will change this to work with database
	{
		for(int i = 0; i < x; i++) {
			if(urlsArr[i].equals(url)) { return false; } // Already in array
		}
		return true; // Not in array
	}
}