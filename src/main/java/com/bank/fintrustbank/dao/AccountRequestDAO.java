package com.bank.fintrustbank.dao;

import java.sql.SQLException;

import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class AccountRequestDAO {

	QueryExecutor qe = new QueryExecutor();
	public boolean addRequest(AccountRequest acRequest) throws TaskException, SQLException {

		
		Query addRequestQuery = new QueryBuilder().insert("account_request")
				.values(acRequest.getPersonId(), acRequest.getBranchId(),acRequest.getAccountType(), acRequest.getRequestStatus(),
						acRequest.getCreatedAt(), acRequest.getModifiedAt(), acRequest.getModifiedBy())
				.build();
		System.out.println(addRequestQuery.getQuery());
		int result = qe.execute(addRequestQuery.getQuery(), addRequestQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}
	
	public Query getInsertQuery(AccountRequest acRequest) throws TaskException 
	{
		Query insertQuery = new QueryBuilder().insert("account_request")
				.values(acRequest.getPersonId(), acRequest.getBranchId(),acRequest.getAccountType(), acRequest.getRequestStatus(),
						acRequest.getCreatedAt(), acRequest.getModifiedAt(), acRequest.getModifiedBy())
				.build();
		System.out.println(insertQuery.getQuery());
		return insertQuery;
	}
	public Query getAcceptRequestQuery(String personId) throws TaskException
	{
		Query requestStatusQuery = new QueryBuilder()
									.update("account_request")
									.set("request_status" ,"ACCEPTED" )
									.where("person_id", "=", personId)
									.build();
		return requestStatusQuery ; 
				
	}
	
	public boolean  rejectRequest(String personId) throws TaskException
	{
		Query rejectRequestQuery = new QueryBuilder()
									.update("account_request")
									.set("request_status" ,"REJECTED")
									.where("person_id", "=", personId)
									.build();
		System.out.println(rejectRequestQuery.getQuery());
		int result = qe.execute(rejectRequestQuery.getQuery(),rejectRequestQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;
		
				
	}
	
	

}
