package com.bank.fintrustbank.util;


import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import com.bank.fintrustbank.dao.PrivilegedUserDAO;
import com.bank.fintrustbank.dao.SessionTokenDAO;
import com.bank.fintrustbank.model.SessionToken;
import com.zoho.training.exceptions.TaskException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.JwtBuilder;


public class SessionUtil {
	
	
	private static SecretKey SECRET_KEY ;
	
    private static final long EXPIRATION_MS = 1000 * 60 * 30; 
    private static final SessionTokenDAO sessionTokenDAO = new SessionTokenDAO();
    
    public static String generateToken(String personId , String role , String name , String status) throws SQLException, TaskException  {
        String jti = UUID.randomUUID().toString();
        Long issuedAt = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                .setSubject(personId)
                .setId(jti)
                .setIssuedAt(new Date())
                .setExpiration(new Date(issuedAt+ EXPIRATION_MS))
                .claim("role",role)
                .claim("name", name)
                .claim("status",status);
        
        if ("ADMIN".equalsIgnoreCase(role) || "SUPERADMIN".equalsIgnoreCase(role)) {
    		String branchId  =  new PrivilegedUserDAO().getBranch(personId);

            builder.claim("branchId", branchId);
        }


        String token = builder.signWith(SECRET_KEY).compact();

        
        sessionTokenDAO.saveOrUpdate(new SessionToken(personId, jti , issuedAt,"ACCESS", "ACTIVE"));
        return token;
        
    }

    public static boolean validateToken(String token , String sessionType ) {
        try {
            Claims claims = parseClaims(token);
            String personId = claims.getSubject();
            String jti = claims.getId();
            String storedJti = sessionTokenDAO.getJtiByPersonId(personId, sessionType);
            return jti.equals(storedJti);
   
        } catch (Exception e) {
            return false;
        }
    }

    public static Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static void invalidateToken(String personId , String sessionType  ,  String newStatus) throws SQLException, TaskException {
        sessionTokenDAO.deleteByPersonId(personId ,sessionType , newStatus);
    }

    public static String extractPersonId(String token) {
        return parseClaims(token).getSubject();
    }
    
    public static void setSecretKey(String secret) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 32 characters.");
        }
        SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
 public static String createLongTermToken(String personId, String name, String status, String role)
        throws TaskException, SQLException {

	// sessionTokenDAO.deleteByPersonId(personId, "ACCESS" ,"REVOKED") ; 
    String jti = UUID.randomUUID().toString();
    long issuedAt = System.currentTimeMillis();

    long oneYearMs = 1000L * 60 * 60 * 24 * 365;

    JwtBuilder builder = Jwts.builder()
            .setSubject(personId)
            .setId(jti)
            .setIssuedAt(new Date(issuedAt))
            .setExpiration(new Date(issuedAt + oneYearMs))
            .claim("role", role)
            .claim("name", name)
            .claim("status", status);

    if ("ADMIN".equalsIgnoreCase(role) || "SUPERADMIN".equalsIgnoreCase(role)) {
        String branchId = new PrivilegedUserDAO().getBranch(personId);
        builder.claim("branchId", branchId);
    }

    String token = builder.signWith(SECRET_KEY).compact();
    sessionTokenDAO.saveOrUpdate(new SessionToken(personId, jti , issuedAt,"LONGTERM" , "ACTIVE"));
    return token;
    
}             
 
}

