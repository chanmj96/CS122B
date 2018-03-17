
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.Cookie;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.google.gson.JsonObject;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection dbcon;
	private Statement stmt;
	private ResultSet rs;   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String userType = request.getParameter("userType");
		
		/*
		String loginUser = "testuser"; 
		String loginPasswd = "password";
		String loginUrl = "jdbc:mysql://localhost:3306/moviedb?autoReconnect=true&useSSL=false";
		*/
		
		PrintWriter out = response.getWriter();
		
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        System.out.println("gRecaptchaResponse=" + gRecaptchaResponse);
        boolean valid = VerifyUtils.verify(gRecaptchaResponse);
        if (!valid) {
        		JsonObject ret = new JsonObject();
        	 	ret.addProperty("status", "fail");
			ret.addProperty("message", "Recaptcha WRONG!!!!");
        		out.write(ret.toString());
        		return;
        }
        
		// this example only allows username/password to be test/test
		// in the real project, you should talk to the database to verify username/password
		
		JsonObject responseJsonObject = new JsonObject();
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
			
			
			
			 // Emails should be unique to users as a login requirement.
			 // test with (cc@msn.com, 1111)
			 String query = "";
			 if(userType == null)
			 {
				 request.getSession().setAttribute("user", new User(email, 0));
				 query = "SELECT * from customers c WHERE c.email=\"" + email + "\"";
			 }
			 else
			 {
				 request.getSession().setAttribute("user", new User(email, 1));
				 query = "SELECT * from employees e WHERE e.email=\"" + email + "\"";
			 }
			 rs = stmt.executeQuery(query);
			 
			 if(!rs.next())
			 {
				 responseJsonObject.addProperty("status", "fail");
				 responseJsonObject.addProperty("message", "Email is not recognized!");
				 out.write(responseJsonObject.toString());
			 }
			 else
			 {
				 String db_password = rs.getString("password");
				 if(db_password.equals(password))
				 {
					 responseJsonObject.addProperty("status", "success");
					 responseJsonObject.addProperty("email", email);
					 responseJsonObject.addProperty("message", "success");
					 out.write(responseJsonObject.toString());
					 ((User)request.getSession().getAttribute("user")).setAccess(true);
				 }
				 else
				 { 
					 responseJsonObject.addProperty("status", "fail");
					 responseJsonObject.addProperty("message", "incorrect password");
					 out.write(responseJsonObject.toString());
				 }
			 }
		}
		catch(java.lang.Exception ex)
		{
			ex.printStackTrace();
			responseJsonObject.addProperty("status", "fail");
			responseJsonObject.addProperty("message", "an error occurred");
			out.write(responseJsonObject.toString());
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
