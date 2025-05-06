package com.bank.fintrustbank.controller;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.yaml.snakeyaml.Yaml;

import com.bank.fintrustbank.handler.HttpRequestHandler;
import com.bank.fintrustbank.util.EndpointConfigManager;

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

	private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {

        
		String endpoint = request.getPathInfo();
		System.out.println(endpoint);
		EndpointDispatcher dispatcher = new EndpointDispatcher();
		dispatcher.dispatch(endpoint,endpointConfig, request, response);
		
    
        }
}
