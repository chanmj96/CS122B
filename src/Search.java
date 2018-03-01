import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String loginUser = "testuser";
		String loginPasswd = "password";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb";
		
		response.setContentType("application/json");
		
		PrintWriter out = response.getWriter();
		try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            Connection dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
            Statement statement = dbcon.createStatement();
            
            String input = request.getParameter("input");
            StringTokenizer st = new StringTokenizer(input);
            String qstring = "";
            while(st.hasMoreTokens()) {
            		qstring += "+" + st.nextToken() + "* ";
            }
            
            String query = ("SELECT id, entry "
            		+ "FROM ft "
            		+ "WHERE MATCH (entry) "
            		+ "AGAINST ('" + qstring + "' IN BOOLEAN MODE);");
            
            ResultSet result = statement.executeQuery(query);
            
            JsonArray ids = new JsonArray();
            while(result.next()) {
            		JsonObject id = new JsonObject();
            		id.addProperty("id",  result.getString("id"));
            		id.addProperty("title", result.getString("entry"));
            		ids.add(id);
            }
            out.write(ids.toString());
            
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
