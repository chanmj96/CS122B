

import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class Search
 */
@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
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
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		
		// Stylesheet
		out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">");
		// Div
		out.println("<div id=\"search-output\"");
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			Statement statement = dbcon.createStatement();
			
			String title = request.getParameter("title");
			String year = request.getParameter("year");
			String director = request.getParameter("director");
			String name = request.getParameter("name");
			
			if(title != "" || year != "" || director != "" || name != "") {
				out.println("Check 0: Passed <br/>");
				String query = "SELECT DISTINCT m.title FROM movies m "
						+ "INNER JOIN stars_in_movies sim ON m.id=sim.movieId "
						+ "INNER JOIN stars s ON s.id=sim.starId ";
				
				if(title != "") {
					query += "WHERE title LIKE '%" + title + "%'";
				}
				if(year != "") {
					if(query.contains("WHERE")) {
						query += " AND m.year='" + year + "'";
					}  else {
						query += "WHERE m.year='" + year + "'";
					}
				}
				if(director != "") {
					if(query.contains("WHERE")) {
						query += " AND m.director='" + director + "'";
					}  else {
						query += "WHERE m.director='" + director + "'";
					}
				}
				if(name != "") {
					if(query.contains("WHERE")) {
						query += " AND m.name='" + name + "'";
					}  else {
						query += "WHERE m.name='" + name + "'";
					}
				}
				
				ResultSet result = statement.executeQuery(query);
				while(result.next()) {
					out.println(result.getString("title") + "<br/>");
					out.println("<br/>");
				}
			}
			out.println("</div>");
		}
		catch (SQLException ex) {
			while(ex != null) {
				System.out.println("SQL Exception : " + ex.getMessage());
				ex = ex.getNextException();
			}
		}
		catch(java.lang.Exception ex) {
			out.println("<HTML>" +
            "<HEAD><TITLE>" +
            "MovieDB: Error" +
            "</TITLE></HEAD>\n<BODY>" +
            "<P>SQL error in doGet: " +
            ex.getMessage() + "</P></BODY></HTML>");
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
