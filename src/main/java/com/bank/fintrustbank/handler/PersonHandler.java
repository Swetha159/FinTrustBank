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
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.service.AdminService;
import com.bank.fintrustbank.service.CustomerService;
import com.bank.fintrustbank.util.TimeFormatter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class PersonHandler implements HttpRequestHandler  {


	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();
	
	PersonDAO personDAO = new PersonDAO();
	BranchDAO branchDAO  = new BranchDAO();
	@Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) throws TaskException {
	 String path = request.getPathInfo();
	 try {
	 if(path.equals("/person"))
	 {
		 if( handleUpdatePerson(request,response))
			{
				response.setContentType("text/plain");
				response.getWriter().write("Updation Successful");

			}
			else
			{
				response.setContentType("text/plain");
				response.getWriter().write("Updation Unsuccessful");

			}
		;
	 }
	
	 }catch (IOException  | ServletException | SQLException  e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
 }
	
	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException{
	 String path = request.getPathInfo();
	 try {
		 if(path.equals("/person/details"))
		 {
			  handleGetPersonDetails(request , response) ; 
				request.getRequestDispatcher("/WEB-INF/admindashboard/editform.jsp").forward(request, response) ; 
		 }
	
	 }catch (IOException  | ServletException | SQLException  e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
 }
	@Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {
	 String path = request.getPathInfo();
	 try {
	 if(path.equals("/customers"))
	 {
		 HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("personId") == null) {
				request.setAttribute("errorMessage", "session expired");
				request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				
			}
		 List<Map<String,Object>> customers =handleGetCustomers(request,response);
		 if(customers!=null)
		 {
			 //TimeFormatter.convertMillisToDateTime(customers, "created_at");
			 request.setAttribute("customers", customers);
			
		 }
		 request.setAttribute("page", "customers");
		 if(session.getAttribute("role").equals("ADMIN"))
			{
				request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
			}
			else if(session.getAttribute("role").equals("SUPERADMIN"))
			{
				request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
			}
			else
			{
				request.setAttribute("errorMessage", "Access Restricted");
				request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			}
	 }
	
	
	 }catch (IOException | SQLException | ServletException  e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
 }
	private void handleGetPersonDetails(HttpServletRequest request, HttpServletResponse response) throws TaskException, SQLException, IOException {

		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		
		String personId = rootNode.path("person_id").asText();
		List<Map<String, Object>>  details  = personDAO.getPersonDetails(personId);
		 if(details!=null)
		 {
			 TimeFormatter.convertMillisToDateTime(details, "created_at");
			 TimeFormatter.convertMillisToDateTime(details, "modified_at");
			 Map<String,Object> person = details.get(0);
			 request.setAttribute("person", person);
			
		 }
	
			
		
		
	}
	private List<Map<String,Object>>  handleGetCustomers(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException, ServletException {
		
	
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			return null;
		}

		String sessionPersonId = (String) session.getAttribute("personId");
		String branchId  = session.getAttribute("branch_id")!=null ? (String) session.getAttribute("branch_id") : new PrivilegedUserDAO().getBranch(sessionPersonId);
		
		
		List<Map<String,Object>>  customers  = personDAO.getCustomers(branchId);
		return customers ;  
		
	}
	
	@Override
	public void doPatch(HttpServletRequest request , HttpServletResponse response) throws TaskException
	{
		String path = request.getPathInfo();
		try {
			if(path.equals("/person/role"))
			{
				if(!handleUpdateRole(request, response))
				{
					if(request.getAttribute("errorMessage")==null)
					{
						request.setAttribute("errorMessage", "Error while updating role");
					}
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}
				
			} else if (path.equals("/person/status"))
			 {
				if(!handleUpdateStatus(request,response))
				{
					if(request.getAttribute("errorMessage")==null)
					{
						request.setAttribute("errorMessage", "Error while updating status");
					}
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}
			 }
			 else if (path.equals("/person/password"))
			 {
				 
			 }
		}catch (IOException  | ServletException | TaskException | SQLException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	private boolean handleUpdateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
				
		HttpSession session = request.getSession(false);
		String sessionPersonId = (String) session.getAttribute("personId");
		
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		
		String status = rootNode.path("status").asText();
		String personId = rootNode.path("person_id").asText();
		Long modifiedAt = System.currentTimeMillis();
		String role = rootNode.path("role").asText();
		
		if(role.equals("CUSTOMER"))
		{
			return new CustomerService().updateCustomerStatus(status ,personId , modifiedAt , sessionPersonId);
		}
		else if(role.equals("ADMIN") ||role.equals("ADMIN"))
		{
			return personDAO.updateStatus( status ,personId , modifiedAt , sessionPersonId);
		}
		else
		{
			request.setAttribute("errorMessage", "Unsupported Operation");
			return false ;
		}
		
	}

	private boolean handleUpdateRole(HttpServletRequest request, HttpServletResponse response) throws IOException ,TaskException, SQLException {
		
		HttpSession session = request.getSession(false);
		String sessionPersonId = (String) session.getAttribute("personId");
		
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		
		String role = rootNode.path("role").asText();
		String newRole = rootNode.path("new_role").asText();
		String branchId = rootNode.path("branch_id").asText();
		String personId = rootNode.path("person_id").asText();
		
		Long modifiedAt = System.currentTimeMillis();
	
		if(role.equals("CUSTOMER"))
		{
			request.setAttribute("errorMessage", "Customer role cannot be changed");
			return false ;
		}
		
		if(newRole.equals("SUPERADMIN"))
		{
			
			if(branchDAO.getSuperAdmin(branchId)!=null)
			{
				request.setAttribute("errorMessage", "Admin already assigned for this branch");
				 return false ;
			}
			 
			return new AdminService().makeSuperAdmin(branchId , personId , modifiedAt ,sessionPersonId );
		}
		else if(newRole.equals("ADMIN"))
		{
			return new AdminService().makeAdmin(branchId , personId , modifiedAt ,sessionPersonId );
		}
		else
		{
			request.setAttribute("errorMessage", "Unsupported Operation");
			return false ;
		}
	
		
	}

	private boolean handleUpdatePerson(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException, SQLException {
		
		HttpSession session = request.getSession(false);
		String sessionPersonId = (String) session.getAttribute("personId");
		BufferedReader reader = request.getReader();
		Person person  =mapper.readValue(reader,Person.class);
		person.setModifiedBy(sessionPersonId);
		person.setModifiedAt(System.currentTimeMillis());
		System.out.println(person);
		
		return personDAO.updatePerson(person);
		
	}
	
	
}
