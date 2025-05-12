package com.bank.fintrustbank.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {
	
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
	
	public Transaction(long transactionId, String customerId, long transactionAccountNo, long accountNo,
			long dateTime, double amount , String transactionStatus, String transactionType, Double availableBalance, String description,
			String transactionBy) {
		
		
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
		transactionAccountNo = transactionAccountNo;
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
	
	

}
