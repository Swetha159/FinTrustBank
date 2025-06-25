package com.bank.fintrustbank.handler;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.dao.SessionTokenDAO;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.model.SessionToken;
import com.bank.fintrustbank.util.SessionUtil;
import com.zoho.training.exceptions.TaskException;

import io.jsonwebtoken.Claims;

public class LoginHandler implements HttpRequestHandler {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		String path = request.getServletPath();
		try {
		if (path.equals("/login")) {

				System.out.println("inside get");
				request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		}
		 
			} catch (ServletException | IOException e) {
				System.out.println("error" + e.getMessage());
				e.printStackTrace();
				throw new TaskException(e.getMessage(), e);
			}
		}
	

	


	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		String path = request.getServletPath();
		if (path.startsWith("/bank")) {
			path = request.getPathInfo();
		}
		System.out.println("inside post");
		try {
			if (path.equals("/login")) {
				handleLogin(request, response);

			}	 else if (path.equals("/longterm-token/revocation")) {

				handleLongTermTokenRevokation(request, response);
			
			} else if (path.equals("/longterm-token")) {

				handleLongTermTokenGeneration(request, response);
			//	response.sendRedirect(request.getContextPath()+"/bank/profile");

			} else if (path.equals("/logout")) {

				handleLogOut(request, response);

			}
		} catch (TaskException | SQLException | ServletException | IOException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}

	}

	private void handleLongTermTokenGeneration(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, TaskException, ServletException, IOException {

		String token = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("auth_token".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}
		if (token != null) {

			Claims claims = SessionUtil.parseClaims(token);
			String personId = claims.getSubject();
			String role = (String) claims.get("role");
			String name = (String) claims.get("name");
			String status = (String) claims.get("status");
			token = SessionUtil.createLongTermToken(personId, role, name, status);

			int oneYearSeconds = 60 * 60 * 24 * 365;
			Cookie cookie = new Cookie("long_term_token", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			cookie.setPath("/");
			cookie.setMaxAge(oneYearSeconds);
			response.addCookie(cookie);

		}
	       //request.setAttribute("longTermToken", token);
	       response.setContentType("text/plain");
	       response.getWriter().write(token);
	  // 	request.getRequestDispatcher(request.getContextPath()+"/bank/profile").forward(request, response);

	}

	private void handleLongTermTokenRevokation(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, TaskException, ServletException, IOException {

		String token = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("long_term_token".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}
		if (token != null) {

			Claims claims = SessionUtil.parseClaims(token);
			String personId = claims.getSubject();
			SessionUtil.invalidateToken(personId, "LONGTERM", "REVOKED");

			Cookie clearedCookie = new Cookie("long_term_token", "");
			clearedCookie.setPath("/");
			clearedCookie.setMaxAge(0);
			clearedCookie.setHttpOnly(true);
			clearedCookie.setSecure(true);
			response.addCookie(clearedCookie);
		}
		   request.setAttribute("longTermToken", token);
		   	request.getRequestDispatcher(request.getContextPath()+"/bank/profile").forward(request, response);
	}

	private void handleLogOut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, SQLException, TaskException {

		String token = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("auth_token".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
		}
		if (token != null) {

			Claims claims = SessionUtil.parseClaims(token);
			String personId = claims.getSubject();
			SessionUtil.invalidateToken(personId, "ACCESS", "REVOKED");

			Cookie clearedCookie = new Cookie("auth_token", "");
			clearedCookie.setPath("/");
			clearedCookie.setMaxAge(0);
			clearedCookie.setHttpOnly(true);
			clearedCookie.setSecure(true);
			response.addCookie(clearedCookie);
		}

		response.sendRedirect(request.getContextPath() + "/login");
	}

	public static void handleLogin(HttpServletRequest request, HttpServletResponse response)
			throws IOException, SQLException, TaskException, ServletException {

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		PersonDAO dao = new PersonDAO();
		Person person = dao.checkCredentials(email, password);

		if (person != null) {
			String personId = person.getPersonId();
			String role = person.getRole();
			String status = person.getStatus();
			String name = person.getName();

			String token = SessionUtil.generateToken(personId, role, name, status);

			Cookie cookie = new Cookie("auth_token", token);
			cookie.setHttpOnly(true);
			cookie.setSecure(true);
			cookie.setPath("/");
			cookie.setMaxAge(30 * 60);
			response.addCookie(cookie);
			String redirectUrl;
			if (status.equals("PENDING") || status.equals("INACTIVE") || status.equals("REJECTED")) {
				request.setAttribute("status", status);
				redirectUrl = request.getContextPath() + "/bank/dashboard/status";
			} else {
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
					return;
				}
			}

			response.sendRedirect(redirectUrl);

		} else {
			request.setAttribute("alertMessage", "Invalid email or password");
			request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
		}
	}

}
