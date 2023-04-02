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

	public ArrayList<Object> SelectUrl()
	{
		UrlDao dao = new UrlDao();
		
		ArrayList<Object> list = dao.selectAll(); 
		return list;
	}


	public void SelectUrl(String urlLink)//prints only one link with specified link
	{
		UrlDao dao = new UrlDao();
		
		ArrayList<Object> list = dao.selectAll();
		
		for (int i=0; i<list.size(); i++)
			if(((Url)list.get(i)).urlLink.equals(urlLink))
				System.out.println((Url)list.get(i));
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


//---------------------------------end of URL methods----------------- beginning of main method--------------------------------
	
	public static void main(String [] args)
	{
		Scanner scan= new Scanner(System.in); Scanner scan1= new Scanner(System.in);

		MainProgram dm = new MainProgram();
		System.out.println();

		ArrayList<Object> l= dm.SelectUser();
		for (int i=0; i<l.size(); i++)
			System.out.println((User) l.get(i));//prints all users

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
				System.out.println("Welcome New User! \nPlease provide the following information to create your account: ");
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
						System.out.println("Welcome Back Admin User! ");
						while(true)
						{
						System.out.println("Please enter an option below (1,2,3,4): ");
						System.out.println("1.) View all user's information");
						System.out.println("2.) Modify info of a user");
						System.out.println("3.) Delete a user");
						System.out.println("4.) Exit");
						int ch= scan.nextInt();
						scan.nextLine();//eat up extra 
						System.out.println("");

						switch(ch)//choices for admin user
						{
							case 1: //view user info
								ArrayList<Object> userlist1= dm.SelectUser();
								for (int i=0; i<userlist1.size(); i++)
									System.out.println((User) userlist1.get(i));
								break;
							case 2: //modify register info 
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
							case 3: 
								System.out.println("Enter username of user to delete");
								String user2= scan.nextLine();
								dm.deleteUser(user2);
								System.out.println("The account has been removed!");
								break;
							case 4: //Exit
								System.exit(0);
								break;
						}
						}//end while true
					}
					else //NORMAL USER-- NOT A ADMIN
					{
						System.out.println("Welcome Regular User! ");
						while(true)
						{
						System.out.println("Please enter an option below (1,2,3,4): ");
						System.out.println("1.) View your information");
						System.out.println("2.) Modify your user info");
						System.out.println("3.) Delete your user");
						System.out.println("4.) Exit");
						int ch= scan.nextInt();
						scan.nextLine();//eat up extra 
						System.out.println("");

						switch(ch)//choices for normal user
						{
							case 1: //view user info
								dm.SelectUser(u.getUsername()); //PRINTS the user logged in
								break;
							case 2: //modify register info 
								//get user object 
								ArrayList<Object> users= dm.SelectUser();
								User myuser= new User();
								for(int n=0; n<users.size(); n++)//check if valid match
								{
									User user= (User)users.get(n);
									if (user.getUsername().equals(u.getUsername()))//get username specified
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
							case 3: 
								dm.deleteUser(u.getUsername());
								System.out.println("The account has been removed!");
								break;

							case 4: //Exit
								System.exit(0);
								break;
						}
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
