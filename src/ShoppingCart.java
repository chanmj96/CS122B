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

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
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
		String loginUser = "testuser";
		String loginPasswd = "password";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		User user = (User)request.getSession().getAttribute("user");
		
		String action = request.getParameter("action");
		if(action != null && action != "")
		{
			String movieID = request.getParameter("id");
			if(action.equals("add"))
			{
				System.out.println("adding " + movieID);
				user.addCartItem(movieID);
			}
			else if(action.equals("remove"))
			{
				user.removeCartItem(movieID);
			}
			return;
		}
		JsonArray movieArray = new JsonArray();
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            stmt = dbcon.createStatement();
            
            ArrayList<String> movie_list = user.getCart();
            
            String limit = request.getParameter("display");
            String page = request.getParameter("page");
            String order = request.getParameter("sort");
            String sortby = request.getParameter("sortby");
            
            System.out.println(Arrays.toString(movie_list.toArray()));
            for(String mid: movie_list)
            {
				String query = "SELECT * FROM movies m WHERE m.id='" + mid + "'";
				
				if(order != "") {
					query += " ORDER BY " + sortby + " " + order;
				}
				query += " LIMIT " + limit;
				if(Integer.parseInt(page) > 1) {
					query += " OFFSET " + (Integer.parseInt(limit) * (Integer.parseInt(page) - 1));
				}
				
				ResultSet rs = stmt.executeQuery(query);
		
				if(rs.next()) {
					String rid = rs.getString("id");
					String rtitle = rs.getString("title");
					String ryear = rs.getString("year");
					String rdirector = rs.getString("director");
					
					JsonObject movie = new JsonObject();
					movie.addProperty("id",  rid);
					movie.addProperty("title", rtitle);
					movie.addProperty("year",  ryear);
					movie.addProperty("director", rdirector);
					
					movieArray.add(movie);
				}
            }
			out.write(movieArray.toString());
            rs.close();
            stmt.close();
            dbcon.close();
            out.close();
			
		} catch (SQLException ex) {
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
