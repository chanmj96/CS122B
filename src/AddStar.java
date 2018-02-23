

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;
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
	private Connection dbcon;
	private Statement stmt;
	private ResultSet rs;
       
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
            String name = request.getParameter("name");
            String birth = request.getParameter("dob");
            if(birth.equals("")) {birth = null;};
            if(birth != null && !isInteger(birth)) {
            		System.out.println("Error: invalid input for birthday.");
            		out.write("false");
				out.close();
				return;
            }
    			
    			if(name == null || name.equals("")) {
    				System.out.println("Error: star name field not filled out.");
    				out.write("false");
    				out.close();
    				return;
    			}
    			
    			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
    			dbcon = DriverManager.getConnection(loginUrl, loginUser, loginPasswd);
    			stmt = dbcon.createStatement();
    			
    			CallableStatement cStmt = dbcon.prepareCall("{call add_star(?, ?)}");
    			
    			
    			cStmt.setString(1, name);
    			if(birth == null) {
    				cStmt.setNull(2,  java.sql.Types.INTEGER);
    			} else {
    				cStmt.setInt(2,  Integer.parseInt(birth));
<<<<<<< HEAD
    			}   			
    			cStmt.executeUpdate();
    					
=======
    			}
    			cStmt.registerOutParameter(3, java.sql.Types.INTEGER);    			
    			cStmt.executeUpdate();
    			
    			if(cStmt.getInt(3) > 0) {
    				out.write("true");
    			} else {
    				out.write("false");
    			}
>>>>>>> SamTesting
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
	
	public static boolean isInteger(String str) {
	    if (str == null) {
	        return false;
	    }
	    int length = str.length();
	    if (length == 0) {
	        return false;
	    }
	    int i = 0;
	    if (str.charAt(0) == '-') {
	        if (length == 1) {
	            return false;
	        }
	        i = 1;
	    }
	    for (; i < length; i++) {
	        char c = str.charAt(i);
	        if (c < '0' || c > '9') {
	            return false;
	        }
	    }
	    return true;
	}
}
