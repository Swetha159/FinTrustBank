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

		Query insertQuery = new QueryBuilder().insert("transaction")
				.values(transaction.getTransactionId(), transaction.getCustomerId(), transaction.getAccountNo(),
						transaction.getTransactionAccountNo(), transaction.getDateTime(), transaction.getAmount(),
						transaction.getTransactionStatus(), transaction.getTransactionType(),
						transaction.getAvailableBalance(), transaction.getDescription(), transaction.getTransactionBy() ,transaction.getRowId())
				.build();
		return insertQuery;

	}

	public Query updateTransactionStatus(String rowId, String status ) throws TaskException {

	         return updateTransactionStatus(rowId, status ,TransactionField.ROW_ID.getColumnName() ,TransactionField.TRANSACTION_STATUS.getColumnName());
	}

	public Query updateTransactionStatus(String rowId, String status , String rowIdColumn , String statusColumn) throws TaskException {

		Query updateStatus = new QueryBuilder().update("transaction").set(statusColumn, status)
				.where(rowIdColumn, "=", rowId).build();

		return updateStatus;
	}

	public Object getTransactions(String accountNo) throws TaskException, SQLException {

		Query getAllTransaction = new QueryBuilder()
				.select("transaction_id,transaction_account_no", "date_time", "amount", "transaction_type",
						"available_balance")
				.from("transaction").where("account_no", "=", accountNo).and("transaction_status", "=", "SUCCESS")
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
				.count("transaction_id","credit_count")
				.from("transaction")
				.where("account_no","=",accountNo)
//				.and("transaction_type", "IN", "(\"CREDIT\",\"DEPOSIT\")")
				.and("date_time",">=", startOfWeekMillis)
				.and("date_time","<=",endOfWeekMillis)
				.and("transaction_status","=","SUCCESS")
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
				.count("transaction_id","debit_count")
				.from("transaction")
				.where("account_no","=",accountNo)
//				.and("transaction_type", "IN", "(\"DEBIT\",\"WITHDRAW\")")
				.and("date_time",">=", startOfWeekMillis)
				.and("date_time","<=",endOfWeekMillis)
				.and("transaction_status","=","SUCCESS")
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
				.select("transaction_id,transaction_account_no", "date_time", "amount", "transaction_type",
					"available_balance", "description")
				.from("transaction").where("account_no", "=", accountNo)
				.and("date_time",">=", startMillis)
				.and("date_time","<=",endMillis)
				.and("transaction_status","=","SUCCESS")
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
