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
import com.google.gson.JsonObject;

/**
 * Servlet implementation class ShowSearch
 */
@WebServlet("/ShowSearch")
public class ShowSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowSearch() {
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
			
            if(title != "" || year != "" || director != "" || name != "") {
            		out.println("Check 0: Passed <br/>");
				String query = "SELECT DISTINCT m.title FROM movies m "
						+ "INNER JOIN stars_in_movies sim ON m.id=sim.movieId "
						+ "INNER JOIN stars s ON s.id=sim.starId ";
				
				if(title != "") {
					query += "WHERE title='" + title + "'";
				}
				if(year != "") {
					if(query.contains("WHERE")) {
						query += " AND year='" + year + "'";
					}  else {
						query += "WHERE year='" + year + "'";
					}
				}
				if(director != "") {
					if(query.contains("WHERE")) {
						query += " AND director='" + director + "'";
					}  else {
						query += "WHERE director='" + director + "'";
					}
				}
				if(name != "") {
					if(query.contains("WHERE")) {
						query += " AND name='" + name + "'";
					}  else {
						query += "WHERE name='" + name + "'";
					}
				}
				
				ResultSet result = statement.executeQuery(query);
				
				JsonArray jsonArray = new JsonArray();
				while(result.next()) {
					String rtitle = result.getString("title");
					String ryear = result.getString("year");
					String rdirector = result.getString("director");
					String rname = result.getString("name");
					
					JsonObject obj = new JsonObject();
	                obj.addProperty("title", rtitle);
	                obj.addProperty("year", ryear);
	                obj.addProperty("director", rdirector);
	                obj.addProperty("cast", rname);
	                
	                jsonArray.add(obj);
				}
				
	            
	            out.write(jsonArray.toString());

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
