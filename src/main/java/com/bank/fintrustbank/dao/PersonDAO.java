package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.util.Password;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class PersonDAO {
	
	QueryExecutor qe = new QueryExecutor();

	public Person checkCredentials(String email, String password) throws SQLException, TaskException {

		System.out.println(email + password);
		Query loginQuery = new QueryBuilder()
				.select("person_id", "role","password")
				.from("Person")
				.where("email", "=", email)
				.build();
		
		System.out.println(loginQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(loginQuery.getQuery(), loginQuery.getValues());
		System.out.println(result);
		Map<String, Object> resultMap = result.get(0);
		String personId = (String) resultMap.get("person_id");
		String role = (String) resultMap.get("role");
        String passwordHash  = (String)resultMap.get("password");
       if(Password.verifyPassword(password, passwordHash)) 
       {
		Person person =new Person();
		person.setPersonId(personId);
		person.setRole(role);
		return person;
       }
       else
       {
    	   return null ; 
       }

	}

	public boolean addNewCustomer(Person person) throws TaskException {
		
		
		Query addCustomerQuery  = new QueryBuilder()
				.insert("Person")
				.values(person.getPersonId(),person.getName(),person.getEmail(),person.getPhoneNumber(),person.getRole(),person.getPassword(),
						person.getStatus(),person.getDob(),person.getAadhar(),person.getPan(),person.getAddress(),person.getCreatedAt(),
						person.getModifiedAt(),person.getModifiedBy())
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
		Query addCustomerQuery  = new QueryBuilder()
				.insert("Person")
				.values(person.getPersonId(),person.getName(),person.getEmail(),person.getPhoneNumber(),person.getRole(),person.getPassword(),
						person.getStatus(),person.getDob(),person.getAadhar(),person.getPan(),person.getAddress(),person.getCreatedAt(),
						person.getModifiedAt(),person.getModifiedBy())
				.build();
				System.out.println(addCustomerQuery.getQuery());	
				return addCustomerQuery ;
	}
	
//	 
}
