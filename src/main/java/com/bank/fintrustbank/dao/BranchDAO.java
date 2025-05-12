package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class BranchDAO {

	QueryExecutor qe = new QueryExecutor();

	public boolean addBranch(Branch branch) throws TaskException {

		Query insertQuery = new QueryBuilder()
				.insert("branch").values(branch.getBranchId(), branch.getManagerId(), branch.getIfscCode(),
						branch.getLocation(), branch.getCreatedAt(), branch.getModifiedAt(), branch.getModifiedBy())
				.build();
		System.out.println(insertQuery.getQuery());
		int result = qe.execute(insertQuery.getQuery(), insertQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;
	}

	public boolean updateBranch(Branch branch) throws TaskException {
		List<String>  excludeColumns = Arrays.asList("branch_id","ifsc","created_at" , "manager_id");
		Query updateQuery = EditUtil.update(branch, "branch", "branch_id",excludeColumns);
	
	System.out.println(updateQuery.getQuery());

	int result = qe.execute(updateQuery.getQuery(), updateQuery.getValues());
	System.out.println(result);
	if(result>0)
	{
		return true;
	}return false;

	}

public String getSuperAdmin(String branchId) throws TaskException, SQLException {
		
		Query  getSuperAdminQuery  = new QueryBuilder()
				.select("manager_id")
			    .from("branch")
			    .where("branch_id","=", branchId)
			    .build();
		System.out.println(getSuperAdminQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getSuperAdminQuery.getQuery(), getSuperAdminQuery.getValues());

	    if (result.isEmpty()) {
	        return null;
	    }

	    return (String) result.get(0).get("manager_id");
		
	}

public Query getUpdateAdminQuery(String branchId,String personId) throws TaskException {
	
	Query updateAdminQuery = new QueryBuilder()
			.update("branch")
			.set("manager_id",personId)
			.where("branch_id", "=" ,branchId)
			.build();
	return updateAdminQuery;
}
	
	
}
