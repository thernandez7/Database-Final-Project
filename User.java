public class User
{
	public String username;
	public String password;
	public String firstname;
	public String lastname;
	
	public User()
	{
		username=null;
		password=null;
		firstname= null;
		lastname=null;
	}
	
	public User(String username, String password, String firstname,  String lastname)
	{
		this.username= username;
		this.password= password;
		this.firstname= firstname;
		this.lastname=lastname;
	}
	
	/* getters and setters for the attributes */
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String firstname) {
		this.firstname = firstname;
	}

	public String getLastName() {
		return lastname;
	}

	public void setLastName(String lastname) {
		this.lastname = lastname;
	}

	public String toString(){
		return (username + " " + password + " " +firstname + " " + lastname);
	}
}