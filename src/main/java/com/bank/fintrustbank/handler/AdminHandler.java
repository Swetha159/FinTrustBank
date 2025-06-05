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
import javax.servlet.http.HttpSession;

import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.dao.PrivilegedUserDAO;
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
			 if(path.equals("/admin"))
			 {
				 List<Branch> branches = new BranchDAO().getAllBranches();
					System.out.println(branches.toString());
				
					request.setAttribute("branches", branches);
				   request.getRequestDispatcher("/WEB-INF/admindashboard/create-admin.jsp").forward(request, response);
				 
			 }
			 }catch (IOException | SQLException | ServletException e) {
					e.printStackTrace();
					throw new TaskException(e.getMessage(), e);
				}
			 
	  }

	private void handleGetAdmins(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException, ServletException {
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			
		}
		String sessionPersonId = (String) session.getAttribute("personId");
		String branchId  = session.getAttribute("branch_id")!=null ? (String) session.getAttribute("branch_id") : new PrivilegedUserDAO().getBranch(sessionPersonId);
		List<Map<String,Object>> admins = new PersonDAO().getAdmins(branchId);
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
		

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
		}
		
		String sessionPersonId = (String) session.getAttribute("personId");
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);

		String branchId = rootNode.path("branch_id").asText();
		Person person = PersonFactory.createPerson(sessionPersonId, jsonBody, "ACTIVE", "ADMIN");
		PrivilegedUser privilegedUser = PrivilegedUserFactory.createPrivilegedUser(person.getPersonId(), branchId, sessionPersonId);

		AdminService service = new AdminService();

		if (!service.addNewAdmin(person, privilegedUser)) {
			throw new TaskException("Admin not created");
		}
	}

	
	
}
