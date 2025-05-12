package com.bank.fintrustbank.service;

import java.sql.SQLException;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.dao.TransactionDAO;
import com.bank.fintrustbank.model.Transaction;
import com.bank.fintrustbank.util.TransactionIdGenerator;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;

public class TransactionService {
	
	AccountDAO accountDAO = new AccountDAO();
	TransactionDAO transactionDAO =  new TransactionDAO();
	public boolean processtransaction(Transaction debit , boolean otherBank) throws TaskException, SQLException
	{
		
		Double senderBalance = accountDAO.getBalance(debit.getAccountNo());
		
		Double senderBalanceAfterTransaction = senderBalance - debit.getAmount();
		
		debit.setAvailableBalance(senderBalanceAfterTransaction);
		
	
		debit.setTransactionStatus("");
		
		Query senderTransactionInsert = transactionDAO.insertTransaction(debit);
		
		
		
		
		
		
		
		
		
		
		
		if(!otherBank)
		{
		
		Double receiverBalance = accountDAO.getBalance(debit.getTransactionAccountNo());
		
	   long timeStamp = System.currentTimeMillis();
		
		String creditCustomerId = accountDAO.getCustomerId(debit.getTransactionAccountNo());
		Transaction receiverTransaction = new Transaction(TransactionIdGenerator.generateTransactionId(timeStamp, debit.getTransactionId())
				,creditCustomerId,debit.getTransactionAccountNo() , debit.getAccountNo() ,timeStamp , "SUCCESS" , "CREDIT" , receiverBalance ,debit.getDescription(),debit.getTransactionBy()
				); 
				
		}
		
		return false;
		
	}

}
