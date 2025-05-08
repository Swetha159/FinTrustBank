package com.bank.fintrustbank.model;

public class PrivilegedUser {
    private String adminId;
    private String branchId;
    private Long createdAt;
    private Long modifiedAt;
    private String modifiedBy;

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
