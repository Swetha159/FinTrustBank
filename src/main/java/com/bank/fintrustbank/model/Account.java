package com.bank.fintrustbank.model;

public class Account {
	private String customerId;
	private long accountNo;
	private String branchId;
	private String accountType;
	private double balance;
	private String status ;
	private long createdAt;
	private long modifiedAt;
	private String modifiedBy;
	
	
	
	
	
	public long getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(long accountNo) {
		this.accountNo = accountNo;
	}
	public String getBranchId() {
		return branchId;
	}
	public void setBranchId(String branchId2) {
		this.branchId = branchId2;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public long getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
	public long getModifiedAt() {
		return modifiedAt;
	}
	public void setModifiedAt(long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String string) {
		this.modifiedBy = string;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String personId) {
		this.customerId = personId;
	}
	
}
