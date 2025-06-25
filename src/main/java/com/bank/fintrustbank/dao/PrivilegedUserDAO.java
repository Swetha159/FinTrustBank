package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.enums.PrivilegedUserField;
import com.bank.fintrustbank.model.PrivilegedUser;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class PrivilegedUserDAO {

	private final QueryExecutor qe = new QueryExecutor();
	public Query getInsertQuery(PrivilegedUser privilegedUser) throws TaskException {
		
		Query insertQuery = new QueryBuilder()
							.insert(PrivilegedUserField.ADMIN_ID)
							.values(privilegedUser.getAdminId(),privilegedUser.getBranchId(),privilegedUser.getCreatedAt(),
									privilegedUser.getModifiedAt(),privilegedUser.getModifiedBy())
							.build();
		
		return insertQuery;
	}

    public String getBranch(String adminId) throws TaskException, SQLException
    {
    	Query getBranch = new QueryBuilder()
    			.select(PrivilegedUserField.BRANCH_ID)
    			.from(PrivilegedUserField.ADMIN_ID)
    			.where(PrivilegedUserField.ADMIN_ID, "=", adminId)
    			.build();
    	
    	System.out.println(getBranch.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getBranch.getQuery(), getBranch.getValues());
		Map<String,Object> resultMap= result.get(0);
		String branchId = (String) resultMap.get("branch_id") ; 
		
		System.out.println(branchId);
		if(branchId==null)
		{
			return null ; 
		}
		return branchId ; 
    }

	public Query getUpdateBranchQuery(String branchId, String personId) throws TaskException {
	
		Query updateBranch = new QueryBuilder()
				.update(PrivilegedUserField.ADMIN_ID)
				.set(PrivilegedUserField.BRANCH_ID, branchId)
				.where(PrivilegedUserField.ADMIN_ID, "=", personId)
				.build();
		return updateBranch;
	}
}
