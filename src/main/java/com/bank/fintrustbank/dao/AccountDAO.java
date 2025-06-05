package com.bank.fintrustbank.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.enums.AccountField;
import com.bank.fintrustbank.enums.BranchField;
import com.bank.fintrustbank.enums.PersonField;
import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.model.Branch;
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

		Query addAccountQuery = new QueryBuilder().insert(AccountField.ACCOUNT_NO)
				.values(account.getAccountNo(), account.getCustomerId(), account.getBranchId(),
						account.getAccountType(), account.getBalance(), account.getStatus(), account.getCreatedAt(),
						account.getModifiedAt(), account.getModifiedBy())
				.build();
		System.out.println(addAccountQuery.getQuery());
		return addAccountQuery;
	}

	public Query getUpdateStatusQuery(String customerId, String status, Long modifiedAt, String sessionPersonId)
			throws TaskException {
		Query updateStatusQuery = new QueryBuilder().update(AccountField.ACCOUNT_NO).set(AccountField.ACCOUNT_STATUS, status)
				.set(AccountField.MODIFIED_AT, modifiedAt).set(AccountField.MODIFIED_BY, sessionPersonId)
				.where(AccountField.CUSTOMER_ID, "=", "customerId").build();
		return updateStatusQuery;
	}

	public boolean updateStatus(Long accountNo, String status, Long modifiedAt, String sessionPersonId)
			throws TaskException, SQLException {
		Query updateStatusQuery = new QueryBuilder().update(AccountField.ACCOUNT_NO).set(AccountField.ACCOUNT_STATUS, status)
				.set(AccountField.MODIFIED_AT, modifiedAt).set(AccountField.MODIFIED_BY, sessionPersonId).where(AccountField.ACCOUNT_NO, "=", accountNo)
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

		Query updateStatusQuery = new QueryBuilder().update(AccountField.ACCOUNT_NO).set(AccountField.BRANCH_ID, branchId)
				.set(AccountField.MODIFIED_AT, modifiedAt).set(AccountField.MODIFIED_BY, sessionPersonId).where(AccountField.ACCOUNT_NO, "=", accountNo)
				.build();
		int result = qe.execute(updateStatusQuery.getQuery(), updateStatusQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}
	
	public Double getBalance(long accountNo) throws TaskException, SQLException {

		Query getBalanceQuery = new QueryBuilder().select(AccountField.BALANCE).from(AccountField.ACCOUNT_NO)
				.where(AccountField.ACCOUNT_NO, "=", accountNo).build();
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
		Query getCustomerIdQuery = new QueryBuilder().select(AccountField.CUSTOMER_ID).from(AccountField.ACCOUNT_NO)
				.where(AccountField.ACCOUNT_NO, "=", accountNo).build();

		System.out.println(getCustomerIdQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getCustomerIdQuery.getQuery(),
				getCustomerIdQuery.getValues());
		System.out.println(result);
		Map<String, Object> resultMap = result.get(0);
		String customerId = (String) resultMap.get("customer_id");
		return customerId;
	}

	public Query updateBalance(Long accountNo, Double balance) throws TaskException {
		Query updateBalance = new QueryBuilder().update(AccountField.ACCOUNT_NO).set(AccountField.BALANCE, balance)
				.where(AccountField.ACCOUNT_NO, "=", accountNo).build();

		return updateBalance;
	}

	public List<Map<String, Object>> getAccounts(String branchId) throws TaskException, SQLException

	{

//		Query getAccounts = new QueryBuilder()
//				.select(AccountField.CUSTOMER_ID, "customer.name", AccountField.ACCOUNT_NO, "account.account_type",
//						"account.balance", "account.account_status", "account.created_at", "account.modified_at",
//						"account.modified_by", "modifier.name")
//				.from(AccountField.ACCOUNT_NO)
//				.join("INNER", "person", "customer", new OnClause("customer.person_id", "=", "account.customer_id"))
//				.join("INNER", "person", "modifier", new OnClause("account.modified_by", "=", "modifier.person_id"))
//				.where("branch_id", "=", branchId).build();

		
		Query getAccounts = new QueryBuilder()
		        .select(
		            AccountField.CUSTOMER_ID,
		           PersonField.NAME.withAlias("customer"),
		            AccountField.ACCOUNT_NO,
		            AccountField.ACCOUNT_TYPE,
		            AccountField.BALANCE,
		            AccountField.ACCOUNT_STATUS,
		            AccountField.CREATED_AT,
		            AccountField.MODIFIED_AT,
		            AccountField.MODIFIED_BY,
		            PersonField.NAME.withAlias("modifier")
		  
		        )
		        .from(AccountField.ACCOUNT_NO)
		       
		        .join("INNER", PersonField.PERSON_ID , "customer",
		            new OnClause(PersonField.PERSON_ID.withAlias("customer"), "=", AccountField.CUSTOMER_ID )
		        )
		        .join("INNER",PersonField.PERSON_ID, "modifier",
		            new OnClause(AccountField.MODIFIED_BY, "=", PersonField.PERSON_ID.withAlias("modifier"))
		        )
		        .where(AccountField.BRANCH_ID, "=", "101")
		        .build();
		List<Map<String, Object>> result = qe.executeQuery(getAccounts.getQuery(), getAccounts.getValues());
		System.out.println(result);
		return result;

	}
	public  List<Map<String, Object>>  getUserAccounts(String personId) throws TaskException, SQLException
	{
		
		/*
		 * Query getUserAccounts = new QueryBuilder()
		 * .select(AccountField.ACCOUNT_NO,"branch_info.branch_id",
		 * "branch_info.location" , "account.account_type" , "account.balance")
		 * .from(AccountField.ACCOUNT_NO) .join("INNER", "branch","branch_info", new
		 * OnClause ("branch_info.branch_id","=","account.branch_id"))
		 * .where("customer_id","=",personId).build();
		 */
		Query getUserAccounts  = new QueryBuilder()
				.select(AccountField.ACCOUNT_NO,BranchField.BRANCH_ID.withAlias("branch_info"),BranchField.LOCATION.withAlias("branch_info") ,AccountField.ACCOUNT_TYPE, AccountField.BALANCE)
				.from(AccountField.ACCOUNT_NO)
				.join("INNER", BranchField.BRANCH_ID,"branch_info", new OnClause (BranchField.BRANCH_ID.withAlias("branch_info"),"=",AccountField.BRANCH_ID))
				.where(AccountField.CUSTOMER_ID,"=",personId).build();
		List<Map<String, Object>> result = qe.executeQuery(getUserAccounts .getQuery(), getUserAccounts .getValues());
		System.out.println(result);
		return result;
		
	}
	
	public List<Map<String,Object>>  getAccountNo(String personId) throws TaskException, SQLException
	{
		Query getAccountNo = new QueryBuilder()
				.select(AccountField.ACCOUNT_NO)
				.from(AccountField.ACCOUNT_NO)
				.where(AccountField.CUSTOMER_ID, "=", personId)
				.build();
		
		List<Map<String, Object>> result = qe.executeQuery(getAccountNo .getQuery(), getAccountNo.getValues());
		System.out.println(result);
		return result;
		
	}
	

	
}
