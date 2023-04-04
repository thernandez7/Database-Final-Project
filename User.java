 public class User
{
	public String username;
	public String password;
	public String name;
	public String admin;
	
	public User()
	{
		username=null;
		password=null;
		name=null;
		admin=null;
	}
	
	public User(String username, String password, String name,  String admin)
	{
		this.username= username;
		this.password= password;
		this.name= name;
		this.admin=admin;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdmin() {
		return admin;
	}

	public void setAdmin(String admin) {
		this.admin = admin;
	}

	public String toString(){
		return (username + " " + password + " " +name + " " + admin);
	}
}