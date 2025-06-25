package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.enums.AccountField;
import com.bank.fintrustbank.enums.BranchField;
import com.bank.fintrustbank.enums.PersonField;
import com.bank.fintrustbank.model.Branch;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.OnClause;
import querybuilder.Query;
import querybuilder.QueryBuilder;

public class BranchDAO {

	private final QueryExecutor qe = new QueryExecutor();

	
	public boolean addBranch(Branch branch) throws TaskException, SQLException {

		Query insertQuery = new QueryBuilder()
				.insert(BranchField.BRANCH_ID).values(branch.getBranchId(), branch.getManagerId(), branch.getIfscCode(),
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
		List< BranchField > excludeColumns = Arrays.asList(BranchField.IFSC_CODE, BranchField.CREATED_AT, BranchField.MANAGER_ID);
		Query updateQuery = EditUtil.update(branch,BranchField.BRANCH_ID, BranchField.BRANCH_ID, excludeColumns);

		System.out.println(updateQuery.getQuery());

		int result = qe.execute(updateQuery.getQuery(), updateQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}

	public String getSuperAdmin(String branchId) throws TaskException, SQLException {

		Query getSuperAdminQuery = new QueryBuilder().select(BranchField.MANAGER_ID).from(BranchField.BRANCH_ID)
				.where(BranchField.BRANCH_ID, "=", branchId).build();
		System.out.println(getSuperAdminQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getSuperAdminQuery.getQuery(),
				getSuperAdminQuery.getValues());

		if (result.isEmpty()) {
			return null;
		}

		return (String) result.get(0).get("manager_id");

	}

	public Query getUpdateAdminQuery(String branchId, String personId, Long modifiedAt ,String sessionPersonId ) throws TaskException {

		Query updateAdminQuery = new QueryBuilder().update(BranchField.BRANCH_ID).set(BranchField.MANAGER_ID, personId) .set(BranchField.MODIFIED_AT, modifiedAt).set(BranchField.MODIFIED_BY, sessionPersonId)
				.where(BranchField.BRANCH_ID, "=", branchId).build();
		return updateAdminQuery;
	}

	public List<Map<String, Object>> getBranches() throws TaskException, SQLException {

	/*	Query getAllBranches = new QueryBuilder()
				.select("branch_id", "location", "ifsc_code", "manager_id", "manager.name", "branch.created_at",
						"branch.modified_at", "branch.modified_by", "modifier.name")
				.from("branch")
				.join("INNER", "person", "manager", new OnClause("branch.manager_id", "=", "manager.person_id"))
				.join("INNER", "person", "modifier", new OnClause("branch.modified_by", "=", "modifier.person_id"))
				.build();*/
		
		Query getAllBranches = new QueryBuilder()
				.select(BranchField.BRANCH_ID,  BranchField.LOCATION,  BranchField.IFSC_CODE, BranchField.MANAGER_ID, PersonField.NAME.withAlias("manager"), BranchField.CREATED_AT,
						BranchField.MODIFIED_AT, BranchField.MODIFIED_BY,  PersonField.NAME.withAlias("modifier").as("modifier_name"))
				.from(BranchField.BRANCH_ID)
				.join("LEFT", PersonField.PERSON_ID, "manager", new OnClause(BranchField.MANAGER_ID, "=", PersonField.PERSON_ID.withAlias("manager")))
				.join("INNER", PersonField.PERSON_ID, "modifier", new OnClause(BranchField.MODIFIED_BY, "=",PersonField.PERSON_ID.withAlias("modifier")))
				.build();

		System.out.println(getAllBranches.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getAllBranches.getQuery(), getAllBranches.getValues());
		if (result.isEmpty()) {
			return null;
		}
		return result;
	}


	public List<Map<String, Object>> getBranchDetails(String branchId) throws TaskException, SQLException {
		/*Query getBranchDetails = new QueryBuilder()
				.select("branch.location", "branch_info.account_no", "branch.ifsc_code", "manager_info.name",
						"manager_info.email")
				.from("branch")
				.join("INNER", "account", "branch_info",
						new OnClause("branch.branch_id", "=", "branch_info.branch_id"))
				.join("INNER", "person", "manager_info",
						new OnClause("branch.manager_id", "=", "manager_info.person_id"))
				.where("customer_id", "=", customerId).build();
		*/
		Query getBranchDetails = new QueryBuilder()
				.select( BranchField.LOCATION , BranchField.BRANCH_ID , BranchField.MANAGER_ID)
				.from(BranchField.BRANCH_ID)
				.where(BranchField.BRANCH_ID, "=", branchId).build();

		System.out.println(getBranchDetails.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getBranchDetails.getQuery(), getBranchDetails.getValues());
		if (result.isEmpty()) {
			return null;
		}
		return result;

	}

	public List<Map<String, Object>> getUserBranchDetails(String customerId) throws TaskException, SQLException {
		/*Query getBranchDetails = new QueryBuilder()
				.select("branch.location", "branch_info.account_no", "branch.ifsc_code", "manager_info.name",
						"manager_info.email")
				.from("branch")
				.join("INNER", "account", "branch_info",
						new OnClause("branch.branch_id", "=", "branch_info.branch_id"))
				.join("INNER", "person", "manager_info",
						new OnClause("branch.manager_id", "=", "manager_info.person_id"))
				.where("customer_id", "=", customerId).build();
		*/
		Query getBranchDetails = new QueryBuilder()
				.select( BranchField.LOCATION, AccountField.ACCOUNT_NO.withAlias("branch_info"),  BranchField.IFSC_CODE, PersonField.NAME.withAlias("manager_info"),
						PersonField.EMAIL.withAlias("manager_info"))
				.from(BranchField.BRANCH_ID)
				.join("INNER", AccountField.ACCOUNT_NO, "branch_info",
						new OnClause(BranchField.BRANCH_ID, "=", BranchField.BRANCH_ID.withAlias("branch_info")))
				.join("INNER", PersonField.PERSON_ID, "manager_info",
						new OnClause(BranchField.MANAGER_ID, "=", PersonField.PERSON_ID.withAlias("manager_info")))
				.where(AccountField.CUSTOMER_ID, "=", customerId).build();

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
				.select(BranchField.BRANCH_ID,BranchField.LOCATION)
				.from(BranchField.BRANCH_ID).build();
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

	public Query removeManager(String personId) throws TaskException, SQLException {
		
		String branchId = getBranchId(personId);
		
		
		
		Query removeManager = new QueryBuilder()
				.update(BranchField.BRANCH_ID)
				.set(BranchField.MANAGER_ID ,null)
				.where(BranchField.BRANCH_ID, "=", branchId)
				.build();
	
		
		
		return removeManager;
	}

	private String getBranchId(String personId) throws TaskException, SQLException {
		
		Query getBranchId = new QueryBuilder()
				.select(BranchField.BRANCH_ID)
				.from(BranchField.BRANCH_ID)
				.where(BranchField.MANAGER_ID,"=", personId)
				.build();
		List<Map<String, Object>> result = qe.executeQuery(getBranchId.getQuery(), getBranchId.getValues());
		System.out.println(getBranchId.getQuery()+ getBranchId.getValues());
		System.out.println(result);
		if (result.isEmpty()) {
			return null;
		}
		else
		{
			String branchId = (String) result.get(0).get("branch_id") ;
			return branchId ; 
		}
		
	}


}
