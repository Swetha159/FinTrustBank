package com.bank.fintrustbank.dao;

import com.bank.fintrustbank.enums.AccountField;
import com.bank.fintrustbank.enums.AccountRequestField;
import com.bank.fintrustbank.enums.PersonField;
import com.zoho.training.exceptions.TaskException;

import querybuilder.OnClause;
import querybuilder.Query;
import querybuilder.QueryBuilder;

public class test {

	public static void main(String args[])
	{
		  try {
			Query getAccounts =  new QueryBuilder()
					.select(PersonField.PERSON_ID , PersonField.NAME,PersonField.EMAIL,PersonField.PHONE_NUMBER , PersonField.DOB , PersonField.AADHAR, PersonField.PAN,PersonField.ADDRESS,PersonField.CREATED_AT,PersonField.MODIFIED_BY,PersonField.NAME.withAlias("modifier").as("modifier_name"), AccountField.ACCOUNT_TYPE)
					.from(AccountRequestField.PERSON_ID)
					.join("INNER",PersonField.PERSON_ID, new OnClause(AccountRequestField.PERSON_ID,"=", PersonField.PERSON_ID ))
					.join("INNER",  PersonField.PERSON_ID, "modifier" , new OnClause(PersonField.MODIFIED_BY,"=",PersonField.PERSON_ID.withAlias("modifier")))
					.where(AccountRequestField.REQUEST_STATUS, "=","sjdf")
					.and(AccountRequestField.BRANCH_ID, "=", "jgsdS")
					.build();
			System.out.println(getAccounts.getQuery()) ; 
		} catch (TaskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
