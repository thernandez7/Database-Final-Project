import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;  //for clob
import java.io.*; 

public class MainProgram 
{
	public ArrayList<Object> SelectUser()
	{
		UserDao dao = new UserDao();
		
		ArrayList<Object> list = dao.selectAll(); 
		return list;
	}


	public void SelectUser(String username)//prints only one user with specified username
	{
		UserDao dao = new UserDao();
		
		ArrayList<Object> list = dao.selectAll();
		
		for (int i=0; i<list.size(); i++)
			if(((User)list.get(i)).username.equals(username))
				System.out.println((User)list.get(i));
	}


	public void insertUser(String username, String password, String name,  String admin)
	{
		User user = new User(username,password,name,admin);
		UserDao dao = new UserDao();
		dao.insert(user);
	}


	public void updateUser(User user,String oldusername)
	{
		UserDao dao = new UserDao();
		dao.update(user,oldusername);
	}


	public void deleteUser(String username)
	{
		UserDao dao = new UserDao();
		User user = dao.selectByUsername(username);
		dao.delete(user);
	}

//----------------------------------end of user methods---------------beginning of URL methods--------------------------------

	public ArrayList<Url> SelectUrls()
	{
		UrlDao dao = new UrlDao();
		
		ArrayList<Url> list = dao.selectAll(); 
		return list;
	}

	public String SelectUrls(String urlLink)//prints only one link with specified link
	{
		// System.out.println("URL: " + urlLink);
		UrlDao dao = new UrlDao();

		if(dao.selectByUrlLink(urlLink) != null) {
			String ret = dao.selectByUrlLink(urlLink).getUrlLink();
			// System.out.println("Return: " + ret);
			return ret;
		} else {
			// System.out.println("No URL found");
			return "";
		}
	}

	public void insertUrl(String urlLink, String ptitle, Clob text, String startingUrl, int crawlNum)
	{
		Url url = new Url(urlLink,ptitle,text,startingUrl,crawlNum);
		UrlDao dao = new UrlDao();
		dao.insert(url);
	}

	public void deleteUrl(String urlLink)
	{
		UrlDao dao = new UrlDao();
		Url url = dao.selectByUrlLink(urlLink);
		dao.delete(url);
	}

//----------------------------------end of user methods---------------beginning of query methods--------------------------------

	public ArrayList<Object> selectQuery() {
		QueryDao qDao = new QueryDao();
		return qDao.selectAll();
	}

	public String selectQuery(String queryIn) {
		QueryDao qDao = new QueryDao();
		return qDao.selectByQuery(queryIn).toString();
	}

	public void insertQuery(String username, String queryString, String topUrl) {
		QueryDao qDao = new QueryDao();
		qDao.insert(new Query(username, queryString, topUrl));
	}

	public void deleteQuery(String queryIn) {
		QueryDao qDao = new QueryDao();
		qDao.delete(qDao.selectByQuery(queryIn));
	}

//---------------------------------end of query methods----------------- beginning of webcrawl methods------------------------------

	public ArrayList<Object> SelectWebcrawl()
	{
		WebcrawlDao dao = new WebcrawlDao();
		
		ArrayList<Object> list = dao.selectAll(); 
		return list;
	}

	public String SelectWebcrawl(int crawlNum)//prints only one link with specified link
	{
		WebcrawlDao dao = new WebcrawlDao();
		
		ArrayList<Object> list = dao.selectAll();
		return dao.selectByCrawlNum(crawlNum).toString();
	}

	public int SelectMaxWebcrawl()//prints only one link with specified link
	{
		WebcrawlDao dao = new WebcrawlDao();
		Webcrawl w= dao.selectMaxCrawlNum();
		if (w != null)
			return dao.selectMaxCrawlNum().getCrawlNum();
		else//max crawl is null
			return 0;//first crawl
	}

	public void insertWebcrawl(String timeRun, String username, int crawlNum)
	{
		Webcrawl webcrawl = new Webcrawl(timeRun,username,crawlNum);
		WebcrawlDao dao = new WebcrawlDao();
		dao.insert(webcrawl);
	}

