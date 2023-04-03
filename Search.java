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
        // Put the URLs found the most times at the top of the results
        for (int i = words.length; i > 0; i--) { //for length of words in query
            for (Url foundUrl: queryResults.get(0)) { //for every url from single word searches
                int timesFound = 1;
                for (ArrayList<Url> list: queryResults) { //for each list of URLs in query results
                    if (list.indexOf(foundUrl) != -1) { //if found in that list
                        timesFound += 1;
                    }
                }
                if (timesFound == i) {//i starts at #of words in query and decreases each iteration
                    results.add(foundUrl);//will add to results in order of times found
                    for (ArrayList<Url> list: queryResults) {//for each list
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
