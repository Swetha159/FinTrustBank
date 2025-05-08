package com.bank.fintrustbank.model;

public class AccountRequest {

    private String personId;
    private String accountType; 
    private String branchId;
    private String requestStatus;
    private long createdAt ; 
    private long modifiedAt ; 
    private String modifiedBy ; 
        public AccountRequest() {}

    public AccountRequest(String personId, 
                          String accountType, String branchId , String requestStatus , Long createdAt , Long modifiedAt , String modifiedBy) {
        this.personId = personId;
        this.accountType= accountType;
        this.branchId = branchId;
        this.requestStatus = requestStatus;
        this.createdAt = createdAt ; 
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
        
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String string) {
        this.personId = string;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
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