package com.bank.fintrustbank.dao;

import com.bank.fintrustbank.model.Transaction;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class TransactionDAO {

	public Query insertTransaction(Transaction transaction) throws TaskException {
	
		Query insertQuery = new QueryBuilder()
				.insert("transaction")
				.values(transaction.getTransactionId(),transaction.getCustomerId(),transaction.getAccountNo(),transaction.getTransactionAccountNo()
						,transaction.getDateTime(),transaction.getAmount(),transaction.getTransactionStatus(),transaction.getTransactionType()
						,transaction.getAvailableBalance(),transaction.getDescription() , transaction.getTransactionBy())
				.build();
		return insertQuery ; 
		
	}

	
public Query updateTransactionStatus(String rowId, String status) throws TaskException
{
	
	Query updateStatus = new QueryBuilder()
			.update("transaction")
			.set("transaction_status", status)
			.where("row_id", "=", rowId)
			.build();
	
			
			return updateStatus;
}

}
