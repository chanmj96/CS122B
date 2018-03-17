import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Arrays;
import java.util.Enumeration;
/**
 * Servlet implementation class ShowSearch
 */
@WebServlet("/ShoppingCart")
public class ShoppingCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection dbcon;
	private Statement stmt;
	private ResultSet rs;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShoppingCart() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*Enumeration<String> enParams = request.getParameterNames(); 
		while(enParams.hasMoreElements()){
		 String paramName = (String)enParams.nextElement();
		 System.out.println("Attribute Name - "+paramName+", Value - "+request.getParameter(paramName));
		}*/
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = request.getSession(false);
		if(session == null)
		{
			httpResponse.sendRedirect("login.html");
			return;
		}
		User user = (User)request.getSession().getAttribute("user");

		String action = request.getParameter("action");
		if(action != null && action != "")
		{
			String movieID = request.getParameter("id");
			String count =   request.getParameter("count");
			if(action.equals("add"))
			{
				//System.out.println("adding " + movieID);
				user.addCartItem(movieID,count);
			}
			else if(action.equals("remove"))
			{
				//System.out.println("removing " + movieID);
				user.changeQuantity(movieID,"0");
			}
			else if(action.equals("update"))
			{
				//System.out.println("removing " + movieID);
				user.changeQuantity(movieID,count);
			}
			return;
		}
		/*
		String loginUser = "testuser";
		String loginPasswd = "password";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		*/
		JsonArray myArray = new JsonArray();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
			//Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            	
			Context initCtx = new InitialContext();
			if(initCtx == null)
				out.println("initCtx is NULL");
			
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if(envCtx == null)
				out.println("envCtx is NULL");
			
			DataSource ds = (DataSource) envCtx.lookup("jdbc/MovieDB");
			
			if(ds == null)
				out.println("ds is NULL.");
			
			Connection dbcon = ds.getConnection();
			if(dbcon == null)
				out.println("dbcon is NULL.");	
			
            HashMap<String,String> movie_list = user.getCart();
            
            //System.out.println(movie_list.toString());
            for(Map.Entry<String,String> entry: movie_list.entrySet())
            {
            		String query = "SELECT * FROM movies m WHERE m.id='"+entry.getKey()+"'";
            		Statement stmt = dbcon.createStatement();
            		ResultSet rs = stmt.executeQuery(query);
            		if(rs.next()) {
	            		JsonObject movie = new JsonObject();
					movie.addProperty("id", entry.getKey());
					movie.addProperty("quantity", entry.getValue());
					movie.addProperty("name", rs.getString("title"));
					myArray.add(movie);
            		}
            		rs.close();
            		stmt.close();
            }
            System.out.println(myArray.toString());
			out.write(myArray.toString());
            
            dbcon.close();
			out.close();
			
		}catch (SQLException ex) {
	            while (ex != null) {
	                System.out.println("SQL Exception:  " + ex.getMessage());
	                ex = ex.getNextException();
	            }
		} catch (java.lang.Exception ex) {
			out.println("<HTML>" + "<HEAD><TITLE>" + "MovieDB: Error" + "</TITLE></HEAD>\n<BODY>"
					+ "<P>SQL error in doGet: " + ex.getMessage() + "</P></BODY></HTML>");
			return;
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
