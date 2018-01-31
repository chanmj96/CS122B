import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Servlet implementation class MovieList
 */
@WebServlet("/MovieList")
public class MovieList extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MovieList() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String loginUser = "root"; // Change to 'sam' for AWS
		String loginPasswd = "klefstad";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
		response.setContentType("text/html");
		
		PrintWriter out = response.getWriter();
		out.println("<html>\n" + 
					"<head>\n" + 
					"	<meta charset=\"UTF-8\">\n" + 
					"	<link rel=\"stylesheet\" href=\"style.css\">\n" + 
					"	<title>MovieJunkieWebsite</title>\n" + 
					"</head>\n" + 
					"<h1>MovieJunkie</h1>" +
					"<h2>Top 20 Movies</h2>" +
					"<body>");
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			Statement statement = dbcon.createStatement();
			
			String query = "SELECT m.*, r.rating "
					+ "FROM ratings r "
					+ "INNER JOIN movies m ON m.id = r.movieId "
					+ "ORDER BY r.rating DESC "
					+ "LIMIT 20 ";
					
			ResultSet rs = statement.executeQuery(query);
			out.println("<TABLE border>");
	

			out.println("<tr>" +
						"<td>" + "ID" + "</td>" +
						"<td>" + "Title" + "</td>" +
						"<td>" + "Year" + "</td>" +
						"<td>" + "Director" + "</td>" +
						"<td>" + "Genres" + "</td>" +
						"<td>" + "Starring" + "</td>" +
						"<td>" + "Rating" + "</td>" +
					  	"</tr>");
			
			while(rs.next()) {
				String movie_id = rs.getString("id");
				String movie_title = rs.getString("title");
				String movie_year = rs.getString("year");
				String movie_director = rs.getString("director");
				String movie_rating = rs.getString("rating");
				
				out.println("<tr>" +
                        "<td>" + movie_id + "</td>" +
						"<td>" + movie_title + "</td>" +
						"<td>" + movie_year + "</td>" +
						"<td>" + movie_director + "</td>" +
						"<td>"
						);
				
				Statement statement2 = dbcon.createStatement();
				String query_genre = "Select g.name "
								+ "FROM genres g "
								+ "INNER JOIN (SELECT gs.genreId FROM genres_in_movies gs WHERE gs.movieId=\"" + movie_id + "\") "
								+ " gs ON g.id=gs.genreId";
				ResultSet get_genres = statement2.executeQuery(query_genre);
				while(get_genres.next()) {
					String genre = get_genres.getString("name");
					out.println(genre + "<br/>");
				}
				out.println("</td><td>");
				
				
				Statement statement3 = dbcon.createStatement();
				String query_stars = "SELECT s.name "
								+ "FROM stars s " 
								+ "INNER JOIN (SELECT sim.* FROM stars_in_movies sim WHERE sim.movieId=\"" + movie_id + "\") "
								+ " sim ON s.id=sim.starId ";
				ResultSet get_stars = statement3.executeQuery(query_stars);
				while(get_stars.next()) {
					String star = get_stars.getString("name");
					out.println(star + "<br/>");
				}
				
				out.println("</td>" + "<td>" + movie_rating + "</td>" + "</tr>");
			}
			
			while(rs.next()) {
				String title = rs.getString("title");
				out.println("<tr>" +
				"<td>" + title + "</td>");
			}
			
			out.println("</TABLE></body>");
			rs.close();
			statement.close();
			dbcon.close();
			
		}
	
		catch (SQLException ex) {
			while(ex != null) {
				System.out.println("SQL Exception : " + ex.getMessage());
				ex = ex.getNextException();
			}
			out.println("There was an error");
		}
		
		catch(java.lang.Exception ex) {
			out.println(ex);
			ex.printStackTrace();
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
