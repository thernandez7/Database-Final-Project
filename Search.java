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
        ArrayList<Url> results = new ArrayList<Url>();
        ArrayList<ArrayList<Url>> queryResults = new ArrayList<ArrayList<Url>>();
        for (String entry: words) {
            queryResults.add(searchSingle(entry)); // Add the lists of URLs found for each word to a larger list
        }


        // Add results of individual word searches to results, from most searched words found to least
        for (int i = words.length; i > 0; i--) { // Put the URLs found the most times at the top of the results
            for (Url foundUrl: queryResults.get(0)) {
                int timesFound = 1;
                for (ArrayList<Url> list: queryResults) { // Count the number of times the URL was found in the various words searched
                    if (list.indexOf(foundUrl) != -1) {
                        timesFound += 1;
                    }
                }
                if (timesFound == i) {
                    results.add(foundUrl);
                    for (ArrayList<Url> list: queryResults) {
                        list.remove(foundUrl); // Remove the URL from all lists it was found in
                        if (list.size() == 0) { // Remove the individual list of URLs from queryResults once empty
                            queryResults.remove(list);
                        }
                    }
                }
            }
        }   

        return results;
    }
}
