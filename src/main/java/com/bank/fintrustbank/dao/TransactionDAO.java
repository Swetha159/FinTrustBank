package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.enums.TransactionField;
import com.bank.fintrustbank.model.Transaction;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class TransactionDAO {

	QueryExecutor qe = new QueryExecutor();

	public Query insertTransaction(Transaction transaction) throws TaskException {

		Query insertQuery = new QueryBuilder().insert(TransactionField.ROW_ID)
				.values(transaction.getTransactionId(), transaction.getCustomerId(), transaction.getAccountNo(),
						transaction.getTransactionAccountNo(), transaction.getDateTime(), transaction.getAmount(),
						transaction.getTransactionStatus(), transaction.getTransactionType(),
						transaction.getAvailableBalance(), transaction.getDescription(), transaction.getTransactionBy() ,transaction.getRowId())
				.build();
		return insertQuery;

	}

	

	public Query updateTransactionStatus(String rowId, String status) throws TaskException {

		Query updateStatus = new QueryBuilder().update(TransactionField.ROW_ID).set(TransactionField.TRANSACTION_STATUS, status)
				.where(TransactionField.ROW_ID, "=", rowId).build();

		return updateStatus;
	}

	public Object getTransactions(String accountNo) throws TaskException, SQLException {

		Query getAllTransaction = new QueryBuilder()
				.select(TransactionField.TRANSACTION_ID,TransactionField.ACCOUNT_NO, TransactionField.DATE_TIME,TransactionField.AMOUNT, TransactionField.TRANSACTION_TYPE,
						TransactionField.AVAILABLE_BALANCE)
				.from(TransactionField.ROW_ID).where(TransactionField.ACCOUNT_NO, "=", accountNo).and(TransactionField.TRANSACTION_STATUS, "=", "SUCCESS")
				.build();

		List<Map<String, Object>> result = qe.executeQuery(getAllTransaction.getQuery(), getAllTransaction.getValues());
		return result;
	}

//	public List<Map<String, Object>> getTransactionHistory(String accountNo) throws TaskException, SQLException {
//
//		Query getTransactionHistory = new QueryBuilder()
//				.select("transaction_id,transaction_account_no", "date_time", "amount", "transaction_type",
//						"available_balance", "description")
//				.from("transaction").where("account_no", "=", accountNo).build();
//
//		List<Map<String, Object>> result = qe.executeQuery(getTransactionHistory.getQuery(),
//				getTransactionHistory.getValues());
//		return result;
//		
//	}
  
	public Long getTotalCreditThisWeek(Long accountNo) throws TaskException, SQLException
	{
		 ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC); 

		 long startOfWeekMillis = getStartOfWeekMillis(now);

		 long endOfWeekMillis = getEndOfweekMillis(now);


		Query getCreditCount= new QueryBuilder()
				.select()
				.count(TransactionField.TRANSACTION_ID,"credit_count")
				.from(TransactionField.ROW_ID)
				.where(TransactionField.ACCOUNT_NO,"=",accountNo)
			//	.and(TransactionField.TRANSACTION_TYPE, "IN", "(\"CREDIT\",\"DEPOSIT\")")
				.and(TransactionField.DATE_TIME,">=", startOfWeekMillis)
				.and(TransactionField.DATE_TIME,"<=",endOfWeekMillis)
				.and(TransactionField.TRANSACTION_STATUS,"=","SUCCESS")
				.build();
		
		System.out.println(getCreditCount.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getCreditCount.getQuery(), getCreditCount.getValues());
		System.out.println(result);
		if(result.isEmpty())
		{
			return null;
		}
		Map<String, Object> resultMap = result.get(0);
		Long creditCount = (Long) resultMap.get("credit_count");
		return creditCount ; 
		
	}

	private long getEndOfweekMillis(ZonedDateTime now) {
		ZonedDateTime endOfWeek = now
		     .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		     .toLocalDate()
		     .atTime(LocalTime.MAX)
		     .atZone(ZoneOffset.UTC);

		 long endOfWeekMillis = endOfWeek.toInstant().toEpochMilli();
		return endOfWeekMillis;
	}

private long getStartOfWeekMillis(ZonedDateTime now) {
	ZonedDateTime startOfWeek = now
	     .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
	     .toLocalDate()
	     .atStartOfDay(ZoneOffset.UTC);

	 long startOfWeekMillis = startOfWeek.toInstant().toEpochMilli();
	return startOfWeekMillis;
}
	public Long getTotalDebitThisWeek(Long accountNo) throws TaskException, SQLException
	{
		 ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC); 

		 long startOfWeekMillis = getStartOfWeekMillis(now);

		 long endOfWeekMillis = getEndOfweekMillis(now);


		Query getCreditCount= new QueryBuilder()
				.select()
				.count(TransactionField.TRANSACTION_ID,"debit_count")
				.from(TransactionField.ROW_ID)
				.where(TransactionField.ACCOUNT_NO,"=",accountNo)
//				.and(TransactionField.TRANSACTION_TYPE, "IN", "(\"DEBIT\",\"WITHDRAW\")")
				.and(TransactionField.DATE_TIME,">=", startOfWeekMillis)
				.and(TransactionField.DATE_TIME,"<=",endOfWeekMillis)
				.and(TransactionField.TRANSACTION_STATUS,"=","SUCCESS")
				.build();
		
		System.out.println(getCreditCount.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getCreditCount.getQuery(), getCreditCount.getValues());
		System.out.println(result);
		if(result.isEmpty())
		{
			return null ;
		}
		Map<String, Object> resultMap = result.get(0);
		Long debitCount = (Long) resultMap.get("debit_count");
		return debitCount ; 
		
	}
	
	public List<Map<String,Object>> getTransactionHistory(Long accountNo,int offset ,long startMillis , long endMillis,String status ) throws TaskException, SQLException
	{

		Query transactionHistory = new QueryBuilder()
				.select(TransactionField.TRANSACTION_ID,TransactionField.ACCOUNT_NO, TransactionField.DATE_TIME, TransactionField.AMOUNT, TransactionField.TRANSACTION_TYPE,
						TransactionField.AVAILABLE_BALANCE,TransactionField.DESCRIPTION)
				.from(TransactionField.ROW_ID).where(TransactionField.ACCOUNT_NO, "=", accountNo)
				.and(TransactionField.DATE_TIME,">=", startMillis)
				.and(TransactionField.DATE_TIME,"<=",endMillis)
				.and(TransactionField.TRANSACTION_STATUS,"=","SUCCESS")
				.limit(11)
				.offset(offset)
				.build();
		System.out.println(transactionHistory.getQuery()+transactionHistory.getValues());
		List<Map<String, Object>> result = qe.executeQuery(transactionHistory.getQuery(),transactionHistory.getValues());
		System.out.println(result);
        return result;

	}
//	public Query updateTransactionStatus(String rowId, String status ,TransactionField transactionField) throws TaskException {
//
//		Query updateStatus = new QueryBuilder().update("transaction").set(transactionField.TRANSACTION_STATUS.getColumnName(), status)
//				.where(transactionField.ROW_ID.getColumnName(), "=", rowId).build();
//
//		return updateStatus;
//	}

	
}
