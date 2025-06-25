package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.factory.PersonFactory;
import com.bank.fintrustbank.factory.PrivilegedUserFactory;
import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.model.PrivilegedUser;
import com.bank.fintrustbank.service.AdminService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class AdminHandler implements HttpRequestHandler{


	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();
	
	private final  BranchDAO branchDAO = new BranchDAO();
	
	private final  PersonDAO personDAO = new PersonDAO();
	
	  @Override
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		  
			 String path = request.getPathInfo();
			 try {
			 if(path.equals("/admin"))
			 {
				 handleAddAdmin(request, response);
				 
				 
			 }
			 }catch (IOException | SQLException | ServletException e) {
					e.printStackTrace();
					throw new TaskException(e.getMessage(), e);
				}
			 
	  }
	  
	  @Override
	    public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		  
			 String path = request.getPathInfo();
			 try {
			 if(path.equals("/admins"))
			 {
				 handleGetAdmins(request, response);
			 }
			 if(path.equals("/superadmins"))
			 {
				 handleGetSuperAdmins(request, response);
			 }
			 if(path.equals("/admin"))
			 {
				 List<Branch> branches = branchDAO.getAllBranches();
					System.out.println(branches.toString());
				
					request.setAttribute("branches", branches);
				   request.getRequestDispatcher("/WEB-INF/admindashboard/create-admin.jsp").forward(request, response);
				 
			 }
			 }catch (IOException | SQLException | ServletException e) {
					e.printStackTrace();
					throw new TaskException(e.getMessage(), e);
				}
			 
	  }

	private void handleGetSuperAdmins(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException, SQLException {
		List<Map<String,Object>> superadmins = personDAO.getSuperAdmins();
		if(superadmins!= null)
		{
			request.setAttribute("superadmins",superadmins ) ; 
			request.setAttribute("page", "superadmins") ; 
			request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response) ; 
		}
		else
		{
			request.setAttribute("page", "superadmins") ; 
			request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response) ; 
		}
		
	}

	private void handleGetAdmins(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException, ServletException {
	
	
	
		String branchId  =  (String) request.getAttribute("branchId") ;
		List<Map<String,Object>> admins = personDAO.getAdmins(branchId);
		if(admins!= null)
		{
			request.setAttribute("admins",admins ) ; 
			request.setAttribute("page", "admins") ; 
			request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response) ; 
		}
		else
		{
			request.setAttribute("page", "admins") ; 
			request.getRequestDispatcher("/WEB-INF/admindashboard/superadmindashboard.jsp").forward(request, response) ; 
		}
	}
	private void handleAddAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException, SQLException {
		

		String sessionPersonId = (String) request.getAttribute("personId");
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);

		String branchId = rootNode.path("branch_id").asText();
		String role = rootNode.path("role").asText();
		Person person = PersonFactory.createPerson(sessionPersonId, jsonBody, "ACTIVE", role);
		PrivilegedUser privilegedUser = PrivilegedUserFactory.createPrivilegedUser(person.getPersonId(), branchId, sessionPersonId);

		AdminService service = new AdminService();

		if (!service.addNewAdmin(person, privilegedUser)) {
			throw new TaskException("Admin not created");
		}
	}

	
	
}
