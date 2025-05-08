package com.bank.fintrustbank.handler;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.fintrustbank.factory.AccountFactory;
import com.bank.fintrustbank.factory.PersonFactory;
import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.service.FirstAccountService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;


public class FirstAccountHandler implements HttpRequestHandler {


	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
	        .findAndRegisterModules();
	 FirstAccountService service =  new FirstAccountService();
	 @Override
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException{
		  
			 String path = request.getPathInfo();
			 try {
				 if(path.equals("/account-request"))
				 {
					
						handleFirstAccountRequest(request, response);
					 
				 } 
				 else if(path.equals("/account-request/approval"))
				 {
					 handleFirstAccountApprove(request,response);
				 }
				 else if (path.equals("/admin/firstaccount"))
				 {
					 handleAdminCreateFirstAccount(request,response);
				 }
			 }
				 catch(IOException | SQLException | ServletException e)
				 {
					e.printStackTrace();
					throw new TaskException(e.getMessage(),e);
				 }
			
			 
	  }
	 
	private boolean handleAdminCreateFirstAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException {
		 HttpSession session = request.getSession(false); 
		    if (session == null || session.getAttribute("personId") == null) {
		    	 request.setAttribute("errorMessage", "session expired");
	             request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
		        return false;
		    }

		    String sessionPersonId = (String) session.getAttribute("personId");

		  
		    String jsonBody = new BufferedReader(request.getReader())
		            .lines()
		            .collect(Collectors.joining());

		    JsonNode rootNode = mapper.readTree(jsonBody);
		    String branchId = rootNode.path("branch_id").asText();
		    String accountType = rootNode.path("account_type").asText();
		    Person person = PersonFactory.createPerson(sessionPersonId , jsonBody,"ACTIVE","CUSTOMER" );
		    Account account = AccountFactory.createAccount(sessionPersonId, branchId, person.getPersonId(), accountType);
		    return service.adminCreateFirstAccount(person,account);
	}

	private boolean handleFirstAccountApprove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException {
		 HttpSession session = request.getSession(false); 
		    if (session == null || session.getAttribute("personId") == null) {
		    	 request.setAttribute("errorMessage", "session expired");
	             request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
		        return false;
		    }

		    String sessionPersonId = (String) session.getAttribute("personId");

		  
		    String jsonBody = new BufferedReader(request.getReader())
		            .lines()
		            .collect(Collectors.joining());

		    JsonNode rootNode = mapper.readTree(jsonBody);

		    String branchId = rootNode.path("branch_id").asText();
		    String customerId = rootNode.path("person_id").asText();
		    String accountType = rootNode.path("account_type").asText();
		    Account account = AccountFactory.createAccount(sessionPersonId, branchId, customerId, accountType);
		   
		    return service.approveFirstAccount(account);
			
		    
			
	}

	private  boolean handleFirstAccountRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException{
		
		   BufferedReader reader = request.getReader();
	 
	        String jsonBody = new BufferedReader(request.getReader())
		            .lines()
		            .collect(Collectors.joining());

		    JsonNode rootNode = mapper.readTree(jsonBody);
		    

	        String branchId = rootNode.path("branch_id").asText();
		    String accountType = rootNode.path("account_type").asText();
		 
	        Person person = PersonFactory.createPerson(null, jsonBody, "PENDING","CUSTOMER");
	    
	        
	        
	        AccountRequest acRequest  = new AccountRequest(person.getPersonId() , branchId , accountType , "PENDING" , System.currentTimeMillis() ,(Long)null , person.getPersonId()); 
	        
	     
	        
	       
	        return service.requestFirstAccount(acRequest , person);
	        
	       
	
	       
	}

	

	
}
