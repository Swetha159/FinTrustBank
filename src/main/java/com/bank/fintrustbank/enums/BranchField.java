package com.bank.fintrustbank.enums;

import querybuilder.Column;

public enum BranchField implements Column {
    BRANCH_ID("branch_id"),
    MANAGER_ID("manager_id"),
    IFSC_CODE("ifsc_code"),
    LOCATION("location"),
    CREATED_AT("created_at"),
    MODIFIED_AT("modified_at"),
    MODIFIED_BY("modified_by");

    private static final String TABLE_NAME = "branch";  // Adjust if needed

    private final String column;

    BranchField(String columnName) {
        this.column = columnName;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getColumnName() {
        return column;
    }

    @Override
    public String getQualifiedName() {
        return getTableName() + "." + getColumnName();
    }

    @Override
    public String toString() {
        return getQualifiedName();
    }

}
