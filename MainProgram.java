import java.util.ArrayList;
import java.util.Scanner;

public class MainProgram 
{
	/////////////////////////////////////////////////////////////
	// SELECT EXAMPLE
	/////////////////////////////////////////////////////////////
	public ArrayList<Object> selectExample()
	{
		UserDao dao = new UserDao();
		
		ArrayList<Object> list = dao.selectAll();
		return list;
	}

	public void selectExample(String username)//prints only one user with specified username
	{
		UserDao dao = new UserDao();
		
		ArrayList<Object> list = dao.selectAll();
		
		for (int i=0; i<list.size(); i++)
			if(((User)list.get(i)).username.equals(username))
				System.out.println((User)list.get(i));
	}

	/////////////////////////////////////////////////////////////
	// INSERT EXAMPLE
	/////////////////////////////////////////////////////////////
	public void insertExample(String username, String password, String firstname,  String lastname)
	{
		User user = new User(username,password,firstname, lastname);
		UserDao dao = new UserDao();
		dao.insert(user);
	}

	/////////////////////////////////////////////////////////////
	// UPDATE EXAMPLE
	/////////////////////////////////////////////////////////////
	public void updateExample(User user,String oldusername)
	{
		UserDao dao = new UserDao();
		dao.update(user,oldusername);
	}

	/////////////////////////////////////////////////////////////
	// DELETE EXAMPLE
	/////////////////////////////////////////////////////////////
	public void deleteExample(String username)
	{
		UserDao dao = new UserDao();
		User user = dao.selectByUsername(username);
		dao.delete(user);
	}

	public static void main(String [] args)
	{
		Scanner scan= new Scanner(System.in); Scanner scan1= new Scanner(System.in);

		MainProgram dm = new MainProgram();
		System.out.println();

		ArrayList<Object> l= dm.selectExample();
		for (int i=0; i<l.size(); i++)
			System.out.println((User) l.get(i));//prints all users

		System.out.println("Welcome!");
		while(true)
		{
		System.out.println("Please enter an option below (1,2,3): ");
		System.out.println("1.) Add Admin: Register Now");
		System.out.println("2.) Admin Users: Login");
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
				System.out.println("Enter your first name: ");
				String firstname= scan.nextLine();
				System.out.println("Enter your last name: ");
				String lastname= scan.nextLine();
				
				dm.insertExample(username,password,firstname,lastname);
				System.out.println("You have been added to the system!");
				System.out.println("");
				break;

			case 2: //Admin user-- login 
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

					ArrayList<Object> list= dm.selectExample();

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
						System.out.println("That username and password combination is not in the Admin system.");
					else
						break;
				}

				//have menu to perform operations
				if (found)
				{
					System.out.println("Welcome Back Admin! ");
					while(true)
					{
					System.out.println("Please enter an option below (1,2,3): ");
					System.out.println("1.) View certain user's information");
					System.out.println("2.) Modify registration info of a user");
					System.out.println("3.) Delete an admin acct");
					System.out.println("4.) Exit");
					int ch= scan.nextInt();
					scan.nextLine();//eat up extra 
					System.out.println("");

					switch(ch)//choices for admin user
					{
						case 1: //view user info
							dm.selectExample(use); //PRINTS the admin logged in
							break;
						case 2: //modify register info 
							//get user object 
							ArrayList<Object> users= dm.selectExample();
							User myuser= new User();
							for(int n=0; n<users.size(); n++)//check if valid match
							{
								User user= (User)users.get(n);
								if (user.getUsername().equals(use))//get username specified
								{
									myuser= user;
									break;
								}
							}
							String oldacctname = myuser.username;

							System.out.println("What attribute would you like to update (username, password, first name, last name): ");
							String attr= scan.nextLine();
							System.out.println("Enter new value of "+ attr+ ": ");
							String newvalue= scan.nextLine();

							if (attr.equalsIgnoreCase("username")){
								myuser.username= newvalue;
								use= newvalue;}
							else if (attr.equalsIgnoreCase("password"))
								myuser.password= newvalue;
							else if (attr.equalsIgnoreCase("first name"))
								myuser.firstname= newvalue;
							else if (attr.equalsIgnoreCase("last name"))
								myuser.lastname= newvalue;
							else 
								System.out.print("Valid attribute not specified ");
							
							dm.updateExample(myuser, oldacctname);
							System.out.print("Update Successful! New Info: ");
							dm.selectExample(myuser.username);// prints user new info
							break;
						case 3: 
							System.out.println("Enter username of user to delete");
							String user2= scan.nextLine();
							dm.deleteExample(user2);
							System.out.println("The account has been removed!");
							break;

						case 4: //Exit
							System.exit(0);
							break;
					}
					}//end while true
				}
				break;
			case 3: //exit

				System.exit(0);
				break;
		}
		}//end while true
	}
}
