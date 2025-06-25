package com.bank.fintrustbank.enums;

import querybuilder.Column;

public enum PrivilegedUserField implements Column {
    ADMIN_ID("admin_id"),
    BRANCH_ID("branch_id"),
    CREATED_AT("created_at"),
    MODIFIED_AT("modified_at"),
    MODIFIED_BY("modified_by");

    private static final String TABLE_NAME = "privileged_user";  

    private final String column;

    PrivilegedUserField(String columnName) {
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
