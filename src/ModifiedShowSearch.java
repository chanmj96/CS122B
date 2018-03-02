

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Servlet implementation class ModifiedShowSearch
 */
@WebServlet("/ModifiedShowSearch")
public class ModifiedShowSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModifiedShowSearch() {
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
            
			JsonArray movieArray = new JsonArray();
            String title = request.getParameter("title");
            
            String limit = request.getParameter("display");
            String page = request.getParameter("page");
            String order = request.getParameter("sort");
            String sortby = request.getParameter("sortby");
            
            if(title == null || title.trim().isEmpty()) {
            		response.getWriter().write(movieArray.toString());
            		return;
            }
            StringTokenizer st = new StringTokenizer(title);
            String qstring = "";
            while(st.hasMoreTokens()) {
            		String word = st.nextToken();
            		if(word.length() <= 3) {
            			qstring += word + "* ";
            		} else {
            			qstring += "+" + word + "* ";
            		}
            }
            
            String stmt = "SELECT m.*, "
					+ "GROUP_CONCAT(DISTINCT g.name) AS genre, "
					+ "GROUP_CONCAT(DISTINCT CONCAT(s.name, ':', starId)) AS cast "
					+ "FROM movies m "
					+ "INNER JOIN stars_in_movies sim ON m.id=sim.movieId "
					+ "INNER JOIN stars s ON s.id=sim.starId "
					+ "INNER JOIN genres_in_movies gim ON m.id=gim.movieId "
					+ "INNER JOIN genres g ON gim.genreId=g.id "
					+ "WHERE MATCH (title) "
					+ "AGAINST ('" + qstring + "' IN BOOLEAN MODE)"
					+ "GROUP BY m.id";
			if(order != null && !order.trim().isEmpty()) {
				stmt += " ORDER BY " + sortby + " " + order;
			}
			stmt += " LIMIT " + limit;

			if(Integer.parseInt(page) > 1) {
				stmt += " OFFSET " + (Integer.parseInt(limit) * (Integer.parseInt(page) - 1));
			}
			ResultSet result = statement.executeQuery(stmt);
							
			while(result.next()) {
				String rid = result.getString("id");
				String rtitle = result.getString("title");
				String ryear = result.getString("year");
				String rdirector = result.getString("director");
				String rname = result.getString("cast");
				String rgenre = result.getString("genre");
				
				JsonArray cast = new JsonArray();
				cast.add(new JsonPrimitive(rname));
				
				JsonArray genres = new JsonArray();
				genres.add(new JsonPrimitive(rgenre));
				
				JsonObject movie = new JsonObject();
				movie.addProperty("id",  rid);
				movie.addProperty("title", rtitle);
				movie.addProperty("year",  ryear);
				movie.addProperty("director", rdirector);
				movie.add("cast",  cast);
				movie.add("genres", genres);
				
				movieArray.add(movie);
				
			}
			out.write(movieArray.toString());
			
            result.close();
            statement.close();
            dbcon.close();
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
