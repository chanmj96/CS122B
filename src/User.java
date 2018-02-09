import java.util.ArrayList;
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
	
	// shopping cart an arraylist of movie id numbers
	private ArrayList<String> shoppingCart;
	
	
	public User(String email) {
		this.email = email;
		access = false;
		shoppingCart = new ArrayList<String>();
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
	public void emptyCart() {
		shoppingCart.clear();
	}
	public ArrayList<String> getCart() {
		return shoppingCart;
	}
	public void addCartItem(String s) {
		shoppingCart.add(s);
	}
	public void removeCartItem(String s){
		try { 
			shoppingCart.remove(s);
		}
		catch(Exception e) {}
	}

}
