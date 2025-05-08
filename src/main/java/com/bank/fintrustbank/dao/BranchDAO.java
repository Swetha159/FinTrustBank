package com.bank.fintrustbank.dao;

import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;

import querybuilder.QueryBuilder;

public class BranchDAO {
	

	QueryExecutor qe = new QueryExecutor();

	public boolean addBranch(Branch branch) throws TaskException {
		
		Query insertQuery = new QueryBuilder()
				.insert("branch")
				.values(branch.getBranchId(),branch.getManagerId(),branch.getIfscCode(),branch.getLocation(),branch.getCreatedAt(),branch.getModifiedAt(),branch.getModifiedBy())
				.build();
		System.out.println(insertQuery.getQuery());
		int result = qe.execute(insertQuery.getQuery(), insertQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;	
	}
	
	public boolean update(String column , String value,String branchId) throws TaskException
	{
		Query updateQuery = new QueryBuilder()
				.update("branch")
				.set(column ,value)
				.where("branch_id" ,"=",branchId)
				.build();
		System.out.println(updateQuery.getQuery());
		int result = qe.execute(updateQuery.getQuery(), updateQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;	
	}

}
