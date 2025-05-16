package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.util.Password;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.OnClause;
import querybuilder.Query;
import querybuilder.QueryBuilder;

public class PersonDAO {

	QueryExecutor qe = new QueryExecutor();

	public Person checkCredentials(String email, String password) throws SQLException, TaskException {

		System.out.println(email + password);
		Query loginQuery = new QueryBuilder().select("person_id", "role", "password", "status").from("person")
				.where("email", "=", email).build();

		System.out.println(loginQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(loginQuery.getQuery(), loginQuery.getValues());
		System.out.println(result);
		Map<String, Object> resultMap = result.get(0);
		String personId = (String) resultMap.get("person_id");
		String role = (String) resultMap.get("role");
		String passwordHash = (String) resultMap.get("password");
		String status = (String) resultMap.get("status");
		if (Password.verifyPassword(password, passwordHash)) {
			Person person = new Person();
			person.setPersonId(personId);
			person.setRole(role);
			return person;
		} else {
			return null;
		}

	}

	public boolean addNewCustomer(Person person) throws TaskException {

		Query addCustomerQuery = new QueryBuilder().insert("person")
				.values(person.getPersonId(), person.getName(), person.getEmail(), person.getPhoneNumber(),
						person.getRole(), person.getPassword(), person.getStatus(), person.getDob(), person.getAadhar(),
						person.getPan(), person.getAddress(), person.getCreatedAt(), person.getModifiedAt(),
						person.getModifiedBy())
				.build();
		System.out.println(addCustomerQuery.getQuery());
		int result = qe.execute(addCustomerQuery.getQuery(), addCustomerQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}

	public Query getInsertQuery(Person person) throws TaskException {
		Query insertQuery = new QueryBuilder().insert("person")
				.values(person.getPersonId(), person.getName(), person.getEmail(), person.getPhoneNumber(),
						person.getRole(), person.getPassword(), person.getStatus(), person.getDob(), person.getAadhar(),
						person.getPan(), person.getAddress(), person.getCreatedAt(), person.getModifiedAt(),
						person.getModifiedBy())
				.build();
		System.out.println(insertQuery.getQuery());
		return insertQuery;
	}

	public boolean addPerson(Person person) throws TaskException {

		Query addPersonQuery = new QueryBuilder().insert("person")
				.values(person.getPersonId(), person.getName(), person.getEmail(), person.getPhoneNumber(),
						person.getRole(), person.getPassword(), person.getStatus(), person.getDob(), person.getAadhar(),
						person.getPan(), person.getAddress(), person.getCreatedAt(), person.getModifiedAt(),
						person.getModifiedBy())
				.build();
		System.out.println(addPersonQuery.getQuery());
		int result = qe.execute(addPersonQuery.getQuery(), addPersonQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;
	}

	public Query getUpdateStatusQuery(String customerId, String status, Long modifiedAt, String sessionPersonId)
			throws TaskException {
		Query updateStatusQuery = new QueryBuilder().update("person").set("status", status)
				.set("modified_at", modifiedAt).set("modified_by", sessionPersonId)
				.where("person_id", "=", "customerId").build();
		return updateStatusQuery;
	}

	public boolean updatePerson(Person person) throws TaskException {
		List<String> excludeColumns = Arrays.asList("person_id", "password", "role", "created_at", "status");
		Query updateQuery = EditUtil.update(person, "person", "person_id", excludeColumns);

		System.out.println(updateQuery.getQuery());

		int result = qe.execute(updateQuery.getQuery(), updateQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}

	public Query getUpdateRoleQuery(String role, String personId, Long modifiedAt, String sessionPersonId)
			throws TaskException {

		Query updateRoleQuery = new QueryBuilder().update("Person").set("role", role).set("modified_at", modifiedAt)
				.set("modified_by", sessionPersonId).where("person_id", "=", personId).build();

		return updateRoleQuery;
	}

	public boolean updateStatus(String status, String personId, Long modifiedAt, String sessionPersonId)
			throws TaskException {

		Query updateStatusQuery = getUpdateStatusQuery(personId, status, modifiedAt, sessionPersonId);

		int result = qe.execute(updateStatusQuery.getQuery(), updateStatusQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}
	
	public List<Map<String,Object>>  getCustomers(String branchId) throws TaskException, SQLException
	{
		Query getCustomers = new QueryBuilder()
				.select("person_id","name","email","phone_number","account.account_no","account.balance","status")
				.from("person").join("INNER", "account", new OnClause("account.customer_id","=", "person.person_id"))
				.where("branch_id", "=", branchId)
				.and("role","=","CUSTOMER")
				.build();
		
		System.out.println(getCustomers.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getCustomers.getQuery(), getCustomers.getValues());
		System.out.println(result);
		return result ; 
	}
	
	
	public Map<String,Object> getPersonDetails (String personId ) throws TaskException, SQLException
	{
		Query getPersonDetails  = new QueryBuilder()
				.select("person.person_id" ,"person.name","person.email","person.phone_number","person.status", "person.dob","person.aadhar","person.pan","person.address" ,"person.created_at","person.modified_at","modifier.modified_by" , "modifier.name")				
				.from("person")
				.join("INNER", "person", "modifier" , new OnClause("person.modified_by","=", "modifier.modified_by"))
				.where("person.person_id","=",personId)
				.build();
		System.out.println(getPersonDetails.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getPersonDetails.getQuery(), getPersonDetails.getValues());
		Map<String,Object> details = result.get(0);
		System.out.println(details);
		if(details.isEmpty())
		{
			return null ; 
		}
		return details ; 
		
	}
	
	public List<Map<String,Object>> getAdmins(String branchId) throws TaskException, SQLException
	{
		Query getAdmins  = new QueryBuilder()
							.select("person_id","name","email","phone_number","status")
							.from("person")
							.join("INNER", "privileged_user" , new OnClause("privileged_user.admin_id","=","person.person_id"))
							.where("branch_id","=",branchId)
							.and("role","=","ADMIN")
							.build();
		System.out.println(getAdmins.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getAdmins.getQuery(), getAdmins.getValues());
		System.out.println(result);
		if(result.isEmpty())
		{
			return null ; 
		}
		return result ; 
	}
	
	
}
