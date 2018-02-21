

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
/**
 * Servlet implementation class Login
 */
@WebServlet("/Verify")
public class Verify extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection dbcon;
	private Statement stmt;
	private ResultSet rs;   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Verify() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String loginUser = "testuser"; 
		String loginPasswd = "password";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";

		PrintWriter out = response.getWriter();

		response.setContentType("application/json");
		String number = request.getParameter("number");
		String expiration = request.getParameter("expiration");
		String first_name = request.getParameter("first_name");
		String last_name = request.getParameter("last_name");
		
		
		 JsonObject ret = new JsonObject();
		if(number != "" && expiration != "" && expiration != "" && first_name != "" && last_name != "")
		{
			try {
				Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
				dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
				stmt = dbcon.createStatement();
				
				 String customerInfo = "SELECT c.* FROM customers c "
						 + "INNER JOIN creditcards cc ON c.ccId=cc.id "
						 + "WHERE c.ccId='"+number+"' "
						 + "AND cc.expiration='"+expiration+"' "
						 + "AND cc.firstName='"+first_name+"' "
						 + "AND cc.lastName='"+last_name+"'";
						 
				 rs = stmt.executeQuery(customerInfo);
				 
				 if(rs.next())
				 {
					 System.out.println("Verified Customer.");
					 String customerIdString=rs.getString("id");
					 int customerId = Integer.parseInt(customerIdString);
					 ret.addProperty("status", "success");
					 
					 // get current date to add movie sales
					 Calendar calendar = Calendar.getInstance();
					 java.sql.Date timestamp = new java.sql.Date(calendar.getTime().getTime());
					 
					 // add all movie sales to sales table
					 User user = (User)request.getSession().getAttribute("user");
					 HashMap<String,String> movie_list = user.getCart();
					 for(Map.Entry<String, String> entry: movie_list.entrySet())
					 {
						 int quantity = Integer.parseInt(entry.getValue());
						 for(int i=0; i<quantity; i++)
						 {	
							 String insertQuery = "INSERT INTO sales (customerId, movieId, saleDate) "
						 		+ "values (?, ?, ?)";
							 PreparedStatement insertSTMT = dbcon.prepareStatement(insertQuery,Statement.RETURN_GENERATED_KEYS);
							 insertSTMT.setInt(1,customerId);
							 insertSTMT.setString(2, entry.getKey());
							 insertSTMT.setDate(3, timestamp);
							 insertSTMT.executeUpdate();
							 
							 // autoincrement sales id
							 ResultSet tableKeys = insertSTMT.getGeneratedKeys();
							 tableKeys.next();
							 insertSTMT.close();
						 }
					 }
					 System.out.println("successful insert statements");
					 System.out.flush();
				 }
				 else
				 {
					 ret.addProperty("status", "fail");
					 ret.addProperty("message", "User with that information was not found.");
				 }
				 out.write(ret.toString());
			}
			catch (SQLException ex) {
	            while (ex != null) {
	                System.out.println("SQL Exception:  " + ex.getMessage());
	                ex = ex.getNextException();
	            }
			} catch (java.lang.Exception ex) {
				out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
						+ "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
				return;
			}
			finally {
				try {rs.close();} catch (Exception e) {}
				try {stmt.close();} catch (Exception e) {}
				try {dbcon.close();} catch (Exception e) {}
				out.close();
			}
		}
		else
		{
			ret.addProperty("status", "fail");
			ret.addProperty("message", "All fields must be filled out.");
			out.write(ret.toString());
			out.close();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
