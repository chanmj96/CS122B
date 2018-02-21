import java.util.HashMap;
/*
 * This User class only has the username field in this example.
 * 
 * However, in the real project, this User class can contain many more things,
 * for example, the user's shopping cart items.
 * 
 */
public class User {
	
	private final String email;
	private final int userType;
	private boolean access;
	
	// shopping cart an arraylist of movie id numbers
	private HashMap<String,String> shoppingCart;
	
	
	public User(String email, int isEmployee) {
		this.email = email;
		access = false;
		shoppingCart = new HashMap<String,String>();
		userType = isEmployee;
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
	public boolean isEmployee() {
		return userType == 1;
	}
	public void emptyCart() {
		shoppingCart.clear();
	}
	public HashMap<String,String> getCart() {
		return shoppingCart;
	}
	public void addCartItem(String s,String quantity) {
		String num = shoppingCart.get(s);
		if(num == null)
			shoppingCart.put(s,quantity);
		else
		{
			int i = Integer.parseInt(num) + Integer.parseInt(quantity);
			shoppingCart.replace(s, Integer.toString(i));
		}
	}
	public void changeQuantity(String s, String quantity){
		String num = shoppingCart.get(s);
		//System.out.println("Enter Function Remove");
		//System.out.flush();
		if(num != null && quantity != null)
		{
			//System.out.println(""+num);
			//System.out.println(""+quantity);
			//System.out.flush();
			if(quantity.equals("0"))
				shoppingCart.remove(s);
			else
				shoppingCart.replace(s, quantity);
		}
		else
			System.out.println("Null quantity");
		
	}

}
