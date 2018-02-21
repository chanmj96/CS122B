

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

/**
 * Servlet implementation class AddStar
 */
@WebServlet("/AddStar")
public class AddStar extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddStar() {
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
            
            String id = request.getParameter("id");
            String name = request.getParameter("name");
            String birth = request.getParameter("dob");
            
            System.out.printf("%s -  %s - %s\n", id, name, birth);
            
            System.out.println("Check 1");
            if(id != "" && name != "") {
            		// Check if star already exists
            		System.out.println("Check 2");
            		String check = "SELECT * FROM stars s WHERE s.id=\"" + id + "\"";
            		ResultSet exists = statement.executeQuery(check);
            		if(!exists.next()) {
            			System.out.println("Check 3");
            			// Perform insertion
            			String insert = "";
            			if(birth != "") {
            				insert += "INSERT INTO stars VALUES (\"" 
            						+ id + "\", \"" 
            						+ name + "\", \""
            						+ birth + "\")";
            			} else {
            				insert += "INSERT INTO stars (id, name) VALUES (\""
            						+ id + "\", \""
            						+ name + "\")";
            			}
            			statement.executeUpdate(insert);
            			
            			// Check if new star was added successfully
            			ResultSet added = statement.executeQuery(check);
            			if(!added.next()) {
            				out.write("false");
            			} else {
            				out.write("true");
            			}
            			added.close();
            		} else {
            			out.write("false");
            		}
            		exists.close();
            } else {
            		out.write("false");
            } 
            
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
