package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONException;
import org.json.JSONObject;

import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.model.Person;
import com.zoho.training.exceptions.TaskException;


public class LoginHandler implements HttpRequestHandler {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws  TaskException {
		
		//String path = request.getPathInfo();
		//String path = request.getServletPath();
		String path = request.getServletPath();
		 if(path.startsWith("/bank"))
		 {
			path = request.getPathInfo();
		 }
		if (path.equals("/login")) {
			  try {
				  System.out.println("inside get");
				request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
				
			} catch (ServletException | IOException e) {
				System.out.println("error"+e.getMessage());
				e.printStackTrace();
				throw new TaskException(e.getMessage(),e);
			}
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws  TaskException {

		//String path = request.getPathInfo();
		//String path = request.getServletPath();
		String path = request.getServletPath();
		 if(path.startsWith("/bank"))
		 {
			path = request.getPathInfo();
		 }
		System.out.println(path.equals("/login  inside post"));
		try {
		if (path.equals("/login")) {
				handleLogin(request, response);
//				if (request.getAttribute("person") != null) {
//					response.sendRedirect(request.getContextPath() + "/bank/dashboard");
//				} else {
//					response.sendRedirect(request.getContextPath() + "/bank/login?error=invalid");
//				}
//			
		}
		if (path.equals("/logout")) {
			
			handleLogOut(request, response);

	}
		}catch(IOException | SQLException | JSONException | ServletException e)
		{
			e.printStackTrace();
			throw new TaskException(e.getMessage(),e);
		}

	}

	private void handleLogOut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  HttpSession session = request.getSession(false); 
	        if (session != null) {
	            session.invalidate(); 
	        }
	        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
			
	}

	public static void handleLogin(HttpServletRequest request, HttpServletResponse response)
			throws TaskException, IOException, SQLException, JSONException, ServletException{

		StringBuilder sb = new StringBuilder();
	
		BufferedReader reader = request.getReader();
		String line;
		
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		
		JSONObject json = new JSONObject(sb.toString());
		String email = json.getString("email");
		String password = json.getString("password");
		HttpSession session = request.getSession();
	
		PersonDAO dao = new PersonDAO();
		Person person = dao.checkCredentials(email, password);

		request.setAttribute("person", person);
		if(person!=null)
		{
		session.setAttribute("personId", person.getPersonId());
		session.setAttribute("role", person.getRole());
		session.setAttribute("name", person.getName());
		String role = person.getRole();
		   String redirectUrl = "";
		 switch (role.toUpperCase()) {
	        case "CUSTOMER":
	            redirectUrl = request.getContextPath() + "/bank/customer/dashboard";
	            break;
	        case "ADMIN":
	            redirectUrl = request.getContextPath() + "/bank/admin/dashboard";
	            break;
	        case "SUPERADMIN":
	            redirectUrl = request.getContextPath() + "/bank/superadmin/dashboard";
	            break;
	        default:
	        	request.setAttribute("alertMessage", "Invalid Role");
		        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		        break;
	    }
		    response.setContentType("application/json");
		    response.getWriter().write("{\"success\": true, \"redirectUrl\": \"" + redirectUrl + "\"}");
		}
		else
		{
		    request.setAttribute("alertMessage", "Invalid email or password");
			 request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		}
		
	}

}
