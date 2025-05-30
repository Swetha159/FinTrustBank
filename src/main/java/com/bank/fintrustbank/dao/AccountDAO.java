package com.bank.fintrustbank.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Condition;
import querybuilder.OnClause;
import querybuilder.Query;
import querybuilder.QueryBuilder;

public class AccountDAO {

	QueryExecutor qe = new QueryExecutor();

	public boolean addAccount(Account account) throws TaskException, SQLException {

		Query addAccountQuery = getInsertQuery(account);
		System.out.println(addAccountQuery.getQuery());
		int result = qe.execute(addAccountQuery.getQuery(), addAccountQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}

	public Query getInsertQuery(Account account) throws TaskException {

		Query addAccountQuery = new QueryBuilder().insert("account")
				.values(account.getAccountNo(), account.getCustomerId(), account.getBranchId(),
						account.getAccountType(), account.getBalance(), account.getStatus(), account.getCreatedAt(),
						account.getModifiedAt(), account.getModifiedBy())
				.build();
		System.out.println(addAccountQuery.getQuery());
		return addAccountQuery;
	}

	public Query getUpdateStatusQuery(String customerId, String status, Long modifiedAt, String sessionPersonId)
			throws TaskException {
		Query updateStatusQuery = new QueryBuilder().update("account").set("account_status", status)
				.set("modified_at", modifiedAt).set("modified_by", sessionPersonId)
				.where("customer_id", "=", "customerId").build();
		return updateStatusQuery;
	}

	public boolean updateStatus(Long accountNo, String status, Long modifiedAt, String sessionPersonId)
			throws TaskException, SQLException {
		Query updateStatusQuery = new QueryBuilder().update("account").set("status", status)
				.set("modified_at", modifiedAt).set("modified_by", sessionPersonId).where("account_no", "=", accountNo)
				.build();
		int result = qe.execute(updateStatusQuery.getQuery(), updateStatusQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean updateBranch(Long accountNo, String branchId, Long modifiedAt, String sessionPersonId)
			throws TaskException, SQLException {

		Query updateStatusQuery = new QueryBuilder().update("account").set("branch_id", branchId)
				.set("modified_at", modifiedAt).set("modified_by", sessionPersonId).where("account_no", "=", accountNo)
				.build();
		int result = qe.execute(updateStatusQuery.getQuery(), updateStatusQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}
	
	public Double getBalance(long accountNo) throws TaskException, SQLException {

		Query getBalanceQuery = new QueryBuilder().select("balance").from("account")
				.where("account_no", "=", accountNo).build();
          System.out.println(accountNo);
		System.out.println(getBalanceQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getBalanceQuery.getQuery(), getBalanceQuery.getValues());
		System.out.println(result);
		if(result.isEmpty())
		{
			return null ; 
		}else
		{
		Map<String, Object> resultMap = result.get(0);
		BigDecimal balanceDecimal = (BigDecimal) resultMap.get("balance");
		double balance = balanceDecimal.doubleValue();

		return balance;
		}

	}

	public String getCustomerId(long accountNo) throws TaskException, SQLException {
		Query getCustomerIdQuery = new QueryBuilder().select("customer_id").from("account")
				.where("account_no", "=", accountNo).build();

		System.out.println(getCustomerIdQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getCustomerIdQuery.getQuery(),
				getCustomerIdQuery.getValues());
		System.out.println(result);
		Map<String, Object> resultMap = result.get(0);
		String customerId = (String) resultMap.get("customer_id");
		return customerId;
	}

	public Query updateBalance(Long accountNo, Double balance) throws TaskException {
		Query updateBalance = new QueryBuilder().update("account").set("balance", balance)
				.where("account_no", "=", accountNo).build();

		return updateBalance;
	}

	public List<Map<String, Object>> getAccounts(String branchId) throws TaskException, SQLException

	{

		Query getAccounts = new QueryBuilder()
				.select("account.customer_id", "customer.name", "account.account_no", "account.account_type",
						"account.balance", "account.account_status", "account.created_at", "account.modified_at",
						"account.modified_by", "modifier.name")
				.from("account")
				.join("INNER", "person", "customer", new OnClause("customer.person_id", "=", "account.customer_id"))
				.join("INNER", "person", "modifier", new OnClause("account.modified_by", "=", "modifier.person_id"))
				.where("branch_id", "=", branchId).build();

		List<Map<String, Object>> result = qe.executeQuery(getAccounts.getQuery(), getAccounts.getValues());
		System.out.println(result);
		return result;

	}
	public  List<Map<String, Object>>  getUserAccounts(String personId) throws TaskException, SQLException
	{
		
		Query getUserAccounts  = new QueryBuilder()
				.select("account_no","branch_info.branch_id", "branch_info.location" , "account.account_type" , "account.balance")
				.from("account")
				.join("INNER", "branch","branch_info", new OnClause ("branch_info.branch_id","=","account.branch_id"))
				.where("customer_id","=",personId).build();
		
		List<Map<String, Object>> result = qe.executeQuery(getUserAccounts .getQuery(), getUserAccounts .getValues());
		System.out.println(result);
		return result;
		
	}
	
	public List<Map<String,Object>>  getAccountNo(String personId) throws TaskException, SQLException
	{
		Query getAccountNo = new QueryBuilder()
				.select("account_no")
				.from("account")
				.where("customer_id", "=", personId)
				.build();
		
		List<Map<String, Object>> result = qe.executeQuery(getAccountNo .getQuery(), getAccountNo.getValues());
		System.out.println(result);
		return result;
		
	}
	

	
}
