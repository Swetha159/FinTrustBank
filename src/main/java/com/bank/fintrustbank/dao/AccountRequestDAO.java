package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.OnClause;
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
	
	public boolean  rejectRequest(String personId) throws TaskException, SQLException
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
	
	public List<Map<String, Object>> getAccountRequests(String branchId, String status) throws TaskException, SQLException
	{
	
		Query getAccountRequest  = new QueryBuilder()
				.select("person.person_id" , "person.name", "person.email","person.phone_number" , "person.dob" , "person.aadhar", "person.pan","person.address","person.created_at","person.modified_by","modifier.name","account_request.account_type")
				.from("account_request")
				.join("INNER", "account_request", new OnClause("account_request.person_id","=", "person.person_id"))
				.join("INNER",  "person", "modifier" , new OnClause("person.modified_by","=","modifier.person_id"))
				.where("request_status", "=", status)
				.and("branch_id", "=",  branchId)
				.build();
		System.out.println(getAccountRequest.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getAccountRequest.getQuery(), getAccountRequest.getValues());
		System.out.println(result);
		if(result.isEmpty())
		{
			return null ; 
		}
		return result ; 
				
	}

}
