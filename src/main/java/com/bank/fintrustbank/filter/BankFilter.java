package com.bank.fintrustbank.filter;

import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.util.SessionUtil;

public class BankFilter implements Filter {

	private Map<String, Map<String, String>> endpointConfig;

	@Override
	public void init(FilterConfig config) {
		ServletContext context = config.getServletContext();
		context.log("Initializing endpoint config...");

		endpointConfig = (Map<String, Map<String, String>>) context.getAttribute("endpointConfig");

		System.out.println(endpointConfig);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

		long startTime = System.currentTimeMillis();

		long gcTimeBefore = getTotalGCTime();
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
			if ("/login".equals(endpoint) || "/account-request".equals(endpoint) || "/forgot-password".equals(endpoint)
					|| "/reset-password".equals(endpoint)) {
				chain.doFilter(request, response);
				return;
			}
			String token = null;
			String sessionType = null;
			for (Cookie cookie : httpRequest.getCookies()) {
				if ("long_term_token".equals(cookie.getName())) {
					token = cookie.getValue();
					sessionType = "LONGTERM";
					break;
				}
				if ("auth_token".equals(cookie.getName())) {
					token = cookie.getValue();
					sessionType = "ACCESS";

					break;
				}

			}
			if (endpointConfig.containsKey(endpoint)) {
				if (!SessionUtil.validateToken(token, sessionType)) {
					request.setAttribute("errorMessage", "session expired");
					request.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
				}

				else {
					chain.doFilter(request, response);

				}

			} else {
				httpRequest.setAttribute("errorMessage", "The requested endpoint '" + endpoint + "' was not found.");
				httpRequest.getRequestDispatcher("/WEB-INF/error/error.jsp").forward(request, response);
			}
		} catch (IOException | ServletException e) {
			e.printStackTrace();
		}
		long gcTimeAfter = getTotalGCTime();
		long gcTimeThisRequest = gcTimeAfter - gcTimeBefore;

		long endTime = System.currentTimeMillis();

		System.out.println("Total Response Time: " + (endTime - startTime) + " ms");
		System.out.println("GC Time during this request: " + gcTimeThisRequest + " ms");
	}

	private long getTotalGCTime() {

		long total = 0;
		List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
		for (GarbageCollectorMXBean gcBean : gcBeans) {
			long time = gcBean.getCollectionTime();
			System.out.println("GC Count: " + gcBean.getCollectionCount());
			if (time != -1)
				total += time;
		}
		return total;
	}
}

