import java.util.ArrayList;

public class Search {
    public ArrayList<Url> searchSingle(String queryIn) {
        UrlDao uDao = new UrlDao();
        ArrayList<Object> allURLs = uDao.selectAll();//gets all urls from the db
        ArrayList<Url> results = new ArrayList<Url>();
        
        // Find the starting position of the query in each the CLOB of each URL and add to results if found
        for (Object entry: allURLs) {
            Url foundUrl = (Url) entry;
            try {
                if (foundUrl.text.position(queryIn, 1) > -1) { //checks if exact phrasing of query is found in Clob text
                    results.add(foundUrl); //add the url to results
                }
            }
            catch (Exception e) {
                continue;
            }
        }
        return results;
    }

    public ArrayList<Url> searchPhrase(String queryIn) {
        // Search each word in the phrase individually
        String[] words = queryIn.split(" ");
        if (words.length == 1) {
            return searchSingle(queryIn);
        }

        // For each word, call the singleSearch method to get the URLs that contain it
        // If the URL is not already in the list of results, it is added with a hitcount of 1
        // If the URL is already in the list of results, the number of hits is increased
        ArrayList<SiteHit> results = new ArrayList<SiteHit>();
        for (String entry: words) {
            for (Url foundUrl : searchSingle(entry)) {
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

        return sortedResults;
    }
}
