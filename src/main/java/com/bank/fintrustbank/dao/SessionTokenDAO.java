package com.bank.fintrustbank.dao;

import com.bank.fintrustbank.enums.SessionTokenField;
import com.bank.fintrustbank.model.SessionToken;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SessionTokenDAO {
	

	private final QueryExecutor qe = new QueryExecutor();
	
    public boolean saveOrUpdate(SessionToken token) throws SQLException, TaskException {
      
    	deleteByPersonId(token.getPersonId() , token.getSessionType() ,"REVOKED") ; 
    	
        Query saveQuery  = new QueryBuilder()
        		.insert(SessionTokenField.PERSON_ID)
        		.values(token.getPersonId(),token.getJti() ,token.getIssuedAt(), token.getSessionType() , token.getSessionStatus())
        		.build();
        
        System.out.println(saveQuery.getQuery());
    
		int result = qe.execute(saveQuery.getQuery(), saveQuery.getValues());

		
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;
        
    
    }

    public String getJtiByPersonId(String personId , String sessionType) throws SQLException, TaskException {
       
        Query getJti = new QueryBuilder()
        		.select(SessionTokenField.JTI)
        		.from(SessionTokenField.PERSON_ID)
        		.where(SessionTokenField.PERSON_ID, "=", personId)
        		.and(SessionTokenField.SESSION_STATUS,"=" ,"ACTIVE")
        		.and(SessionTokenField.SESSION_TYPE,"=" ,sessionType)
        		.build();
        
        System.out.println(getJti.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getJti.getQuery(), getJti.getValues());
	
		if(result.isEmpty())
		{
			return null ; 
		}
		
		return (String) result.get(0).get("jti");

    }

    public boolean deleteByPersonId(String personId, String sessionType, String newStatus) throws SQLException, TaskException {
        
        Query updateStatus = new QueryBuilder()
        		.update(SessionTokenField.PERSON_ID)
        		.set(SessionTokenField.SESSION_STATUS ,newStatus )
        		.where(SessionTokenField.PERSON_ID, "=", personId)
        		.and(SessionTokenField.SESSION_TYPE, "=", sessionType)
        		.and(SessionTokenField.SESSION_STATUS, "=", "ACTIVE")
        		.build();
        
        System.out.println(updateStatus.getQuery());
     		int result = qe.execute(updateStatus.getQuery(), updateStatus.getValues());
     		System.out.println(result);
     		if (result > 0) {
     			return true;
     		}
     		return false;
     		
    }

}

