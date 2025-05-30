package com.bank.fintrustbank.enums;

public enum AccountRequestField {
    PERSON_ID("person_id"),
    BRANCH_ID("branch_id"),
    ACCOUNT_TYPE("account_type"),
    REQUEST_STATUS("request_status"),
    CREATED_AT("created_at"),
    MODIFIED_AT("modified_at"),
    MODIFIED_BY("modified_by");

    private final String columnName;

    AccountRequestField(String columnName) {
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

