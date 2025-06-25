
package com.bank.fintrustbank.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.util.TokenUtil;

import io.jsonwebtoken.Claims;

public class JwtAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        
        Claims claims = TokenUtil.getTokenClaims(request);

		String endpoint = request.getPathInfo();
		System.out.println(endpoint);
        if ( "/forgot-password".equals(endpoint) || "/reset-password".equals(endpoint)) {
            chain.doFilter(request, response);
            return;
        }
        if (claims == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

      
        request.setAttribute("personId", claims.getSubject());
        request.setAttribute("name", claims.get("name"));
        request.setAttribute("role", claims.get("role"));
        request.setAttribute("status", claims.get("status"));

        if (claims.get("branchId") != null) {
            request.setAttribute("branchId", claims.get("branchId", String.class));
        }
        chain.doFilter(request, response);
    }
}
