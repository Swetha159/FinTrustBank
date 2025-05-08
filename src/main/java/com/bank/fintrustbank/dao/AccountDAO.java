package com.bank.fintrustbank.dao;

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
	
}
