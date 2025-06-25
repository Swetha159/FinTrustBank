package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.dao.ResetTokenDAO;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.model.ResetToken;
import com.bank.fintrustbank.service.AdminService;
import com.bank.fintrustbank.service.CustomerService;
import com.bank.fintrustbank.util.EmailService;
import com.bank.fintrustbank.util.Password;
import com.bank.fintrustbank.util.TimeFormatter;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.zoho.training.exceptions.TaskException;

public class PersonHandler implements HttpRequestHandler  {


	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();
	
	private final PersonDAO personDAO = new PersonDAO();
	private final BranchDAO branchDAO  = new BranchDAO();
	private final ResetTokenDAO resetTokenDAO = new ResetTokenDAO();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		 String path = request.getPathInfo();
		 
		 try {
		 if(path.equals("/customers"))
		 {
			handleGetCustomers(request,response);
			
		 } 
		 else if(path.equals("/forgot-password"))
		 {
			 request.getRequestDispatcher("/WEB-INF/views/forgot-password.jsp").forward(request, response);
		 }
		 else if(path.equals("/profile"))
		 {
			 handleGetPersonDetails(request,response);
			 request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
		 }
		 else if(path.equals("/reset-password"))
		 {
			 request.getRequestDispatcher("/WEB-INF/views/reset-password.jsp").forward(request, response);
		 }
		 }catch (IOException | SQLException | ServletException  e) {
				e.printStackTrace();
				throw new TaskException(e.getMessage(), e);
			}
	 }
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
	 else if (path.equals("/person/status"))
	 {
		if(!handleUpdateStatus(request,response))
		{
			System.out.println("inside person/status");
			if(request.getAttribute("errorMessage")==null)
			{
				request.setAttribute("errorMessage", "Error while updating status");
			}
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
		}
	 }
	
	 }catch (IOException  | ServletException | SQLException  e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
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

	@Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException{
	 String path = request.getPathInfo();
	 System.out.println("inside doPost"+ path); 
	 try {
		 if(path.equals("/person/details"))
		 {
			  handleGetPersonDetails(request , response) ; 
				request.getRequestDispatcher("/WEB-INF/admindashboard/editform.jsp").forward(request, response) ; 
		 }
	
		 else if(path.equals("/forgot-password"))
		 {
			
		        handleResetPasswordRequest(request, response);
		    }
		 else if (path.equals("/reset-password"))
		 {

		        handleChangePassword(request, response);
		        
		    }
		 else if (path.equals("/person/role"))
		 {

		       handleGetRole(request, response);
		        
		    }
		 
		 
	 }catch (IOException  | ServletException | SQLException  e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
 }
	
	private void handleGetRole(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		
		String personId = rootNode.path("person_id").asText();
		
		String role = personDAO.getRole(personId);
		ObjectNode result = mapper.createObjectNode();
		if (role == null) {
		    result.put("role", "unassigned");
		} else {
		    result.put("role", role);
		}

		response.getWriter().write(result.toString());
		
		
	}
	
    

	private void handleResetPasswordRequest(HttpServletRequest request, HttpServletResponse response)
        throws IOException, StreamReadException, DatabindException, StreamWriteException, TaskException, SQLException {

    String email = request.getParameter("email");
    Map<String, String> jsonResponse = new HashMap<>();
    response.setContentType("application/json");

    if (personDAO.ifexist(email)) {
        try {
            String token = UUID.randomUUID().toString();
            Timestamp expiry = Timestamp.valueOf(LocalDateTime.now().plusMinutes(30));
            Long expiryMillis = expiry.toInstant().toEpochMilli();

            resetTokenDAO.saveToken(new ResetToken(email, expiryMillis, token));

            String resetLink = request.getRequestURL().toString()
                    .replace("forgot-password", "reset-password") + "?token=" + token;
            String emailBody = "Click this link to reset your password:\n" + resetLink;
            EmailService.sendEmail(email, "Password Reset Request", emailBody);

            jsonResponse.put("message", "Reset link sent to email.");
            response.setStatus(HttpServletResponse.SC_OK);

        } catch (Exception e) {
            e.printStackTrace();
            jsonResponse.put("error", "Failed to process request.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

    } else {
        jsonResponse.put("error", "This email is not registered with us.");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);  
    }

    mapper.writeValue(response.getOutputStream(), jsonResponse);
}

	private void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
			throws IOException, StreamReadException, DatabindException, TaskException, StreamWriteException {
		System.out.println("inside the method ");
		Map<String, String> requestBody = mapper.readValue(request.getInputStream(), Map.class);
		String token = requestBody.get("token");
		String newPassword = requestBody.get("newPassword");

		response.setContentType("application/json");
		Map<String, String> jsonResponse = new HashMap<>();

		try {
			Map<String,Object> result = resetTokenDAO.getExpiryAndUsed(token) ; 
			System.out.println(result.toString());
			Long expiry = (Long) result.get("expiry");
			Boolean used = (Boolean) result.get("used") ; 
			
			if(System.currentTimeMillis() >= expiry  || used==true)
			{
				jsonResponse.put("error", "Invalid or expired token.");
		        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
		        String email = resetTokenDAO.getEmail(token);
		        if (email == null) {
		            jsonResponse.put("error", "Token not associated with any email.");
		            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		        } else {
		            
		            String hashedPassword = Password.createHash(newPassword);
		            boolean success = personDAO.updatePassword(email, hashedPassword);
		            if (success) {
		                resetTokenDAO.markUsed(token);
		                jsonResponse.put("message", "Password reset successful.");
		                response.setStatus(HttpServletResponse.SC_OK);
		            } else {
		                jsonResponse.put("error", "Failed to update password.");
		                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		            }
		        }
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		    jsonResponse.put("error", "Server error.");
		    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}

		mapper.writeValue(response.getOutputStream(), jsonResponse);
	}

	private void handleGetPersonDetails(HttpServletRequest request, HttpServletResponse response) throws TaskException, SQLException, IOException {

		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		
		String personId = rootNode.path("person_id").asText(); 
		if(personId==null || personId.isEmpty())
		{
			personId = (String) request.getAttribute("personId");
		}
		
		String targetRole = rootNode.path("target_role").asText();
		System.out.println(personId);
		request.setAttribute("target_role", targetRole);
		List<Map<String, Object>>  details  = personDAO.getPersonDetails(personId);
		System.out.println(details);
		 if(details!=null)
		 {
			 TimeFormatter.convertMillisToDateTime(details, "created_at");
			 TimeFormatter.convertMillisToDateTime(details, "modified_at");
			 Map<String,Object> person = details.get(0);
			 request.setAttribute("person", person);
			
		 }
	
			
		
		
	}
	private void  handleGetCustomers(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException, ServletException {
		
	
		String branchId  =  (String)request.getAttribute("branchId") ;
		
		
		List<Map<String,Object>>  customers  = personDAO.getCustomers(branchId);
		 if(customers!=null)
		 {
			 
			 request.setAttribute("customers", customers);
			
		 }
		 request.setAttribute("page", "customers");
	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String json = mapper.writeValueAsString(customers);
			response.getWriter().write(json);
		/* if(request.getAttribute("role").equals("ADMIN"))
			{
				request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
			}
			else if(request.getAttribute("role").equals("SUPERADMIN"))
			{
				request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
			}
			else
			{
				request.setAttribute("errorMessage", "Access Restricted");
				request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			}
		*/
	}
	

	private boolean handleUpdateStatus(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
				
		String sessionPersonId = (String) request.getAttribute("personId");
		
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
		else if(role.equals("ADMIN") ||role.equals("SUPERADMIN"))
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
		
		String sessionPersonId = (String)request.getAttribute("personId");
		
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
		
		String sessionPersonId = (String) request.getAttribute("personId");
		BufferedReader reader = request.getReader();
		Person person  =mapper.readValue(reader,Person.class);
		person.setModifiedBy(sessionPersonId);
		person.setModifiedAt(System.currentTimeMillis());
		System.out.println(person);
		
		return personDAO.updatePerson(person);
		
	}
	
	
}
