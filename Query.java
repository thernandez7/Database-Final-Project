public class Query {
    public String username;
    public String queryInput;
    public String topURL;

    public Query() {
        username = null;
        queryInput = null;
        topURL = null;
    }

    public Query(String usernamePassed, String queryPassed, String topURLPassed) {
        username = usernamePassed;
        queryInput = queryPassed;
        topURL = topURLPassed;
    }

    public void setUsername(String usernamePassed) {
        username = usernamePassed;
    }

    public void setQuery(String queryPassed) {
        queryInput = queryPassed;        
    }

    public void setTopURL(String urlPassed) {
        topURL = urlPassed;
    }

    public String getUsername() {
        return username;
    }

    public String getQueryInput() {
        return queryInput;
    }

    public String getTopURL() {
        return topURL;
    }

    public String toString() {
        return (username + " " + queryInput + " " + topURL);
    }
}
