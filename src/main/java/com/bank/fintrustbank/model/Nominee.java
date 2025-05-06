package com.bank.fintrustbank.model;

public class Nominee {
	
	private String nomineeName ;
	private String relationship;
	private long nomineeId;
	private long customerId;
	private long accountNo ;
	private int aadhaar ;
	private float percentage ;
	private int priority;
	
	public String getNomineeName() {
		return nomineeName;
	}
	public void setNomineeName(String nomineeName) {
		this.nomineeName = nomineeName;
	}
	public String getRelationship() {
		return relationship;
	}
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	public long getNomineeId() {
		return nomineeId;
	}
	public void setNomineeId(long nomineeId) {
		this.nomineeId = nomineeId;
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
	public int getAadhaar() {
		return aadhaar;
	}
	public void setAadhaar(int aadhaar) {
		this.aadhaar = aadhaar;
	}
	public float getPercentage() {
		return percentage;
	}
	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
}
