package com.bank.fintrustbank.dao;

import com.bank.fintrustbank.enums.AccountField;
import com.bank.fintrustbank.enums.AccountRequestField;
import com.bank.fintrustbank.enums.PersonField;
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
				Query  getAccounts  = new QueryBuilder() 
						.select(PersonField.NAME , TransactionField.TRANSACTION_ACCOUNT_NO).max(TransactionField.DATE_TIME, "latest_transaction")
						.from(TransactionField.ROW_ID)
						.join("INNER", AccountField.ACCOUNT_NO, new OnClause(TransactionField.TRANSACTION_ACCOUNT_NO,"=" , AccountField.ACCOUNT_NO))
						.join("INNER", PersonField.PERSON_ID, new OnClause(AccountField.CUSTOMER_ID,"=" , PersonField.PERSON_ID))
						.where(TransactionField.ACCOUNT_NO, "=", accountNo)
						.groupBy(TransactionField.TRANSACTION_ACCOUNT_NO)
						.groupBy(PersonField.NAME)
						.orderBy("latest_transaction", false)
						.limit(20)
						.build() ;
			System.out.println(getAccounts.getQuery()) ; 
		} catch (TaskException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
