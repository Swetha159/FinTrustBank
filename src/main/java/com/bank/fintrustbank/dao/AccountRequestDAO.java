package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.enums.AccountRequestField;
import com.bank.fintrustbank.enums.PersonField;
import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.OnClause;
import querybuilder.Query;
import querybuilder.QueryBuilder;

public class AccountRequestDAO {

	QueryExecutor qe = new QueryExecutor();
	public boolean addRequest(AccountRequest acRequest) throws TaskException, SQLException {

		Query addRequestQuery = new QueryBuilder().insert(AccountRequestField.PERSON_ID)
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
		Query insertQuery = new QueryBuilder().insert(AccountRequestField.PERSON_ID)
				.values(acRequest.getPersonId(), acRequest.getBranchId(),acRequest.getAccountType(), acRequest.getRequestStatus(),
						acRequest.getCreatedAt(), acRequest.getModifiedAt(), acRequest.getModifiedBy())
				.build();
		System.out.println(insertQuery.getQuery());
		return insertQuery;
	}
	public Query getAcceptRequestQuery(String personId  , Long modifiedAt , String modifiedBy) throws TaskException
	{
		Query requestStatusQuery = new QueryBuilder()
									.update(AccountRequestField.PERSON_ID)
									.set(AccountRequestField.REQUEST_STATUS,"ACCEPTED" )
									.set(AccountRequestField.MODIFIED_AT, modifiedAt)
									.set(AccountRequestField.MODIFIED_BY, modifiedBy)
									.where(AccountRequestField.PERSON_ID, "=", personId)
									.build();
		return requestStatusQuery ; 
				
	}
	
	public Query  getRejectRequestQuery(String personId , Long modifiedAt , String sessionPersonId) throws TaskException, SQLException
	{
		Query rejectRequestQuery = new QueryBuilder()
									.update(AccountRequestField.PERSON_ID)
									.set(AccountRequestField.REQUEST_STATUS ,"REJECTED")
									.set(AccountRequestField.MODIFIED_AT, modifiedAt)
									.set(AccountRequestField.MODIFIED_BY, sessionPersonId)
									.where(AccountRequestField.PERSON_ID, "=", personId)
									.build();
			return  rejectRequestQuery;
	}
	
	public List<Map<String, Object>> getAccountRequests(String branchId, String status) throws TaskException, SQLException
	{
	
/*		Query getAccountRequest  = new QueryBuilder()
				.select("person.person_id" , "person.name", "person.email","person.phone_number" , "person.dob" , "person.aadhar", "person.pan","person.address","person.created_at","person.modified_by","modifier.name  AS modifier_name ","account_request.account_type")
				.from(AccountRequestField.PERSON_ID)
				.join("INNER", "person", new OnClause("account_request.person_id","=", "person.person_id"))
				.join("INNER",  "person", "modifier" , new OnClause("person.modified_by","=","modifier.person_id"))
				.where(AccountRequestField.REQUEST_STATUS, "=", status)
				.and(AccountRequestField.BRANCH_ID, "=",  branchId)
				.build();*/
		

		Query getAccountRequest  = new QueryBuilder()
				.select(PersonField.PERSON_ID , PersonField.NAME,PersonField.EMAIL,PersonField.PHONE_NUMBER , PersonField.DOB , PersonField.AADHAR, PersonField.PAN,PersonField.ADDRESS,PersonField.CREATED_AT,PersonField.MODIFIED_BY,PersonField.NAME.withAlias("modifier").as("modifier_name"), AccountRequestField.ACCOUNT_TYPE)
				.from(AccountRequestField.PERSON_ID)
				.join("INNER",PersonField.PERSON_ID, new OnClause(AccountRequestField.PERSON_ID,"=", PersonField.PERSON_ID ))
				.join("INNER",  PersonField.PERSON_ID, "modifier" , new OnClause(PersonField.MODIFIED_BY,"=",PersonField.PERSON_ID.withAlias("modifier")))
				.where(AccountRequestField.REQUEST_STATUS, "=", status)
				.and(AccountRequestField.BRANCH_ID, "=",  branchId)
				.build();
		System.out.println(getAccountRequest.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getAccountRequest.getQuery(), getAccountRequest.getValues());
		System.out.println(result);
		  return result.isEmpty() ? null : result;
				
	}

}
