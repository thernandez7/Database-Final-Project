import java.sql.SQLException;
import java.util.ArrayList;

public class Search {

    public ArrayList<Url> searchPhrase(String queryIn) {
        // Search each word in the phrase individually
        UrlDao uDao = new UrlDao();
        String[] words = queryIn.split(" ");

        // For each word, call the search method to get the URLs that contain it
        // If the URL is not already in the list of results, it is added with a hitcount of 1
        // If the URL is already in the list of results, the number of hits is increased
        ArrayList<SiteHit> results = new ArrayList<SiteHit>();
        for (String entry: words) {
            for (Url foundUrl : uDao.search(entry)) {
                SiteHit found= new SiteHit(foundUrl);//make one object for found site to use
                if (results.indexOf(found) == -1) {
                    results.add(found);
                } else {
                    results.get(results.indexOf(found)).hitCount += 1;
                }
            }
        }

        // Make a new list of the URLs sorted by the number of query words it contains, highest to lowest
        ArrayList<Url> sortedResults = new ArrayList<Url>();
        for (int i = words.length; i > 0; i--) {
            for (SiteHit site : results) {
                if (site.hitCount == i) {
                    sortedResults.add(site.webpage);
                }
            }
        }

        ArrayList<Url> titleSites = new ArrayList<Url>();
        for (Url site : sortedResults) 
	   {
		if (site.ptitle !=null)
		{
            if (site.ptitle.contains(queryIn))
                titleSites.add(site);
		}
        }
        for (Url site : titleSites) {
            sortedResults.remove(site);
            sortedResults.add(0, site);
        }

        return sortedResults;
    }

    public static void main(String[] args) throws SQLException {
        Search s = new Search();
        System.out.println("Results for searching \"science\":");
        for (Url site : s.searchPhrase("science")) {
            System.out.println("Title: " + site.ptitle + "\nText: " + site.text.getSubString(1, (int)site.text.length()) + "\n");
        }

        System.out.println("All sites:");
        UrlDao uDao = new UrlDao();
        for (Url site : uDao.selectAll()) {
            System.out.println(site.text.getSubString(1, (int)site.text.length()) + "\n");
        }
    }
}
