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

import com.bank.fintrustbank.dao.TransactionDAO;
import com.bank.fintrustbank.model.Transaction;
import com.bank.fintrustbank.service.TransactionService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class TransactionHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
	        .findAndRegisterModules();
	
	
	private final TransactionDAO dao  = new TransactionDAO();
	TransactionService service = new TransactionService();
		@Override
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		  
		try {	 
		 String path = request.getPathInfo();
			 if(path.equals("/transaction"))
			 {
				 handleTransaction(request, response);

			 }
			 else if(path.equals("/deposit"))
			 {
				 handleDeposit(request, response);

			 }
			 else if(path.equals("/withdraw"))
			 {
				 handleWithdraw(request, response);

			 }
	 }
			 catch (IOException  | ServletException | SQLException | com.zoho.training.exceptions.TaskException e) {
					e.printStackTrace();
					throw new TaskException(e.getMessage(), e);
				}
			 
			 
	  }

		@Override
	    public void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		  
		try {	 
		 String path = request.getPathInfo();
			 if(path.equals("/transactions"))
			 {
				 handleViewTransaction(request, response);

			 }
			 else if(path.equals("/history"))
			 {
				 handleTransactionHistory(request, response);

			 }
			 else if(path.equals("/withdraw"))
			 {
				 handleWithdraw(request, response);

			 }
			 else if(path.equals("creditcount"))
			 {
				 handleCreditCount(request,response);
			 }
			 else if(path.equals("debitcount"))
			 {
				 handleDebitCount(request,response);
			 }
	 }
			 catch (IOException  | ServletException | SQLException | TaskException e) {
					e.printStackTrace();
					throw new TaskException(e.getMessage(), e);
				}
			 
			 
	  }

		
	private void handleCreditCount(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		
		
		
		Long accountNo = rootNode.path("account_no").asLong();
		int creditCount =  dao.getTotalCreditThisWeek(accountNo);
		
		
			
		}

	private void handleDebitCount(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		
		
		
		Long accountNo = rootNode.path("account_no").asLong();
		int debitCount = dao.getTotalDebitThisWeek(accountNo);
		
		
		}

	private List<Map<String, Object>>  handleTransactionHistory(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
		BufferedReader reader = request.getReader();
		String jsonBody = reader.lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		   String accountNo = rootNode.path("account_no").asText();
		   
		   return (List<Map<String, Object>>) dao.getTransactionHistory(accountNo);
			
		}

	private List<Map<String, Object>>  handleViewTransaction(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
		
		BufferedReader reader = request.getReader();
		String jsonBody = reader.lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		   String accountNo = rootNode.path("account_no").asText();
		   
		   return (List<Map<String, Object>>) dao.getTransactions(accountNo);
		    
		
		}

	private boolean handleTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException, SQLException, com.zoho.training.exceptions.TaskException {
	
			HttpSession session = request.getSession(false);
			if (session == null || session.getAttribute("personId") == null) {
				request.setAttribute("errorMessage", "session expired");
				request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

			}
			String sessionPersonId = (String) session.getAttribute("personId");
			BufferedReader reader = request.getReader();
			
			Transaction debit =mapper.readValue(reader,Transaction.class);
			String jsonBody = reader.lines().collect(Collectors.joining());
			JsonNode rootNode = mapper.readTree(jsonBody);
		   Boolean otherBank = rootNode.path("other_bank").asBoolean();
			long transactionTime = System.currentTimeMillis();
	        debit.setDateTime(transactionTime);
	        debit.setTransactionBy(sessionPersonId);
			
			
			return service.processtransaction(debit,otherBank);
			
	}

	public boolean handleDeposit(HttpServletRequest request , HttpServletResponse response) throws IOException, ServletException, SQLException, TaskException
	{
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

		}
		String sessionPersonId = (String) session.getAttribute("personId");
		BufferedReader reader = request.getReader();
		
		Transaction credit =mapper.readValue(reader,Transaction.class);
		long transactionTime = System.currentTimeMillis();
        credit.setDateTime(transactionTime);
        credit.setTransactionBy(sessionPersonId);
        return service.processDeposit(credit);
		
	}
	
	public boolean handleWithdraw(HttpServletRequest request , HttpServletResponse response) throws IOException, ServletException, SQLException, TaskException
	{
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("personId") == null) {
			request.setAttribute("errorMessage", "session expired");
			request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
		}
		String sessionPersonId = (String) session.getAttribute("personId");
		BufferedReader reader = request.getReader();
		
		Transaction debit =mapper.readValue(reader,Transaction.class);
		long transactionTime = System.currentTimeMillis();
        debit.setDateTime(transactionTime);
        debit.setTransactionBy(sessionPersonId);
        return service.processWithdraw(debit);
		
	}
}
