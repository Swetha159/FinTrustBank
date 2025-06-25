package com.bank.fintrustbank.dao;

import com.bank.fintrustbank.enums.AccountField;
import com.bank.fintrustbank.enums.AccountRequestField;
import com.bank.fintrustbank.enums.BranchField;
import com.bank.fintrustbank.enums.PersonField;
import com.bank.fintrustbank.enums.PrivilegedUserField;
import com.bank.fintrustbank.enums.TransactionField;
import com.zoho.training.exceptions.TaskException;

import querybuilder.OnClause;
import querybuilder.Query;
import querybuilder.QueryBuilder;

public class test {

	public static void main(String args[])
	{
		  try {
			  Long accountNo = 101203705548L; 
				Query  getAccounts  =  new QueryBuilder()
				        .select(
					            AccountField.CUSTOMER_ID,
					           PersonField.NAME.withAlias("customer"),
					            AccountField.ACCOUNT_NO,
					            AccountField.ACCOUNT_TYPE,
					            AccountField.BALANCE,
					            AccountField.ACCOUNT_STATUS,
					            AccountField.CREATED_AT,
					            AccountField.MODIFIED_AT,
					            AccountField.MODIFIED_BY,
					            PersonField.NAME.withAlias("modifier").as("modifier_name")
					  
					        )
					        .from(AccountField.ACCOUNT_NO)
					       
					        .join("INNER", PersonField.PERSON_ID , "customer",
					            new OnClause(PersonField.PERSON_ID.withAlias("customer"), "=", AccountField.CUSTOMER_ID )
					        )
					        .join("INNER",PersonField.PERSON_ID, "modifier",
					            new OnClause(AccountField.MODIFIED_BY, "=", PersonField.PERSON_ID.withAlias("modifier"))
					        )
					        .where(AccountField.BRANCH_ID, "=", "101")
					        .build();
			System.out.println(getAccounts.getQuery()) ; 
		} catch (TaskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
