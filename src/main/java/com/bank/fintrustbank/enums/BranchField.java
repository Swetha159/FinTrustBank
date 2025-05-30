package com.bank.fintrustbank.enums;

public enum BranchField {
    BRANCH_ID("branch_id"),
    MANAGER_ID("manager_id"),
    IFSC_CODE("ifsc_code"),
    LOCATION("location"),
    CREATED_AT("created_at"),
    MODIFIED_AT("modified_at"),
    MODIFIED_BY("modified_by");

    private final String columnName;

    BranchField(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    @Override
    public String toString() {
        return columnName;
    }
}

