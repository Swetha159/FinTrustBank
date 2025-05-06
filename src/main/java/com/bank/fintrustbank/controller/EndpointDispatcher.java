package com.bank.fintrustbank.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.handler.HttpRequestHandler;

public class EndpointDispatcher {
	

	public void dispatch(String endpoint, Map<String, Map<String, String>> endpointConfig, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		if (endpointConfig == null) {
			(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}

		System.out.println("Endpoint: " + endpoint);
		System.out.println(endpointConfig.containsKey(endpoint));

		if (endpointConfig.containsKey(endpoint)) {
			String handlerClassName = endpointConfig.get(endpoint).get("handler");
			System.out.println(handlerClassName);

			try {
				Class<?> handlerClass = Class.forName("com.bank.fintrustbank.handler." + handlerClassName);
				HttpRequestHandler handler = (HttpRequestHandler) handlerClass.getDeclaredConstructor().newInstance();

				switch ((request).getMethod()) {
				case "GET":
					handler.doGet(request, response);
					break;
				case "POST":
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
			} catch (Exception e) {
				e.printStackTrace();
				( response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error processing the request");
			}
		} else {
			( response).sendError(HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
		}

	}

}
