package com.bank.fintrustbank.util;

import io.jsonwebtoken.Claims;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class TokenUtil {

	private static String sessionType ; 
    public static Claims getTokenClaims(HttpServletRequest request) {
        String token = extractTokenFromCookies(request);
        if (token == null) {
            return null;
            
        }

        try {
            boolean isValid = SessionUtil.validateToken(token , sessionType);
            if (!isValid) {
                return null;
            }

            return SessionUtil.parseClaims(token);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String extractTokenFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("auth_token".equals(cookie.getName())) {
            	sessionType = "ACCESS"; 
                return cookie.getValue();
            }
            else if ("long_term_token".equals(cookie.getName()))
            {
            	sessionType = "LONGTERM";
                return cookie.getValue();

            }
            else
            {
            	System.out.println("TOKEN IS NULL");
            	
            }
        }
        return null;
    }
}
