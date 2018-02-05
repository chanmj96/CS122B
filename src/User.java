/*
 * This User class only has the username field in this example.
 * 
 * However, in the real project, this User class can contain many more things,
 * for example, the user's shopping cart items.
 * 
 */
public class User {
	
	private final String email;
	private boolean access;
	
	public User(String email) {
		this.email = email;
		access = false;
	}
	
	public boolean hasAccess() {
		return access;
	}
	public void setAccess(boolean b) {
		access = b;
	}
	public String getEmail() {
		return this.email;
	}

}
