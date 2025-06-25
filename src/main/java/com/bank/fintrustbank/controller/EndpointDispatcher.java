package com.bank.fintrustbank.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.handler.HttpRequestHandler;
import com.zoho.training.exceptions.TaskException;

public class EndpointDispatcher {
	

	public void dispatch(String endpoint, Map<String, Map<String, String>> endpointConfig, HttpServletRequest request,
			HttpServletResponse response) throws TaskException {

		if (endpointConfig == null) {
			(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		System.out.println("Endpoint: " + endpoint);
		System.out.println(endpointConfig.containsKey(endpoint));
		try {
		if (endpointConfig.containsKey(endpoint)) { 
			String handlerClassName = endpointConfig.get(endpoint).get("handler");
			System.out.println("handler class name"+handlerClassName);

			
				Class<?> handlerClass = Class.forName("com.bank.fintrustbank.handler." + handlerClassName);
				HttpRequestHandler handler = (HttpRequestHandler) handlerClass.getDeclaredConstructor().newInstance();
                System.out.println(request.getMethod());
				switch ((request).getMethod()) {
				case "GET":
					handler.doGet(request, response);
					break;
				case "POST":
					System.out.println("inside dispatch");
					handler.doPost(request, response);
					break;
				case "PUT":
					handler.doPut(request, response);
					break;
				case "DELETE":
					handler.doDelete(request, response);
					break;
				default:
					( response).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Unsupported HTTP method");
				}
			} 
		 else {
			request.setAttribute("errorMessage","Endpoint not found" );
			 RequestDispatcher requestDispatcher = request.getRequestDispatcher("/WEB-INF/error/error.jsp");
		        requestDispatcher.forward(request, response);
		}
	}
		catch (TaskException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | IOException | ServletException  e) {
			e.printStackTrace();
			throw new TaskException(e.getMessage(),e);

		}

	}

}
