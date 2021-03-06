import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

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
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		/*
		String loginUser = "testuser";
		String loginPasswd = "password";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		*/
		
		try {
			Context initCtx = new InitialContext();
			if(initCtx == null)
				out.println("initCtx is NULL");
			
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			if(envCtx == null)
				out.println("envCtx is NULL");
			
			DataSource ds = (DataSource) envCtx.lookup("jdbc/MovieDB");
			
			//# Removing direct connections to use pooling
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            //Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
			//Statement statement = dbcon.createStatement();
            
			if(ds == null)
				out.println("ds is NULL.");
			
			Connection dbcon = ds.getConnection();
			if(dbcon == null)
				out.println("dbcon is NULL.");			
            
            JsonArray suggestions = new JsonArray();
            String query = request.getParameter("query");
            if (query == null || query.trim().isEmpty()) {
            		response.getWriter().write(suggestions.toString());
            		return;
            }
            StringTokenizer st = new StringTokenizer(query);
            String qstring = "";
            
            while(st.hasMoreTokens()) {
            		String word = st.nextToken();
            		if(word.length() <= 3) {
            			qstring += word + "* ";
            		} else {
            			qstring += "+" + word + "* ";
            		}
            }
            
			//# Switching to using prepared statements
            PreparedStatement statement = null;
            
            String stmt = ("SELECT id,title "
            		+ "FROM movies "
            		+ "WHERE MATCH (title) "
            		+ "AGAINST ('" + qstring + "' IN BOOLEAN MODE) "
    				+ "UNION SELECT id,name FROM stars "
    				+ "WHERE MATCH (name) "
    				+ "AGAINST ('" + qstring + "' IN BOOLEAN MODE) LIMIT 10;");                        
            
            dbcon.setAutoCommit(false);
            statement = dbcon.prepareStatement(stmt);
            ResultSet result = statement.executeQuery(stmt);
            
            // Populate suggestions with movies and stars
            while(result.next()) {
            		JsonObject entry = new JsonObject();
            		entry.addProperty("value", result.getString("title"));
            		
            		JsonObject info = new JsonObject();
            		if(result.getString("id").substring(0,2).equals("tt")) {
            			info.addProperty("category", "movie");
            		} else {
            			info.addProperty("category", "star");
            		}
            			
            		info.addProperty("id", result.getString("id"));
            		
            		entry.add("data", info);
            		suggestions.add(entry);
            } 
     
            response.getWriter().write(suggestions.toString());
            System.out.println(suggestions.toString());

            
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
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
