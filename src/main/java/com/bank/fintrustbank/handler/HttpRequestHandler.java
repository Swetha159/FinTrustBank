package com.bank.fintrustbank.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

	
	public interface HttpRequestHandler {
	    default void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET not supported");
	    }

	    default void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST not supported");
	    }

	    default void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "PUT not supported");
	    }

	    default void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "DELETE not supported");
	    }
	}

