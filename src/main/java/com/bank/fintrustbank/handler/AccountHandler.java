package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.bank.fintrustbank.dao.AccountDAO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.zoho.training.exceptions.TaskException;

public class AccountHandler implements HttpRequestHandler {
	
	private static final ObjectMapper mapper = new ObjectMapper()
	        .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE).findAndRegisterModules();
	AccountDAO accountDAO = new AccountDAO();
	@Override
	public void doPatch(HttpServletRequest request , HttpServletResponse response) throws TaskException
	{
		String path = request.getPathInfo();
		try 
		{
		if(path.equals("/account/status"))
		{
			handleUpdateAccountStatus(request,response);
		}
		
	 }catch (IOException    e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(), e);
		}
	}

	private boolean handleUpdateAccountStatus(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException {
	
		HttpSession session = request.getSession(false);
		String sessionPersonId = (String) session.getAttribute("personId");
		
		String jsonBody = new BufferedReader(request.getReader()).lines().collect(Collectors.joining());
		JsonNode rootNode = mapper.readTree(jsonBody);
		
		String status = rootNode.path("status").asText();
		Long accountNo = rootNode.path("account_no").asLong();
		Long modifiedAt = System.currentTimeMillis();
		
		return accountDAO.updateStatus(accountNo, status, modifiedAt, sessionPersonId);
	}

}
