

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Servlet implementation class Count
 */
@WebServlet("/Count")
public class Count extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Count() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String loginUser = "testuser";
		String loginPasswd = "password";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            Statement statement = dbcon.createStatement();
            
            String title = request.getParameter("title");
            String year = request.getParameter("year");
            String director = request.getParameter("director");
            String name = request.getParameter("name");
            String genre = request.getParameter("genre");
            String letter = request.getParameter("letter");
            String limit = request.getParameter("display");
            String offset = request.getParameter("offset");
            String page = request.getParameter("page");
			
            if(title != "" || year != "" || director != "" || name != "" || genre != null || letter != null) {
				String query = "SELECT m.*, "
						+ "GROUP_CONCAT(DISTINCT g.name) AS genre, "
						+ "GROUP_CONCAT(DISTINCT s.name) AS cast "
						+ "FROM movies m "
						+ "INNER JOIN stars_in_movies sim ON m.id=sim.movieId "
						+ "INNER JOIN stars s ON s.id=sim.starId "
						+ "INNER JOIN genres_in_movies gim ON m.id=gim.movieId "
						+ "INNER JOIN genres g ON gim.genreId=g.id ";
				
				if(letter!=null && letter != "" && letter.equals("other"))
					query += "WHERE title REGEXP '^[^a-zA-Z]'";
				else if(letter!=null && letter != "")
					query += "WHERE (lower(title) LIKE '" + letter.toLowerCase() + "%')";
				else if(title != null && title != "") {
					query += "WHERE title LIKE '%" + title + "%'";
				}
				if(year != null && year != "") {
					if(query.contains("WHERE")) {
						query += " AND year LIKE '%" + year + "%'";
					}  else {
						query += "WHERE year LIKE '%" + year + "%'";
					}
				}
				if(director != null && director != "") {
					if(query.contains("WHERE")) {
						query += " AND director LIKE '%" + director + "%'";
					}  else {
						query += "WHERE director LIKE '%" + director + "%'";
					}
				}
				if(name != null && name != "") {
					if(query.contains("WHERE")) {
						query += " AND s.name LIKE '%" + name + "%'";
					}  else {
						query += "WHERE s.name LIKE '%" + name + "%'";
					}
				}

				query += " GROUP BY m.id ";
				
				if(genre != null && genre != "") {
					query = "SELECT * FROM (" + query
						+ ") AS result WHERE genre LIKE '%" + genre + "%'";
				}
				
				
				String extended_query = "SELECT COUNT(*) AS total "
						+ "FROM (" + query + ") AS result "; 
				
				ResultSet result = statement.executeQuery(extended_query);
				result.next();
				out.write(result.getString("total"));
				
	            result.close();
	            statement.close();
	            dbcon.close();
            } 
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
		out.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
