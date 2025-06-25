package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.service.AdminService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class SuperAdminHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();

	

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		String path = request.getPathInfo();
		try {
			if (path.equals("/branch/superadmin")) {
				String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
				JsonNode rootNode = mapper.readTree(jsonBody);

				String branchId = rootNode.path("branch_id").asText();
				String location = rootNode.path("location").asText();
				
				request.setAttribute("branch_id", branchId);
				request.setAttribute("location", location) ;
				
				request.getRequestDispatcher("/WEB-INF/admindashboard/assign-manager.jsp").forward(request, response);
			} 
		} catch (IOException | ServletException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		String path = request.getPathInfo();
		try {
			if (path.equals("/branch/superadmin")) {
				
				String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
				JsonNode rootNode = mapper.readTree(jsonBody);
				String role = rootNode.path("role").asText();
				String branchId = rootNode.path("branch_id").asText();
				String managerId = rootNode.path("manager_id").asText();
				if(role.equals("SUPERADMIN"))
				{
					handleChangeSuperAdmin(branchId, managerId , request , response);
				}
				else
				{
					handleMakeSuperAdmin(branchId, managerId , request , response);
				}
			
			}
		} catch (IOException | ServletException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	private void handleChangeSuperAdmin(String branchId, String managerId  ,HttpServletRequest request ,HttpServletResponse response) throws TaskException, ServletException, IOException {
	

		String sessionPersonId = (String) request.getAttribute("personId");
	
		Long modifiedAt = System.currentTimeMillis();
		AdminService service = new AdminService();
		 
		if(!service.changeSuperAdmin(branchId,managerId, modifiedAt, sessionPersonId))
		{
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		    response.setContentType("application/json");
		    response.getWriter().write("{\"message\":\"Failed to assign Super Admin.\"}");
		}
		
	}
	private void handleMakeSuperAdmin(String branchId, String managerId  ,HttpServletRequest request ,HttpServletResponse response) throws ServletException, IOException, TaskException {
		
		String sessionPersonId = (String) request.getAttribute("personId");
		
		Long modifiedAt = System.currentTimeMillis();
		AdminService service = new AdminService();
		 
		if(!service.makeSuperAdmin(branchId,managerId, modifiedAt, sessionPersonId))
		{
			  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			    response.setContentType("application/json");
			    response.getWriter().write("{\"message\":\"Failed to assign Super Admin.\"}");
		}
		

	}

	/*
	 * public void handleAddSuperAdmin(HttpServletRequest request,
	 * HttpServletResponse response) throws ServletException, IOException,
	 * TaskException, SQLException {
	 * 
	 * HttpSession session = request.getSession(false); if (session == null ||
	 * session.getAttribute("personId") == null) {
	 * request.setAttribute("errorMessage", "session expired");
	 * request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request,
	 * response);
	 * 
	 * }
	 * 
	 * String sessionPersonId = (String) session.getAttribute("personId"); String
	 * jsonBody = new
	 * BufferedReader(request.getReader()).lines().collect(Collectors.joining());
	 * JsonNode rootNode = mapper.readTree(jsonBody);
	 * 
	 * String branchId = rootNode.path("branch_id").asText(); Person person =
	 * PersonFactory.createPerson(sessionPersonId, jsonBody, "ACTIVE",
	 * "SUPERADMIN"); PrivilegedUser privilegedUser =
	 * PrivilegedUserFactory.createPrivilegedUser(person.getPersonId(), branchId,
	 * sessionPersonId);
	 * 
	 * AdminService service = new AdminService();
	 * 
	 * if (!service.addNewAdmin(person, privilegedUser)) { throw new
	 * TaskException("Super Admin not created"); }
	 * 
	 * }
	 */

}
