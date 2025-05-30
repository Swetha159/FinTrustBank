package com.bank.fintrustbank.model;

public class PrivilegedUser {
    private String adminId;
    private String branchId;
    private Long createdAt;
    private Long modifiedAt;
    private String modifiedBy;

    public static final String TABLE_NAME ="privileged_user";
    public static final String ADMIN_ID  ="admin_id";
    public static final String BRANCH_ID ="branch_id";
    public static final String CREATED_AT ="created_at";
    public static final String MODIFIED_AT ="modified_at";
    public static final String MODIFIED_BY ="modified_by";
    
    
    // Constructors
    public PrivilegedUser() {
    }

    public PrivilegedUser(String adminId, String branchId, Long createdAt,Long modifiedAt, String modifiedBy) {
        this.adminId = adminId;
        this.branchId = branchId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.modifiedBy = modifiedBy;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

}
