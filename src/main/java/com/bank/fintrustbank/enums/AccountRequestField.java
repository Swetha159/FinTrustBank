package com.bank.fintrustbank.enums;

import querybuilder.Column;

public enum AccountRequestField implements Column {
    PERSON_ID("person_id"),
    BRANCH_ID("branch_id"),
    ACCOUNT_TYPE("account_type"),
    REQUEST_STATUS("request_status"),
    CREATED_AT("created_at"),
    MODIFIED_AT("modified_at"),
    MODIFIED_BY("modified_by");

    private static final String TABLE_NAME = "account_request";  // Adjust to your actual table name

    private final String column;

    AccountRequestField(String columnName) {
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
