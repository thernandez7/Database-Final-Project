import java.util.ArrayList;

public class Search {
    public ArrayList<Url> searchSingle(String queryIn) {
        UrlDao uDao = new UrlDao();
        ArrayList<Object> allURLs = uDao.selectAll();
        ArrayList<Url> results = new ArrayList<Url>();
        
        // Find the starting position of the query in each the CLOB of each URL and add to results if found
        for (Object entry: allURLs) {
            Url foundUrl = (Url) entry;
            try {
                if (foundUrl.text.position(queryIn, 0) > -1) {
                    results.add(foundUrl);
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
        ArrayList<ArrayList<Url>> queryResults = new ArrayList<ArrayList<Url>>();
        for (String entry: words) {
            queryResults.add(searchSingle(entry));
        }

        // Automatically add everything containing the full string to results
        ArrayList<Url> results = new ArrayList<Url>();
        results.addAll(searchSingle(queryIn));

        // Add all urls that were found for all individual words in the query to results
        for (Url foundUrl: queryResults.get(0)) {
            int timesFound = 1;
            for (int i = 1; i < queryResults.size(); ++i) {
                if (queryResults.get(i).indexOf(foundUrl) != -1) {
                    timesFound += 1;
                }
            }
            if (timesFound == queryResults.size()) {
                results.add(foundUrl);
            }
        }

        return results;
    }
}
