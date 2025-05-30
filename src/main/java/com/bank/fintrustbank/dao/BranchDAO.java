package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.OnClause;
import querybuilder.Query;
import querybuilder.QueryBuilder;

public class BranchDAO {

	QueryExecutor qe = new QueryExecutor();

	public boolean addBranch(Branch branch) throws TaskException, SQLException {

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

	public boolean updateBranch(Branch branch) throws TaskException, SQLException {
		List<String> excludeColumns = Arrays.asList("branch_id", "ifsc", "created_at", "manager_id");
		Query updateQuery = EditUtil.update(branch, "branch", "branch_id", excludeColumns);

		System.out.println(updateQuery.getQuery());

		int result = qe.execute(updateQuery.getQuery(), updateQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}

	public String getSuperAdmin(String branchId) throws TaskException, SQLException {

		Query getSuperAdminQuery = new QueryBuilder().select("manager_id").from("branch")
				.where("branch_id", "=", branchId).build();
		System.out.println(getSuperAdminQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getSuperAdminQuery.getQuery(),
				getSuperAdminQuery.getValues());

		if (result.isEmpty()) {
			return null;
		}

		return (String) result.get(0).get("manager_id");

	}

	public Query getUpdateAdminQuery(String branchId, String personId) throws TaskException {

		Query updateAdminQuery = new QueryBuilder().update("branch").set("manager_id", personId)
				.where("branch_id", "=", branchId).build();
		return updateAdminQuery;
	}

	public List<Map<String, Object>> getBranches() throws TaskException, SQLException {

		Query getAllBranches = new QueryBuilder()
				.select("branch_id", "location", "ifsc_code", "manager_id", "manager.name", "branch.created_at",
						"branch.modified_at", "branch.modified_by", "modifier.name")
				.from("branch")
				.join("INNER", "person", "manager", new OnClause("branch.manager_id", "=", "manager.person_id"))
				.join("INNER", "person", "modifier", new OnClause("branch.modified_by", "=", "modifier.person_id"))
				.build();

		System.out.println(getAllBranches.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getAllBranches.getQuery(), getAllBranches.getValues());
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}

	public List<Map<String, Object>> getBranchDetails(String customerId) throws TaskException, SQLException {
		Query getBranchDetails = new QueryBuilder()
				.select("branch.location", "branch_info.account_no", "branch.ifsc_code", "manager_info.name",
						"manager_info.email")
				.from("branch")
				.join("INNER", "account", "branch_info",
						new OnClause("branch.branch_id", "=", "branch_info.branch_id"))
				.join("INNER", "person", "manager_info",
						new OnClause("branch.manager_id", "=", "manager_info.person_id"))
				.where("customer_id", "=", customerId).build();

		System.out.println(getBranchDetails.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getBranchDetails.getQuery(), getBranchDetails.getValues());
		if (result.isEmpty()) {
			return null;
		}
		return result;

	}
	
	
	
	public List<Branch> getAllBranches() throws TaskException, SQLException
	{
		Query getAllBranches = new QueryBuilder()
				.select("branch_id","location")
				.from("branch").build();
		List<Map<String, Object>> result = qe.executeQuery(getAllBranches.getQuery(), getAllBranches.getValues());
		
		   List<Branch> branches = new ArrayList<>();
		   
		    for (Map<String, Object> row : result) {
		        String location = (String) row.get("location");
		        String branchId = (String) row.get("branch_id");
		        Branch branch = new Branch();
		        branch.setLocation(location);
		        branch.setBranchId(branchId);
		        branches.add(branch);
		    }

		    return branches;

		
				
	}


}
