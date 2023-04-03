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
                if (foundUrl.text.position(queryIn, 1) > -1) {
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
        if (words.length == 1) {
            return searchSingle(queryIn);
        }
        ArrayList<Url> results = new ArrayList<Url>();
        ArrayList<ArrayList<Url>> queryResults = new ArrayList<ArrayList<Url>>();
        for (String entry: words) {
            queryResults.add(searchSingle(entry));
        }

        // Add results of individual word searches to results, from most searched words found to least
        for (int i = words.length; i > 0; i--) {
            for (Url foundUrl: queryResults.get(0)) {
                int timesFound = 1;
                for (ArrayList<Url> list: queryResults) {
                    if (list.indexOf(foundUrl) != -1) {
                        timesFound += 1;
                    }
                }
                if (timesFound == i && results.indexOf(foundUrl) == -1) {
                    results.add(foundUrl);
                    for (ArrayList<Url> list: queryResults) {
                        list.remove(foundUrl);
                    }
                }
            }
        }   

        return results;
    }
}
