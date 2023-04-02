public class Webcrawl
{
	public String timeRun;
	public String username;
	public int crawlNum;
	
	public Webcrawl()
	{
		timeRun=null;
		username=null;
		crawlNum=-1;
	}
	
	public Webcrawl(String timeRun, String username, int crawlNum)
	{
		this.timeRun= timeRun;
		this.username= username;
		this.crawlNum= crawlNum;
	}

	public String getTimeRun() {
		return timeRun;
	}

	public void setTimeRun(String timeRun) {
		this.timeRun = timeRun;
	}

	public String setUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getCrawlNum() {
		return crawlNum;
	}

	public void getCrawlNum(int crawlNum) {
		this.crawlNum = crawlNum;
	}

	public String toString(){
		return (timeRun + " " + username + " " +crawlNum );
	}



}