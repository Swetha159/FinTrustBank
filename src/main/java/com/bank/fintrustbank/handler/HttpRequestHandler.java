package com.bank.fintrustbank.handler;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zoho.training.exceptions.TaskException;

	
	public interface HttpRequestHandler {
	    default void doGet(HttpServletRequest request, HttpServletResponse response) throws TaskException, IOException{
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "GET not supported");
	    }

	    default void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, TaskException {
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "POST not supported");
	    }

	    default void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException , TaskException{
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "PUT not supported");
	    }

	    default void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException , TaskException{
	        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "DELETE not supported");
	    }
	}

