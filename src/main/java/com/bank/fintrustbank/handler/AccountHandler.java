package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.util.TimeFormatter;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class AccountHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
			.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();
	private final AccountDAO accountDAO = new AccountDAO();
  

	 

	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {

		String path = request.getPathInfo();
		try {
			if (path.equals("/allaccounts")) {
				handleAllAccounts(request, response);
			}
			else if (path.equals("/branch-accounts")) {
				handleBranchAccounts(request, response);
			}
			else if(path.equals("/accountno"))
			{
				handleAccountNo(request,response);
			}
			else if(path.equals("/balance"))
			{
				handleBalance(request,response) ; 
			}

		} catch (IOException | SQLException | ServletException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	




	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		String path = request.getPathInfo();
		try {
			if (path.equals("/account/status")) {
				handleUpdateAccountStatus(request, response);
			}

		} catch (IOException | SQLException e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	private boolean handleUpdateAccountStatus(HttpServletRequest request, HttpServletResponse response)
			throws IOException, TaskException, SQLException {
		String sessionPersonId = (String) request.getAttribute("personId");
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);

		String status = rootNode.path("status").asText();
		Long accountNo = rootNode.path("account_no").asLong();
		Long modifiedAt = System.currentTimeMillis();

		return accountDAO.updateStatus(accountNo, status, modifiedAt, sessionPersonId);
	}

	private void handleAllAccounts(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException, ServletException {
	
		String sessionPersonId = (String) request.getAttribute("personId");

		List<Map<String,Object>>  details  = accountDAO.getUserAccounts(sessionPersonId);
		  response.setContentType("application/json");
		    PrintWriter out = response.getWriter();

		    if (details != null && !details.isEmpty()) {
		        
		        request.setAttribute("accounts", details);
		        request.setAttribute("page" , "myaccounts");
		        request.getRequestDispatcher("/bank/customer/dashboard").forward(request, response);
		    
		    } else {
		       
		    }
	}

	private void handleBranchAccounts(HttpServletRequest request, HttpServletResponse response) throws TaskException, SQLException, IOException, ServletException {
System.out.println("Branch Accounts"); 
		
		String branchId  = (String) request.getAttribute("branchId");
		
		List<Map<String,Object>>  details  = accountDAO.getAccounts(branchId);
		 if (details != null && !details.isEmpty()) {
		        request.setAttribute("accounts", details);
		        TimeFormatter.convertMillisToDateTime(details, "created_at");
		        TimeFormatter.convertMillisToDateTime(details, "modified_at");
		
		 }
			
		 request.setAttribute("page" , "accounts");
		 String role = (String) request.getAttribute("role");
			if(role.equals("ADMIN"))
			{
				request.getRequestDispatcher("/bank/admin/dashboard").forward(request, response);
			}else if (role.equals("SUPERADMIN"))
			{
				request.getRequestDispatcher("/bank/superadmin/dashboard").forward(request, response);
			}else
			{
				request.setAttribute("errorMessage", "Access Restricted");
				request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			}
	}


	private void handleAccountNo(HttpServletRequest request, HttpServletResponse response) throws TaskException, SQLException, StreamWriteException, DatabindException, IOException {
			
		String sessionPersonId = (String) request.getAttribute("personId");
		List<Map<String,Object>>  accountNos = accountDAO.getAccountNo(sessionPersonId);
		System.out.println("person id "+ sessionPersonId  + "accountno" +accountNos);
		Map<String , Object> accountNoMap =accountNos.get(0);
		request.setAttribute("account_no_list", accountNos);
		System.out.println(accountNos);
		Long accountNo = (Long) accountNoMap.get("account_no");
 		if(accountNo!=null)
		{
 			request.setAttribute("account_no", accountNo);
			Map<String, Object> jsonResponse = new HashMap<>();
			jsonResponse.put("account_no", accountNo);
			response.setContentType("application/json");
			mapper.writeValue(response.getWriter(), jsonResponse);	
		}else
		{
		}
 		
	}
	
	

	private void handleBalance(HttpServletRequest request, HttpServletResponse response) throws TaskException, SQLException, StreamWriteException, DatabindException, IOException {

		String sessionPersonId = (String) request.getAttribute("personId");
	List<Map<String,Object>>  accountNos = accountDAO.getAccountNo(sessionPersonId);
	Double balance = (double) 0; 
	for(Map<String,Object> accountNoMap :accountNos)
	{
		Long accountNo = (Long) accountNoMap.get("account_no");
		balance += accountDAO.getBalance(accountNo);
	}
	  BigDecimal roundedBalance = BigDecimal.valueOf(balance).setScale(2, RoundingMode.HALF_UP);
System.out.print(roundedBalance);
    if(roundedBalance!=null)
	{
		Map<String, Object> jsonResponse = new HashMap<>();
		jsonResponse.put("balance", roundedBalance);
		response.setContentType("application/json");
		mapper.writeValue(response.getWriter(), jsonResponse);	
	}
	
	}

	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
}
