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
import javax.servlet.http.HttpSession;

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
	         {
		 HttpServletRequest httpRequest = (HttpServletRequest) request;
		 HttpServletResponse httpResponse = (HttpServletResponse) response;
	    
		String endpoint = httpRequest.getPathInfo();
	
	    System.out.println(endpoint);
	    try {
	    if (endpointConfig == null) {
			  httpRequest.setAttribute("errorMessage", "Error Handling the request");
            httpRequest.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
            return;
		}
	    if ("/login".equals(endpoint) || "/account-request".equals(endpoint)) {
            chain.doFilter(request, response);
            return;
        }

        if (endpoint == null || endpoint.startsWith("/assets") || endpoint.startsWith("/css") || endpoint.startsWith("/js")) {
            chain.doFilter(request, response);
            return;
        }
	    if(endpointConfig.containsKey(endpoint))
	    {
	    	HttpSession session = httpRequest.getSession(false);
			if (session == null || session.getAttribute("personId") == null) {
				request.setAttribute("errorMessage", "session expired");
				request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);

			}
			else
			{
				 chain.doFilter(request, response);
			}
	    	
	    }
	    else
	    {
	    	  httpRequest.setAttribute("errorMessage", "The requested endpoint '" + endpoint + "' was not found.");
	    	    httpRequest.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
	    }
	    }
	    catch(IOException | ServletException e )
	    {
	    	e.printStackTrace();
	    }
	}


}
