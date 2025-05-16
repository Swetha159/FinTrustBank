package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws  TaskException {

		String path = request.getPathInfo();
		System.out.println(path.equals("/login"));
		try {
		if (path.equals("/login")) {
		
				handleLogin(request, response);
				if (request.getAttribute("person") != null) {
					response.sendRedirect(request.getContextPath() + "/bank/dashboard");
				} else {
					response.sendRedirect(request.getContextPath() + "/bank/login?error=invalid");
				}
			
		}
		}catch(IOException | SQLException e)
		{
			e.printStackTrace();
			throw new TaskException(e.getMessage(),e);
		}

	}

	public static void handleLogin(HttpServletRequest request, HttpServletResponse response)
			throws TaskException, IOException, SQLException, JSONException{

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
//			String email = request.getParameter("email");
//			String password = request.getParameter("password");
		PersonDAO dao = new PersonDAO();
		Person person = dao.checkCredentials(email, password);
		
		request.setAttribute("person", person);
		session.setAttribute("personId", person.getPersonId());
		
	}

}
