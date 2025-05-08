package com.bank.fintrustbank.model;

public class Branch {

    private String branchId;
    private String managerId;
    private String ifscCode;
    private String location;
    private long createdAt;
    private long modifiedAt;
    private String  modifiedBy;

   
    public Branch() {}

    public Branch(String branchId, String managerId, String ifscCode, String location,
                  long createdAt, long modifiedAt, String modifiedBy) {
        this.setBranchId(branchId);
        this.setManagerId(managerId);
        this.setIfscCode(ifscCode);
        this.setLocation(location);
        this.setCreatedAt(createdAt);
        this.setModifiedAt(modifiedAt);
        this.setModifiedBy(modifiedBy);
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

}
