package com.bank.fintrustbank.model;

public class Transaction {
	
	private long transactionId;
	private long customerId;
	private long accountNo;
	private long TransactionAccountNo;
	private long dateTime ; 
	private double amount ; 
	private String status ; 
	private String transactionType ;
	private double closingBalance;
	private String description ;
	private long transactionBy ; 
	
	public long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public long getTransactionAccountNo() {
		return TransactionAccountNo;
	}
	public void setTransactionAccountNo(long transactionAccountNo) {
		TransactionAccountNo = transactionAccountNo;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	public double getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(double closingBalance) {
		this.closingBalance = closingBalance;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getTransactionBy() {
		return transactionBy;
	}
	public void setTransactionBy(long transactionBy) {
		this.transactionBy = transactionBy;
	}
	
	

}
