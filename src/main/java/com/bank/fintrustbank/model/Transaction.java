package com.bank.fintrustbank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Transaction {
	
	private String rowId;
	private long transactionId;
	private String customerId;
	private long accountNo;
	private long transactionAccountNo;
	private long dateTime ; 
	private double amount ; 
	private String transactionStatus ; 
	private String transactionType ;
	private double availableBalance;
	private String description ;
	private String transactionBy ; 
	
	
	 public static final String TABLE_NAME ="transaction";
	 public static final String ROW_ID ="row_id";
	 public static final String ACCOUNT_NO ="account_no";
	 public static final String AMOUNT ="amount";
	 public static final String AVAILABLE_BALANCE  ="available_balance";
	 public static final String  CUSTOMER_ID="customer_id";
	 public static final String DATE_TIME  ="date_time";
	 public static final String DESCRIPTION ="description";
	 public static final String TRANSACTION_BY ="transaction_by";
	 public static final String TRANSACTION_STATUS ="transaction_status";
	 public static final String TRANSACTION_ACCOUNT_NO ="transaction_account_no";
	 public static final String TRANSACTION_TYPE ="transaction_type";
	
	    public Transaction() {}
		
	public Transaction(String rowId , long transactionId, String customerId, long transactionAccountNo, long accountNo,
			long dateTime, double amount , String transactionStatus, String transactionType, Double availableBalance, String description,
			String transactionBy) {
		
		
		this.rowId = rowId ; 
		this.accountNo=accountNo;
		this.amount = amount ; 
		this.availableBalance = availableBalance ;
		this.customerId = customerId;
		this.dateTime = dateTime ; 
		this.description = description;
		this.transactionBy = transactionBy;
		this.transactionId = transactionId;
		this.transactionStatus = transactionStatus ; 
		this.transactionAccountNo = transactionAccountNo;
		this.transactionType = transactionType;
	}

	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public long getTransactionAccountNo() {
		return transactionAccountNo;
	}
	public void setTransactionAccountNo(long transactionAccountNo) {
		this.transactionAccountNo = transactionAccountNo;
	}
	public long getDateTime() {
		return dateTime;
	}
	public void setDateTime(long dateTime) {
		this.dateTime = dateTime;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getTransactionStatus() {
		return transactionStatus;
	}
	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public double getAvailableBalance() {
		return availableBalance;
	}
	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTransactionBy() {
		return transactionBy;
	}
	public void setTransactionBy(String transactionBy) {
		this.transactionBy = transactionBy;
	}
	public String getRowId() {
		return rowId;
	}
	public void setRowId(String rowId) {
		this.rowId = rowId;
	}
	
	

}
