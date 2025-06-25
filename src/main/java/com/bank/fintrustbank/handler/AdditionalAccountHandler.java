package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.factory.AccountFactory;
import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.model.Branch;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class AdditionalAccountHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
	        .findAndRegisterModules();

	private final AccountDAO accountDAO = new AccountDAO();
	

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws  TaskException
	{
		String path = request.getPathInfo();
		try
		{
		if (path.equals("/admin/additional-account")) {
			 handleGetAdminAdditional(request, response);

			}
		else if (path.equals("/customer/additional-account")) {
			handleGetCustomerAdditional(request, response);
		}
		}catch(IOException  | ServletException | SQLException e)
		{
			e.printStackTrace();
			throw new TaskException(e.getMessage(),e);
		}

		}



	private void handleGetCustomerAdditional(HttpServletRequest request, HttpServletResponse response)
			throws TaskException, SQLException, ServletException, IOException {
		List<Branch> branches = new BranchDAO().getAllBranches();
		System.out.println(branches.toString());
		request.setAttribute("branches", branches);
		    request.setAttribute("page" , "additional-account");
		    request.getRequestDispatcher("/bank/customer/dashboard").forward(request, response);
	}



	private void handleGetAdminAdditional(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, TaskException, SQLException {

		List<Branch> branches = new BranchDAO().getAllBranches();
		System.out.println(branches.toString());
		request.setAttribute("branches", branches);
		    request.setAttribute("page" , "additional-account");
		    
		     if(request.getAttribute("role").equals("ADMIN"))
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
	}
	
	
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws  TaskException{
		String path = request.getPathInfo();
		try
		{
		if (path.equals("/admin/additional-account")) {
				handleAdminCreateAdditionalAccount(request, response);
		
			}
		else if (path.equals("/customer/additional-account")) {
			handleCreateAdditionalAccount(request, response);
			
		}
		}catch(IOException  | ServletException | SQLException e)
		{
			e.printStackTrace();
			throw new TaskException(e.getMessage(),e);
		}

		}
	
	

	private boolean handleCreateAdditionalAccount(HttpServletRequest request, HttpServletResponse response) throws TaskException, IOException, ServletException, SQLException {


		    String sessionPersonId = (String) request.getAttribute("personId");
		   
		    
		   
		   
		  
		    String jsonBody = new BufferedReader(request.getReader())
		            .lines()
		            .collect(Collectors.joining());

		    JsonNode rootNode = mapper.readTree(jsonBody);

		    
		    String branchId = rootNode.path("branch_id").asText();
		    String accountType = rootNode.path("account_type").asText();
		    String role =(String) request.getAttribute("role") ; 
		    String customerId;
		    if(role=="ADMIN" ||role =="SUPERADMIN" )
		    {
		    	customerId = rootNode.path("person_id").asText();
		    }
		    else
		    {
		    	customerId = sessionPersonId ; 
		    }
		    Account account = AccountFactory.createAccount(sessionPersonId, branchId,customerId, accountType);
		    return accountDAO.addAccount(account);
		
	}

	private boolean handleAdminCreateAdditionalAccount(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException, SQLException {
		

		    String sessionPersonId = (String) request.getAttribute("personId");

		  
		    BufferedReader bufferedReader = new BufferedReader(request.getReader());
			String jsonBody = bufferedReader
		            .lines()
		            .collect(Collectors.joining());

		    JsonNode rootNode = mapper.readTree(jsonBody);

		    String branchId = rootNode.path("branch_id").asText();
		    String customerId = rootNode.path("person_id").asText();
		    String accountType = rootNode.path("account_type").asText();
		    Account account = AccountFactory.createAccount(sessionPersonId, branchId, customerId, accountType);
		    return accountDAO.addAccount(account);
	
	}


	

	
}