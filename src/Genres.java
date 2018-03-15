

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

/**
 * Servlet implementation class Login
 */
@WebServlet("/Genres")
public class Genres extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection dbcon;
	private Statement stmt;
	private ResultSet rs;   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Genres() {
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
			//Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			//dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			
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
			
			stmt = dbcon.createStatement();
			
			 String query = "SELECT DISTINCT g.name FROM genres g";
			 rs = stmt.executeQuery(query);
			 
			 JsonArray array = new JsonArray();
			 while(rs.next())
			 {
				 String name = rs.getString("name");
				 
				 JsonObject obj = new JsonObject();
				 obj.addProperty("name", name);
				 array.add(obj);
			 }
			 out.write(array.toString());
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