	public void deleteWebcrawl(int crawlNum)
	{
		WebcrawlDao dao = new WebcrawlDao();
		Webcrawl webcrawl = dao.selectByCrawlNum(crawlNum);
		dao.delete(webcrawl);
	}

//---------------------------------end of webcrawl methods----------------- beginning of main method--------------------------------
	
	public static void main(String [] args) throws IOException
	{
		Scanner scan= new Scanner(System.in); Scanner scan1= new Scanner(System.in);

		MainProgram dm = new MainProgram();
		// ArrayList<Object> l= dm.SelectUser();
		// for (int i=0; i<l.size(); i++)
		// 	System.out.println((User) l.get(i));//prints all users

		System.out.println("Welcome!");
		while(true)
		{
		System.out.println("Please enter an option below (1,2,3): ");
		System.out.println("1.) Add User: Register Now");
		System.out.println("2.) Users: Login");
		System.out.println("3.) Exit");
		int choice= scan.nextInt();
		System.out.println("");

		switch(choice)
		{
			case 1: //Unregistered user -- register
				System.out.println("\n\nWelcome New User! \nPlease provide the following information to create your account: ");
				System.out.println("Enter Username: ");
				scan.nextLine();
				String username= scan.nextLine();
				System.out.println("Enter Password: ");
				String password= scan.nextLine();
				System.out.println("Enter your name: ");
				String name= scan.nextLine();
				System.out.println("Are you an Admin?(Yes or No): ");
				String admin= scan.nextLine();
				
				dm.insertUser(username,password,name,admin);
				System.out.println("You have been added to the system!");
				System.out.println("");
				break;

			case 2: // user-- login 
				boolean found= false;
				String use="";
				System.out.println("You have 3 chances to log in");
				scan.nextLine();//eat up extra
				for(int i=0; i<3; i++)//3 chances to login
				{
					System.out.println("Enter your username: ");
					use= scan.nextLine();
					System.out.println("Enter your password: ");
					String ps= scan1.nextLine();

					ArrayList<Object> list= dm.SelectUser();

					for (int j=0; j<list.size(); j++) //check if valid match
					{
						User user= (User)list.get(j);
						if (user.getUsername().equals(use) && user.getPassword().equals(ps))
						{
							System.out.print("Login successful!");
							found=true;
							break;
						}
					}
					if (!found)
						System.out.println("That username and password combination is not in the system.");
					else
						break;
				}

				//have menu to perform operations
				if (found)
				{
					ArrayList<Object> userlist= dm.SelectUser();
					User u = new User("","","","");
					for (int j=0; j<userlist.size(); j++) //loop through users
					{
						User user= (User)userlist.get(j);
						if (user.getUsername().equals(use))
						{
							u=(User)userlist.get(j);
							break;
						}
					}

					//ADMIN CAPABILITIES
					if (u.getAdmin().equalsIgnoreCase("Yes"))//if admin user
					{
						System.out.println("\n\nWelcome Back Admin User! ");
						while(true)
						{
						System.out.println("Please enter an option below: ");
						System.out.println("1.) Run webcrawl");
						System.out.println("2.) Do a search");
						System.out.println("3.) View all user's information");
						System.out.println("4.) View a users previous queries");
						System.out.println("5.) View previous webcrawls");
						System.out.println("6.) View all URLS");
						System.out.println("7.) Modify info of a user");
						System.out.println("8.) Delete a user");
						System.out.println("9.) Exit");
						int ch= scan.nextInt();
						scan.nextLine();//eat up extra 
						System.out.println("");

						switch(ch)//choices for admin user
						{
							case 1://run webcrawl
								Scraper scraper= new Scraper();
								System.out.println("Enter in a starting Url: ");
								String starturl= scan.nextLine();
								System.out.println("Enter the date (MM/DD/YY): ");
								String date= scan.nextLine();
								System.out.println("Enter the time (HH:MM(am/pm)): ");
								String time= scan.nextLine();
								String datetime= date+ " "+ time;

								int crawlNumber=dm.SelectMaxWebcrawl()+1;//keep track of number of crawls for webcrawl
								System.out.println();

								//insert webcrawl into the db webcrawl table
								dm.insertWebcrawl(datetime, u.getUsername(), crawlNumber);
								
								//call the webcrawl to begin 
								scraper.webCrawler(starturl, starturl, crawlNumber);//first is changing, second is the inital page
								
								crawlNumber++;
								break;
							case 2://do a search- admin
								System.out.println("Enter in your query: ");
								String queryIn= scan.nextLine();

								//call search--store topurl for insert
								Search search= new Search();
								Url topurl;
								ArrayList<Url> searchResults=  search.searchPhrase(queryIn);
								if (searchResults.size() != 0)//if search didnt return nothing
								{
									topurl=searchResults.get(0);//get first result
									dm.insertQuery(u.getUsername(),queryIn,topurl.urlLink);//insert this query into the db Query table
								}
								else {
									dm.insertQuery(u.getUsername(),queryIn, null);//insert this query into the db Query table
								}
								//print out results for user to view 
								System.out.println("\nSearch Results:");
								for (int i=0; i<searchResults.size(); i++)
								{
									System.out.println(searchResults.get(i).getPtitle());//print ptitle
									System.out.println("\t"+searchResults.get(i).getUrlLink());//print the Url Link
									System.out.println("");//have space between each result
								}
								break;
							case 3: //view user info
								ArrayList<Object> userlist1= dm.SelectUser();
								for (int i=0; i<userlist1.size(); i++)
									System.out.println((User) userlist1.get(i));
								break;
							case 4://View a users previous queries
								System.out.println("Enter username of account to view queries of: ");
								String acctname=scan.nextLine();

								//get Query object 
								ArrayList<Object> queries= dm.selectQuery();
								for(int n=0; n<queries.size(); n++)//check if valid match
								{
									Query query= (Query)queries.get(n);
									if (query.getUsername().equals(acctname))//get username specified
									{
										System.out.println(query);
									}
								}
								break;
							case 5: //view all previous webcrawls
								ArrayList<Object> allcrawls= dm.SelectWebcrawl();
								for (int i=0; i<allcrawls.size(); i++)
									System.out.println((Webcrawl) allcrawls.get(i));//print all webcrawls							
								break;

							case 6: //view all URL's
								ArrayList<Url> allURLS= dm.SelectUrls();
								for (int i=0; i<allURLS.size(); i++)
									System.out.println(allURLS.get(i).getUrlLink() );//print all webcrawls							
								break;	

							case 7: //modify register info of a user
								System.out.println("Enter username of account to modify: ");
								String acct=scan.nextLine();

								//get user object 
								ArrayList<Object> users= dm.SelectUser();
								User myuser= new User();
								for(int n=0; n<users.size(); n++)//check if valid match
								{
									User user= (User)users.get(n);
									if (user.getUsername().equals(acct))//get username specified
									{
										myuser= user;
										break;
									}
								}
								String oldacctname = myuser.username;

								System.out.println("What attribute would you like to update (username, password, name, admin): ");
								String attr= scan.nextLine();
								System.out.println("Enter new value of "+ attr+ ": ");
								String newvalue= scan.nextLine();

								if (attr.equalsIgnoreCase("username")){
									myuser.username= newvalue;
									use= newvalue;}
								else if (attr.equalsIgnoreCase("password"))
									myuser.password= newvalue;
								else if (attr.equalsIgnoreCase("name"))
									myuser.name= newvalue;
								else if (attr.equalsIgnoreCase("admin"))
									myuser.admin= newvalue;
								else 
									System.out.print("Valid attribute not specified ");
								
								dm.updateUser(myuser, oldacctname);
								System.out.print("Update Successful! New Info: ");
								dm.SelectUser(myuser.username);// prints user new info
								break;
							case 8: 
								System.out.println("Enter username of user to delete");
								String user2= scan.nextLine();

								dm.deleteUser(user2);
								System.out.println("The account has been removed!");
								if (user2.equals(u.getUsername()))
									System.exit(0);//deleted own account so end program
								break;
							case 9: //Exit
								System.exit(0);
								break;
						}
						System.out.println("");
						}//end while true
					}
					else //NORMAL USER-- NOT A ADMIN
					{
						System.out.println("\n\nWelcome Regular User! ");
						while(true)
						{
						System.out.println("Please enter an option below: ");
						System.out.println("1.) Do a Search");
						System.out.println("2.) View your information");
						System.out.println("3.) View your previous queries");
						System.out.println("4.) Modify your user info");
						System.out.println("5.) Delete your user");
						System.out.println("6.) Exit");
						int ch= scan.nextInt();
						scan.nextLine();//eat up extra 
						System.out.println("");

						switch(ch)//choices for normal user
						{
							case 1://do a search
								System.out.println("Enter in your query: ");
								String queryIn= scan.nextLine();

								//call search--store topurl for insert
								Search search= new Search();
								Url topurl;
								ArrayList<Url> searchResults=  search.searchPhrase(queryIn);
								if (searchResults.size() != 0)//if search didnt return nothing
								{
									topurl=searchResults.get(0);//get first result
									dm.insertQuery(u.getUsername(),queryIn,topurl.urlLink);//insert this query into the db Query table
								}
								else
									dm.insertQuery(u.getUsername(),queryIn, null);//insert this query into the db Query table

								//print out results for user to view 
								System.out.println("\nSearch Results");
								for (int i=0; i<searchResults.size(); i++)
								{
									System.out.println(searchResults.get(i).getPtitle());//print ptitle
									System.out.println("\t"+searchResults.get(i).getUrlLink());//print the Url Link
									System.out.println("");//have space between each result
								}
								break;
							case 2: //view your user info
								dm.SelectUser(u.getUsername()); //PRINTS the user logged in
								break;
							case 3: //view your previous queries
								//get Query object 
								ArrayList<Object> queries= dm.selectQuery();
								Query myq= new Query();
								for(int n=0; n<queries.size(); n++)//check if valid match
								{
									Query query= (Query)queries.get(n);
									if (query.getUsername().equals(u.getUsername()))//get username specified
									{
										myq= query;//holds user we want to view queries of
										System.out.println((Query)myq);
									}
								}
								break;
							case 4: //modify your registration info 
								//get user object 
								ArrayList<Object> users= dm.SelectUser();
								User myuser= new User();
								for(int n=0; n<users.size(); n++)//check if valid match
								{
									User user= (User)users.get(n);
									if (user.getUsername().equals(u.getUsername()))
									{
										myuser= user;
										break;
									}
								}
								String oldacctname = myuser.username;

								System.out.println("What attribute would you like to update (username, password, name, admin): ");
								String attr= scan.nextLine();
								System.out.println("Enter new value of "+ attr+ ": ");
								String newvalue= scan.nextLine();

								if (attr.equalsIgnoreCase("username")){
									myuser.username= newvalue;
									use= newvalue;
									u.setUsername(newvalue);}
								else if (attr.equalsIgnoreCase("password"))
									myuser.password= newvalue;
								else if (attr.equalsIgnoreCase("name"))
									myuser.name= newvalue;
								else if (attr.equalsIgnoreCase("admin"))
									myuser.admin= newvalue;
								else 
									System.out.print("Valid attribute not specified ");
								
								dm.updateUser(myuser, oldacctname);
								System.out.print("Update Successful! New Info: ");
								dm.SelectUser(myuser.username);// prints user new info
								break;
							case 5: //delete your acct
								//can"t delete acct until clear all associating info from other tables
								dm.deleteUser(u.getUsername());
								System.out.println("Your account has been removed!\nGoodbye!");
								System.exit(0);//end program
								break;
							case 6: //Exit
								System.exit(0);
								break;
						}
						System.out.println("");
						}//end while true
					}
				}
				break;
			case 3: //exit
				System.exit(0);
				break;
		}
		}//end while true
	}
}
