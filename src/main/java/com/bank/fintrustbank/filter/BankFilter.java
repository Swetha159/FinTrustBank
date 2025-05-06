package com.bank.fintrustbank.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.controller.EndpointDispatcher;

public class BankFilter implements Filter {

	private Map<String, Map<String, String>> endpointConfig;
	@Override
	public void init(FilterConfig config)
	{
		 ServletContext context = config.getServletContext();
		    context.log("Initializing endpoint config...");

		    endpointConfig = (Map<String, Map<String, String>>) context.getAttribute("endpointConfig");
		    
		    System.out.println(endpointConfig);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	        throws IOException, ServletException {
		 HttpServletRequest httpRequest = (HttpServletRequest) request;
		 HttpServletResponse httpResponse = (HttpServletResponse) response;
	    
		String endpoint = httpRequest.getPathInfo();
	
	    System.out.println(endpoint);
	    if (endpointConfig == null) {
			httpResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
	    if(endpointConfig.containsKey(endpoint))
	    {
	    	 chain.doFilter(request, response);
	    }
	    else
	    {
	    	  httpRequest.setAttribute("errorMessage", "The requested endpoint '" + endpoint + "' was not found.");
	    	    httpRequest.getRequestDispatcher("/WEB_INF/error/error.jsp").forward(request, response);
	    }
	  
	}


}
