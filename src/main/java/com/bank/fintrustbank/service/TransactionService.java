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

	private final AccountDAO accountDAO = new AccountDAO();
	private final TransactionDAO transactionDAO = new TransactionDAO();
	private final QueryExecutor qe = new QueryExecutor();
    private static final Long BANK_ACCOUNT_NO = 123456789012L ;

	public boolean processTransaction(Transaction debit, boolean otherBank) throws TaskException, SQLException {

		try {

			Double senderBalance = accountDAO.getBalance(debit.getAccountNo());
System.out.println(debit.getAccountNo());

			Double senderBalanceAfterTransaction = senderBalance - debit.getAmount();

			debit.setAvailableBalance(senderBalanceAfterTransaction);
			debit.setRowId(RowIdGenerator.generateRowId());
			debit.setTransactionId(
					TransactionIdGenerator.generateTransactionId(debit.getDateTime(), debit.getAccountNo()));

			debit.setTransactionStatus("PENDING");

			qe.beginTransaction();

			Query updateBalanceQuery = accountDAO.updateBalance(debit.getAccountNo(), senderBalanceAfterTransaction);
			qe.execute(updateBalanceQuery.getQuery(), updateBalanceQuery.getValues());
			if (otherBank) {
				debit.setDescription("OTHER BANK TRANSACTION  IFSC CODE : " + debit.getDescription());
				debit.setTransactionStatus("SUCCESS");
			}
			Query senderTransactionInsert = transactionDAO.insertTransaction(debit);
			qe.execute(senderTransactionInsert.getQuery(), senderTransactionInsert.getValues());
			qe.commitTransaction();
		} catch (TaskException | SQLException e) {
			qe.rollbackTransaction();
			return false;

		}

		if (!otherBank) {
			try {

				Double receiverBalance = accountDAO.getBalance(debit.getTransactionAccountNo());

				Double receiverBalanceAfterTransaction = receiverBalance + debit.getAmount();
				long timeStamp = System.currentTimeMillis();

				String creditCustomerId = accountDAO.getCustomerId(debit.getTransactionAccountNo());
				Transaction credit = new Transaction(RowIdGenerator.generateRowId(),
						TransactionIdGenerator.generateTransactionId(timeStamp, debit.getTransactionAccountNo()),
						creditCustomerId,  debit.getAccountNo(),debit.getTransactionAccountNo(), timeStamp,
						debit.getAmount(), "SUCCESS", "CREDIT", receiverBalanceAfterTransaction, debit.getDescription(),
						debit.getTransactionBy());

				qe.beginTransaction();
				Query receiverTransactionInsert = transactionDAO.insertTransaction(credit);
				qe.execute(receiverTransactionInsert.getQuery(), receiverTransactionInsert.getValues());
				Query updateBalanceQuery = accountDAO.updateBalance(credit.getAccountNo(),
						receiverBalanceAfterTransaction);
				qe.execute(updateBalanceQuery.getQuery(), updateBalanceQuery.getValues());
				Query updateSenderStatus = transactionDAO.updateTransactionStatus(debit.getRowId(), "SUCCESS");
				qe.execute(updateSenderStatus.getQuery(), updateSenderStatus.getValues());
				qe.commitTransaction();

			} catch (TaskException | SQLException e) {
				qe.rollbackTransaction();
				Query updateSenderStatus = transactionDAO.updateTransactionStatus(debit.getRowId(), "FAILED");
				qe.execute(updateSenderStatus.getQuery(), updateSenderStatus.getValues());

				return false;

			}

		}

		return true;

	}

	public boolean processDeposit(Transaction credit) throws SQLException, TaskException {
		try {

			Double balance = accountDAO.getBalance(credit.getAccountNo());

			Double balanceAfterDeposit = balance + credit.getAmount();

			credit.setAvailableBalance(balanceAfterDeposit);
			credit.setRowId(RowIdGenerator.generateRowId());
			credit.setTransactionId(
					TransactionIdGenerator.generateTransactionId(credit.getDateTime(), credit.getAccountNo()));
			credit.setTransactionAccountNo(BANK_ACCOUNT_NO);
			credit.setTransactionStatus("SUCCESS");

			qe.beginTransaction();

			Query updateBalanceQuery = accountDAO.updateBalance(credit.getAccountNo(), balanceAfterDeposit);
			qe.execute(updateBalanceQuery.getQuery(), updateBalanceQuery.getValues());
			
			Query transactionInsert = transactionDAO.insertTransaction(credit);
			qe.execute(transactionInsert.getQuery(), transactionInsert.getValues());

			qe.commitTransaction();
			return true ; 
		} catch (TaskException | SQLException e) {
			credit.setTransactionStatus("FAILED");
			Query transactionInsert = transactionDAO.insertTransaction(credit);
			qe.execute(transactionInsert.getQuery(),transactionInsert.getValues());
			qe.rollbackTransaction();
			return false;

		}
	}
	


	public boolean processWithdraw(Transaction debit) throws SQLException, TaskException {
		try {

			Double balance = accountDAO.getBalance(debit.getAccountNo());

			Double balanceAfterWithdraw = balance - debit.getAmount();

			debit.setAvailableBalance(balanceAfterWithdraw);
			debit.setRowId(RowIdGenerator.generateRowId());
			debit.setTransactionId(
					TransactionIdGenerator.generateTransactionId(debit.getDateTime(), debit.getAccountNo()));
			debit.setTransactionAccountNo(BANK_ACCOUNT_NO);
			debit.setTransactionStatus("SUCCESS");

			qe.beginTransaction();

			Query updateBalanceQuery = accountDAO.updateBalance(debit.getAccountNo(), balanceAfterWithdraw);
			qe.execute(updateBalanceQuery.getQuery(), updateBalanceQuery.getValues());
			
			Query transactionInsert = transactionDAO.insertTransaction(debit);
			qe.execute(transactionInsert.getQuery(), transactionInsert.getValues());

			qe.commitTransaction();
			return true ; 
		} catch (TaskException | SQLException e) {
			debit.setTransactionStatus("FAILED");
			Query transactionInsert = transactionDAO.insertTransaction(debit);
			qe.execute(transactionInsert.getQuery(),transactionInsert.getValues());
			qe.rollbackTransaction();
			return false;

		}
	}

}
