package com.bank.fintrustbank.handler;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.AccountRequestDAO;
import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.util.IdCreation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;
public class NewAccountHandler implements HttpRequestHandler {

	 @Override
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  
			 String path = request.getPathInfo();
			 if(path.equals("/account-request"))
			 {
				 try {
					handleNewAccount(request, response);
				} catch (IOException | TaskException | SQLException e) {
					
					e.printStackTrace();
				}
				 
				 
			 }
			 
	  }
	 
	public static boolean handleNewAccount(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException, SQLException {
	
		   BufferedReader reader = request.getReader();
	        ObjectMapper mapper = new ObjectMapper();
	        mapper.findAndRegisterModules();
	        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
	        AccountRequest req = mapper.readValue(reader, AccountRequest.class);
	        req.setPersonId(IdCreation.createPersonId());
	        req.setRequestStatus("PENDING");
	        req.setModifiedAt((Long) null);       
	        req.setCreatedAt(System.currentTimeMillis());   
	        AccountRequestDAO dao = new AccountRequestDAO();
	        return dao.addRequest(req);
	}

	
}
