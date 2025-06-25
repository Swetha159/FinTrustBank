package com.bank.fintrustbank.dao;

import java.sql.SQLException;

import com.bank.fintrustbank.enums.RequestLogField;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class RequestLogDAO {

	private final QueryExecutor qe = new QueryExecutor();

    public boolean logRequest(String endpoint, String method, String requestId , long responseTimeMs) throws SQLException, TaskException {
   
        
        Query log = new QueryBuilder().insert(RequestLogField.RESPONSE_TIME_MS).values(endpoint , method,requestId ,  responseTimeMs , System.currentTimeMillis() )
        		.build();
		int result = qe.execute(log.getQuery(), log.getValues());
	
		if (result > 0) {
			return true;
		}
		return false;
    }
}
