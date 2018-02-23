

import java.io.IOException;
import java.io.PrintWriter;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 * Servlet implementation class Employee
 */
@WebServlet("/Employee")
public class Employee extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Employee() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		HttpSession session = request.getSession(false);
		JsonObject json = new JsonObject();
		if(session == null)
		{
			json.addProperty("status", "fail");
			json.addProperty("page", "login");
			return;
		}
		PrintWriter out = response.getWriter();
		User user = (User)request.getSession().getAttribute("user");
		System.out.println("is user employee?: " +user.isEmployee());
		if(!user.isEmployee())
		{
			json.addProperty("status", "fail");
			json.addProperty("page", "index");
		}
		else
			json.addProperty("status", "success");
		out.write(json.toString());
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
