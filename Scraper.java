import java.io.IOException;
import java.util.*;
import java.lang.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

public class Scraper
{
	public static void main(String [] args) throws IOException
	{
		// System.out.println("Scraper Test");
		// Document doc = Jsoup.connect("https://www.imdb.com/chart/top/").timeout(6000).get();
		// Elements body = doc.select("tbody.lister-list");
		// // System.out.println(body.select("tr").size());
		// int i = 0;
		// for(Element e : body.select("tr")) {
		// 	i++;
		// 	String title = e.select("td.posterColumn img").attr("alt");
		// 	System.out.println(i + ". " + title);
		// }

		System.out.println("Scraper Test");
		Document doc = Jsoup.connect("http://web.stonehill.edu/compsci/").timeout(6000).get();
		Elements body = doc.select("td");
		System.out.println(body.select("blockquote").size());
		// int i = 0;
		// for(Element e : body.select("blockquote")) {
		// 	i++;
		// 	String title = e.text(); //e.select("p").attr("alt"); // getPlainText(e);
		// 	System.out.println(i + ". " + title);
		// }
	}
}