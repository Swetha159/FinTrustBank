package com.bank.fintrustbank.model;

import java.time.LocalDate;
import java.util.Date;

public class AccountRequest {

    private String personId;
    private String name;
    private String email;
    private String phoneNumber;
    private LocalDate dob;
    private long aadhar;
    private String pan;
    private String address;
    private String accountType; 
    private int branchId;
    private String requestStatus;
    private long createdAt ; 
    private long modifiedAt ; 
    private String modifiedBy ; 
        public AccountRequest() {}

    public AccountRequest(String personId, String name, String email, String phoneNumber,
                          LocalDate dob, long aadhar, String pan, String address,
                          String accountType, int branchId , String status) {
        this.personId = personId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.aadhar = aadhar;
        this.pan = pan;
        this.address = address;
        this.accountType= accountType;
        this.branchId = branchId;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String string) {
        this.personId = string;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public long getAadhar() {
        return aadhar;
    }

    public void setAadhar(long aadhar) {
        this.aadhar = aadhar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

	public String getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(String status) {
		this.requestStatus = status;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public long getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(long createdAt) {
		this.createdAt = createdAt;
	}
}