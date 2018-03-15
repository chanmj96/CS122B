

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Servlet implementation class ShowMovie
 */
@WebServlet("/ShowMovie")
public class ShowMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowMovie() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*
		String loginUser = "testuser";
		String loginPasswd = "password";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		*/
		
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
			
			Statement statement = dbcon.createStatement();
            
            String id = request.getParameter("id");
            String query = "SELECT m.*, "
				+ "GROUP_CONCAT(DISTINCT g.name) AS genre, "
				+ "GROUP_CONCAT(DISTINCT CONCAT(s.name, ':', starId)) AS cast "
				+ "FROM movies m "
				+ "INNER JOIN stars_in_movies sim ON m.id=sim.movieId "
				+ "INNER JOIN stars s ON s.id=sim.starId "
				+ "INNER JOIN genres_in_movies gim ON m.id=gim.movieId "
				+ "INNER JOIN genres g ON gim.genreId=g.id "
				+ "WHERE m.id = '" + id + "'";
            
			ResultSet result = statement.executeQuery(query);
							
			JsonObject movie = new JsonObject();
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
				
				movie.addProperty("id",  rid);
				movie.addProperty("title", rtitle);
				movie.addProperty("year",  ryear);
				movie.addProperty("director", rdirector);
				movie.add("cast",  cast);
				movie.add("genres", genres);
				
			}
			out.write(movie.toString());
			
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
