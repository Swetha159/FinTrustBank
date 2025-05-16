package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;

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
						transaction.getAvailableBalance(), transaction.getDescription(), transaction.getTransactionBy())
				.build();
		return insertQuery;

	}

	public Query updateTransactionStatus(String rowId, String status) throws TaskException {

		Query updateStatus = new QueryBuilder().update("transaction").set("transaction_status", status)
				.where("row_id", "=", rowId).build();

		return updateStatus;
	}

	public Object getTransactions(String accountNo) throws TaskException, SQLException {

		Query getAllTransaction = new QueryBuilder()
				.select("transaction_id,transaction_account_no", "date_time", "amount", "transaction_type",
						"available_balance")
				.from("transaction").where("account_no", "=", accountNo).and("transaction_status", "=", "SUCESS")
				.build();

		List<Map<String, Object>> result = qe.executeQuery(getAllTransaction.getQuery(), getAllTransaction.getValues());
		return result;
	}

	public List<Map<String, Object>> getTransactionHistory(String accountNo) throws TaskException, SQLException {

		Query getTransactionHistory = new QueryBuilder()
				.select("transaction_id,transaction_account_no", "date_time", "amount", "transaction_type",
						"available_balance", "description")
				.from("transaction").where("account_no", "=", accountNo).build();

		List<Map<String, Object>> result = qe.executeQuery(getTransactionHistory.getQuery(),
				getTransactionHistory.getValues());
		return result;
	}
  
	public int getTotalCreditThisWeek(Long accountNo) throws TaskException, SQLException
	{
		 ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC); 

		 ZonedDateTime startOfWeek = now
		     .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
		     .toLocalDate()
		     .atStartOfDay(ZoneOffset.UTC);

		 long startOfWeekMillis = startOfWeek.toInstant().toEpochMilli();

		 ZonedDateTime endOfWeek = now
		     .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		     .toLocalDate()
		     .atTime(LocalTime.MAX)
		     .atZone(ZoneOffset.UTC);

		 long endOfWeekMillis = endOfWeek.toInstant().toEpochMilli();


		Query getCreditCount= new QueryBuilder()
				.select()
				.count("transaction_id","credit_count")
				.from("transaction")
				.where("account_no","=",accountNo)
				.and("transaction_type", "IN", "(\"CREDIT\",\"DEPOSIT\")")
				.and("date_time",">=", startOfWeekMillis)
				.and("data_time","<=",endOfWeekMillis)
				.and("transaction_status","=","SUCCESS")
				.build();
		
		System.out.println(getCreditCount.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getCreditCount.getQuery(), getCreditCount.getValues());
		System.out.println(result);
		Map<String, Object> resultMap = result.get(0);
		int creditCount = (int) resultMap.get("credit_count");
		return creditCount ; 
		
	}
	public int getTotalDebitThisWeek(Long accountNo) throws TaskException, SQLException
	{
		 ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC); 

		 ZonedDateTime startOfWeek = now
		     .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
		     .toLocalDate()
		     .atStartOfDay(ZoneOffset.UTC);

		 long startOfWeekMillis = startOfWeek.toInstant().toEpochMilli();

		 ZonedDateTime endOfWeek = now
		     .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
		     .toLocalDate()
		     .atTime(LocalTime.MAX)
		     .atZone(ZoneOffset.UTC);

		 long endOfWeekMillis = endOfWeek.toInstant().toEpochMilli();


		Query getCreditCount= new QueryBuilder()
				.select()
				.count("transaction_id","debit_count")
				.from("transaction")
				.where("account_no","=",accountNo)
				.and("transaction_type", "IN", "(\"DEBIT\",\"WITHDRAW\")")
				.and("date_time",">=", startOfWeekMillis)
				.and("data_time","<=",endOfWeekMillis)
				.and("transaction_status","=","SUCCESS")
				.build();
		
		System.out.println(getCreditCount.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getCreditCount.getQuery(), getCreditCount.getValues());
		System.out.println(result);
		Map<String, Object> resultMap = result.get(0);
		int creditCount = (int) resultMap.get("debit_count");
		return creditCount ; 
		
	}
	
}
