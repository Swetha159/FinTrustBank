package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.enums.AccountField;
import com.bank.fintrustbank.enums.PersonField;
import com.bank.fintrustbank.enums.PrivilegedUserField;
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
		Query loginQuery = new QueryBuilder().select(PersonField.PERSON_ID, PersonField.ROLE,PersonField.PASSWORD, PersonField.STATUS).from(PersonField.PERSON_ID)
				.where(PersonField.EMAIL, "=", email).build();

		System.out.println(loginQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(loginQuery.getQuery(), loginQuery.getValues());
		System.out.println(loginQuery.getValues());
		System.out.println(result.toString());
		if(!result.isEmpty()) {
			Map<String, Object> resultMap = result.get(0);
			String personId = (String) resultMap.get("person_id");
			String role = (String) resultMap.get("role");
			String name = (String) resultMap.get("name");
			String passwordHash = (String) resultMap.get("password");
			String status = (String) resultMap.get("status");
			if (Password.verifyPassword(password, passwordHash)) {
				Person person = new Person();
				person.setPersonId(personId);
				person.setRole(role);
				person.setName(name);
				person.setStatus(status);
				return person;
			} else {
				return null;
			}
		}
		else {
			return null;
		}

	}

	public boolean addNewCustomer(Person person) throws TaskException, SQLException {

		Query addCustomerQuery = new QueryBuilder().insert(PersonField.PERSON_ID)
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
		Query insertQuery = new QueryBuilder().insert(PersonField.PERSON_ID)
				.values(person.getPersonId(), person.getName(), person.getEmail(), person.getPhoneNumber(),
						person.getRole(), person.getPassword(), person.getStatus(), person.getDob(), person.getAadhar(),
						person.getPan(), person.getAddress(), person.getCreatedAt(), person.getModifiedAt(),
						person.getModifiedBy())
				.build();
		System.out.println(insertQuery.getQuery());
		return insertQuery;
	}

	public boolean addPerson(Person person) throws TaskException, SQLException {

		Query addPersonQuery = new QueryBuilder().insert(PersonField.PERSON_ID)
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
		Query updateStatusQuery = new QueryBuilder().update(PersonField.PERSON_ID).set(PersonField.STATUS, status)
				.set(PersonField.MODIFIED_AT, modifiedAt).set(PersonField.MODIFIED_BY, sessionPersonId)
				.where(PersonField.PERSON_ID, "=", customerId).build();
		return updateStatusQuery;
	}

	public boolean updatePerson(Person person) throws TaskException, SQLException {
		List<PersonField> excludeColumns = Arrays.asList(PersonField.PASSWORD, PersonField.ROLE, PersonField.CREATED_AT, PersonField.STATUS);
		Query updateQuery = EditUtil.update(person, PersonField.PERSON_ID, PersonField.PERSON_ID, excludeColumns);

		System.out.println(updateQuery.getQuery());

		System.out.println(updateQuery.getValues());

		int result = qe.execute(updateQuery.getQuery(), updateQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}

	public Query getUpdateRoleQuery(String role, String personId, Long modifiedAt, String sessionPersonId)
			throws TaskException {

		Query updateRoleQuery = new QueryBuilder().update(PersonField.PERSON_ID).set( PersonField.ROLE, role).set(PersonField.MODIFIED_AT, modifiedAt)
				.set(PersonField.MODIFIED_BY, sessionPersonId).where(PersonField.PERSON_ID, "=", personId).build();

		return updateRoleQuery;
	}

	public boolean updateStatus(String status, String personId, Long modifiedAt, String sessionPersonId)
			throws TaskException, SQLException {

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
				.select(PersonField.PERSON_ID,PersonField.NAME,PersonField.EMAIL,PersonField.PHONE_NUMBER,AccountField.ACCOUNT_NO,AccountField.BALANCE,PersonField.STATUS)
				.from(PersonField.PERSON_ID).join("INNER", AccountField.ACCOUNT_NO, new OnClause(AccountField.CUSTOMER_ID,"=", PersonField.PERSON_ID))
				.where(AccountField.BRANCH_ID, "=", branchId)
				.and(PersonField.ROLE,"=","CUSTOMER")
				.build();
		
		System.out.println(getCustomers.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getCustomers.getQuery(), getCustomers.getValues());
		System.out.println(result);
		return result ; 
	}
	
	
	public List<Map<String, Object>> getPersonDetails (String personId ) throws TaskException, SQLException
	{
		Query getPersonDetails  = new QueryBuilder()
				.select(PersonField.PERSON_ID ,PersonField.NAME,PersonField.EMAIL,PersonField.PHONE_NUMBER,PersonField.STATUS, PersonField.DOB,PersonField.AADHAR,PersonField.PAN,PersonField.ADDRESS ,PersonField.CREATED_AT,PersonField.MODIFIED_AT,PersonField.MODIFIED_BY ,PersonField.NAME.withAlias("modifier").as("modifier_name"))				
				.from(PersonField.PERSON_ID)
				.join("INNER", PersonField.PERSON_ID, "modifier" , new OnClause(PersonField.MODIFIED_BY,"=",PersonField.PERSON_ID.withAlias("modifier")))
				.where(PersonField.PERSON_ID,"=",personId)
				.build();
		System.out.println(getPersonDetails.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getPersonDetails.getQuery(), getPersonDetails.getValues());
	
		if(result.isEmpty())
		{
			return null ; 
		}
		return result ; 
		
	}
	
	public List<Map<String,Object>> getAdmins(String branchId) throws TaskException, SQLException
	{
		Query getAdmins  = new QueryBuilder()
							.select(PersonField.PERSON_ID,PersonField.NAME,PersonField.EMAIL,PersonField.PHONE_NUMBER,PersonField.STATUS)
							.from(PersonField.PERSON_ID)
							.join("INNER", PrivilegedUserField.ADMIN_ID , new OnClause(PrivilegedUserField.ADMIN_ID,"=",PersonField.PERSON_ID))
							.where(PrivilegedUserField.BRANCH_ID,"=",branchId)
							.and(PersonField.ROLE,"=","ADMIN")
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
