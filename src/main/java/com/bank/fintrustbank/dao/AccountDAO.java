package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class AccountDAO {

	QueryExecutor qe = new QueryExecutor();
	public boolean addAccount(Account account) throws TaskException {
		
		Query addAccountQuery  = getInsertQuery(account);
				System.out.println(addAccountQuery.getQuery());
				int result = qe.execute(addAccountQuery.getQuery(), addAccountQuery.getValues());
				System.out.println(result);
				if (result > 0) {
					return true;
				}
				return false;	
			
	}
	
public Query getInsertQuery(Account account) throws TaskException {
		
		Query addAccountQuery  = new QueryBuilder()
				.insert("account")
				.values(account.getAccountNo(),account.getCustomerId(),account.getBranchId(),account.getAccountType(),account.getBalance(),
						account.getStatus(),account.getCreatedAt(),account.getModifiedAt(),account.getModifiedBy())
				.build();
				System.out.println(addAccountQuery.getQuery());	
				return addAccountQuery;
	}

public Query getUpdateStatusQuery (String customerId,String status, Long modifiedAt, String sessionPersonId) throws TaskException {
	Query updateStatusQuery = new QueryBuilder()
			.update("account")
			.set("status", status)
			.set("modified_at", modifiedAt).set("modified_by", sessionPersonId)
			.where("customer_id","=", "customerId")
			.build();
	return updateStatusQuery ;
}

public boolean updateStatus(Long accountNo,String status, Long modifiedAt, String sessionPersonId) throws TaskException
{
	Query updateStatusQuery = new QueryBuilder()
			.update("account")
			.set("status", status)
			.set("modified_at", modifiedAt).set("modified_by", sessionPersonId)
			.where("account_no","=",accountNo)
			.build();
	int result = qe.execute(updateStatusQuery.getQuery(),updateStatusQuery.getValues());
	System.out.println(result);
	if (result > 0) {
		return true;
	}
	return false;
}

public boolean updateBranch(Long accountNo, String branchId, Long modifiedAt, String sessionPersonId) throws TaskException {
	
	
	
	
	
	Query updateStatusQuery = new QueryBuilder()
			.update("account")
			.set("branch_id", branchId)
			.set("modified_at", modifiedAt).set("modified_by", sessionPersonId)
			.where("account_no","=",accountNo)
			.build();
	int result = qe.execute(updateStatusQuery.getQuery(),updateStatusQuery.getValues());
	System.out.println(result);
	if (result > 0) {
		return true;
	}
	return false;
	
}

public Double getBalance(long accountNo) throws TaskException, SQLException {
	
	Query getBalanceQuery = new QueryBuilder()
			.select("balance")
			.from("account")
			.where("account_no","=","accountNo")
			.build();
	
	System.out.println(getBalanceQuery.getQuery());
	List<Map<String, Object>> result = qe.executeQuery(getBalanceQuery.getQuery(), getBalanceQuery.getValues());
	System.out.println(result);
	Map<String, Object> resultMap = result.get(0);
	Double balance = (Double) resultMap.get("balance");
	return balance;
	
	
}

public String getCustomerId(long accountNo) throws TaskException, SQLException {
	Query  getCustomerIdQuery = new QueryBuilder()
			.select("customer_id")
			.from("account")
			.where("account_no", "=",accountNo)
			.build();
	
	System.out.println(getCustomerIdQuery.getQuery());
	List<Map<String, Object>> result = qe.executeQuery(getCustomerIdQuery.getQuery(), getCustomerIdQuery.getValues());
	System.out.println(result);
	Map<String, Object> resultMap = result.get(0);
	String customerId = (String) resultMap.get("customer_id");
	return customerId;
}
	
}
