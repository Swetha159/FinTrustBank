package com.bank.fintrustbank.enums;


public enum AccountField {
    ACCOUNT_NO("account_no"),
    CUSTOMER_ID("customer_id"),
    BRANCH_ID("branch_id"),
    ACCOUNT_TYPE("account_type"),
    BALANCE("balance"),
    ACCOUNT_STATUS("account_status"),
    CREATED_AT("created_at"),
    MODIFIED_AT("modified_at"),
    MODIFIED_BY("modified_by");

    private final String columnName;

    AccountField(String columnName) {
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

