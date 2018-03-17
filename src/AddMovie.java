

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

import java.sql.CallableStatement;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class AddMovie
 */
@WebServlet("/AddMovie")
public class AddMovie extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection dbcon;
	private Statement stmt;
	private ResultSet rs;   
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMovie() {
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
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
		*/
		
		response.setContentType("application/json");
		// this example only allows username/password to be test/test
		// in the real project, you should talk to the database to verify username/password
		
		PrintWriter out = response.getWriter();
		try {
			Context initCtx = new InitialContext();
			if(initCtx == null)
				out.println("initCtx is NULL");
			
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if(envCtx == null)
				out.println("envCtx is NULL");
			
			DataSource ds = (DataSource) envCtx.lookup("jdbc/MovieDB");
			
			String title = request.getParameter("title");
			String year = request.getParameter("year");
			String director = request.getParameter("director");
			String star = request.getParameter("star");
			String genre = request.getParameter("genre");
			
			if(title == null || title.equals("") || year == null || year.equals("") || director == null || director.equals(""))
			{
				System.out.println("Error: movie fields not filled out.");
				out.write("false");
				out.close();
				return;
			}
			if(star == null || star.equals(""))
			{
				System.out.println("Error: provide a star name to add movie.");
				out.write("false");
				out.close();
				return;
			}
			if(genre == null || genre.equals(""))
			{
				System.out.println("Error: provide a genre name to add movie.");
				out.write("false");
				out.close();
				return;
			}
			
			/*
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			*/
			if(ds == null)
				out.println("ds is NULL.");
			
			Connection dbcon = ds.getConnection();
			if(dbcon == null)
				out.println("dbcon is NULL.");	
			
			stmt = dbcon.createStatement();
			
			CallableStatement cStmt = dbcon.prepareCall("{call add_movie(?, ?, ?, ?, ?, ?, ?)}");
			cStmt.setString(1, title);
			cStmt.setString(2, year);
			cStmt.setString(3, director);
			cStmt.setString(4, star);
			cStmt.setString(5, genre);
			
			cStmt.registerOutParameter(7,java.sql.Types.INTEGER);
			cStmt.executeUpdate();

			if(cStmt.getInt(7) == 1) {
				out.write("true");
			} else {
				out.write("false");
			}
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
			try {stmt.close();} catch (Exception e) {}
			try {dbcon.close();} catch (Exception e) {}
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
