package com.bank.fintrustbank.enums;

import querybuilder.Column;

public enum AccountField implements Column {
    ACCOUNT_NO("account_no"),
    CUSTOMER_ID("customer_id"),
    BRANCH_ID("branch_id"),
    ACCOUNT_TYPE("account_type"),
    BALANCE("balance"),
    ACCOUNT_STATUS("account_status"),
    CREATED_AT("created_at"),
    MODIFIED_AT("modified_at"),
    MODIFIED_BY("modified_by");

    private static final String TABLE_NAME = "account";
    private final String column;

    AccountField(String columnName) {
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
