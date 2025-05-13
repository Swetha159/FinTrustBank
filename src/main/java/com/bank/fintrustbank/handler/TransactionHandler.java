package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.fintrustbank.dao.TransactionDAO;
import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.model.Transaction;
import com.bank.fintrustbank.service.TransactionService;
import com.bank.fintrustbank.util.TransactionIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class TransactionHandler implements HttpRequestHandler {

	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
	        .findAndRegisterModules();
	TransactionService service = new TransactionService();
		@Override
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws TaskException {
		  
		try {	 
		 String path = request.getPathInfo();
			 if(path.equals("/transaction"))
			 {
				 handleTransaction(request, response);

			 }
	 }
			 catch (IOException  | ServletException | SQLException e) {
					e.printStackTrace();
					throw new TaskException(e.getMessage(), e);
				}
			 
			 
	  }

	private boolean handleTransaction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, TaskException, SQLException {
	
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

}
