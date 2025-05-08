package com.bank.fintrustbank.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request, HttpServletResponse response)  {

        
		String endpoint = request.getPathInfo();
		System.out.println(endpoint);
		EndpointDispatcher dispatcher = new EndpointDispatcher();
		try {
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
}
