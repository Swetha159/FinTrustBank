package com.bank.fintrustbank.model;

public class Branch {

    private String branchId;
    private String managerId;
    private String ifscCode;
    private String location;
    private Long createdAt;
    private Long modifiedAt;
    private String  modifiedBy;

	 public static final String TABLE_NAME ="branch";
	 public static final String BRANCH_ID ="branch_id";
	 public static final String MANAGER_ID ="manager_id";
	 public static final String IFSC_CODE ="ifsc_code";
	 public static final String LOCATION ="location";
	 public static final String CREATED_AT ="created_at";
	    public static final String MODIFIED_AT ="modified_at";
	    public static final String MODIFIED_BY ="modified_by";
    
    
    
   
    public Branch() {}

    public Branch(String branchId, String managerId, String ifscCode, String location,
                  Long createdAt, Long modifiedAt, String modifiedBy) {
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

	public Long getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Long modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
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
