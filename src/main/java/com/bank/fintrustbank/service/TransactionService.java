package com.bank.fintrustbank.service;

import java.sql.SQLException;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.dao.TransactionDAO;
import com.bank.fintrustbank.model.Transaction;
import com.bank.fintrustbank.util.QueryExecutor;
import com.bank.fintrustbank.util.TransactionIdGenerator;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;

public class TransactionService {
	
	AccountDAO accountDAO = new AccountDAO();
	TransactionDAO transactionDAO =  new TransactionDAO();
	QueryExecutor qe = new QueryExecutor();
	public boolean processtransaction(Transaction debit , boolean otherBank) throws TaskException, SQLException
	{
		
		Double senderBalance = accountDAO.getBalance(debit.getAccountNo());
		
		Double senderBalanceAfterTransaction = senderBalance - debit.getAmount();
		
		debit.setAvailableBalance(senderBalanceAfterTransaction);
		debit.setRowId(RowIdGenerator.generateRowId());
	    debit.setTransactionId(TransactionIdGenerator.generateTransactionId(debit.getDateTime(),debit.getAccountNo()));
	
		debit.setTransactionStatus("PENDING");
		Query senderTransactionInsert = transactionDAO.insertTransaction(debit);
		if(otherBank)
		{
			debit.setDescription("OTHER BANK TRANSACTION "+debit.getDescription());
			debit.setTransactionStatus("SUCCESS");
		}
		
		qe.execute(senderTransactionInsert.getQuery(), senderTransactionInsert.getValues());
		
		
		
		
		
		
		
		
		
		
		
		
		
		if(!otherBank)
		{
		qe.beginTransaction();
			
		Double receiverBalance = accountDAO.getBalance(debit.getTransactionAccountNo());

		Double receiverBalanceAfterTransaction = receiverBalance - debit.getAmount();
	   long timeStamp = System.currentTimeMillis();
		
		String creditCustomerId = accountDAO.getCustomerId(debit.getTransactionAccountNo());
		Transaction credit = new Transaction( RowIdGenerator.generateRowId(),TransactionIdGenerator.generateTransactionId(timeStamp,debit.getTransactionAccountNo()),
				creditCustomerId,debit.getTransactionAccountNo() , debit.getAccountNo() ,timeStamp ,debit.getAmount(), "SUCCESS" ,
				"CREDIT" , receiverBalance ,debit.getDescription(),debit.getTransactionBy()); 
		Query receiverTransactionInsert = transactionDAO.insertTransaction(credit);
		qe.execute(receiverTransactionInsert.getQuery(), receiverTransactionInsert.getValues());
		
		Query updateSenderStatus = transactionDAO.updateTransactionStatus(debit.getRowId(), "SUCCESS");
		qe.execute(updateSenderStatus.getQuery(),updateSenderStatus.getValues());
		
		
		}
		
		return false;
		
	}

}
