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
	
	
    public static final String TABLE_NAME ="account";
	 public static final String ACCOUNT_NO ="account_no";
	 public static final String CUSTOMER_ID = "customer_id";
	 public static final String BRANCH_ID = "branch_id";
	 public static final String ACCOUNT_TYPE = "account_type";
	 public static final String BALANCE = "balance";
	 public static final String CREATED_AT ="created_at";
	    public static final String MODIFIED_AT ="modified_at";
	    public static final String MODIFIED_BY ="modified_by";
	
	
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
