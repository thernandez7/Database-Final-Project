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
                if (foundUrl.text.position(queryIn, 0) > -1) { //checks if exact phrasing of query is found in Clob text
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
        ArrayList<ArrayList<Url>> queryResults = new ArrayList<ArrayList<Url>>();
        for (String entry: words) { //for every word in the query
            queryResults.add(searchSingle(entry)); //add all urls that contain each word individually
        }


        // Automatically add everything containing the full string to results
        ArrayList<Url> results = new ArrayList<Url>(); //will hold final search results
        results.addAll(searchSingle(queryIn));//adds urls that contain the entire phrase


        // Add all urls that were found for all individual words in the query to results
        for (Url foundUrl: queryResults.get(0)) {
            int timesFound = 1; //keep track of how many times a url is found through the single search method
            for (int i = 1; i < queryResults.size(); ++i) {  //for every url found from the single word searches
                if (queryResults.get(i).indexOf(foundUrl) != -1) {
                    timesFound += 1; 
                }
            }
            if (timesFound == queryResults.size() && results.indexOf(foundUrl) == -1) {
                results.add(foundUrl);
            }
        }

        return results;
    }
}
