package com.bank.fintrustbank.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.RequestLogDAO;
import com.zoho.training.exceptions.TaskException;


public class Bank extends HttpServlet {

	private Map<String, Map<String, String>> endpointConfig;

	@Override
	public void init() throws ServletException {
		 ServletContext context = getServletContext();
		    context.log("Initializing endpoint config...");
		    endpointConfig = (Map<String, Map<String, String>>) context.getAttribute("endpointConfig");
		    System.out.println(endpointConfig);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("inside doGet in servlet") ; 
		Long startTime = System.currentTimeMillis();
		
		processRequest(request, response);
		Long endTime =System.currentTimeMillis();
			Long responseTime = endTime - startTime ;
		
			logRequestToDB(request, "GET", responseTime);
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("inside doPost in servlet") ; 
		Long startTime = System.currentTimeMillis();
		processRequest(request, response);
		Long endTime =System.currentTimeMillis();
		Long responseTime = endTime - startTime ;
	
		logRequestToDB(request, "GET", responseTime);
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Long startTime = System.currentTimeMillis();
		processRequest(request, response);
		Long endTime =System.currentTimeMillis();
		Long responseTime = endTime - startTime ;
	
		logRequestToDB(request, "GET", responseTime);
	}
	
//	protected void doPatch(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//		System.out.println("inside doPatch in servlet") ; 
//		processRequest(request, response);
//	}
	
	private void processRequest(HttpServletRequest request, HttpServletResponse response)  {
	
		
		String endpoint = request.getServletPath();
		 if(endpoint.startsWith("/bank"))
		 {
			endpoint = request.getPathInfo();
		 }
		System.out.println("endpoint in servlet"+endpoint);
		EndpointDispatcher dispatcher = new EndpointDispatcher();
		try {
			System.out.println("inside process request");
		dispatcher.dispatch(endpoint,endpointConfig, request, response);
    
        }
		catch(TaskException e)
		{
			
			e.printStackTrace();
			request.setAttribute("errorMessage","Endpoint not found" );
			 RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/error/error.jsp");
		     try {
				requestDispatcher.forward(request, response);
			} catch (ServletException | IOException e1) {
				
				e1.printStackTrace();
			}
				 
		}
}
	private void logRequestToDB(HttpServletRequest request, String method, long responseTime) {
	    String endpoint = request.getServletPath();
	    if (endpoint.startsWith("/bank")) {
	        endpoint = request.getPathInfo();
	    }
         String requestId  = (String) request.getAttribute("requestId");
	    try {
	        RequestLogDAO dao = new RequestLogDAO();
	        dao.logRequest(endpoint, method,requestId ,responseTime);
	        System.out.println("Logged to DB: " + endpoint + ", time: " + responseTime + " ms");
	    } catch (SQLException | TaskException e) {
	        System.err.println("Failed to log request: " + e.getMessage());
	    }
	}

}
