package com.bank.fintrustbank.enums;

import querybuilder.Column;

public enum TransactionField implements Column {
    TRANSACTION_ID("transaction_id"),
    CUSTOMER_ID("customer_id"),
    ACCOUNT_NO("account_no"),
    TRANSACTION_ACCOUNT_NO("transaction_account_no"),
    DATE_TIME("date_time"),
    AMOUNT("amount"),
    TRANSACTION_STATUS("transaction_status"),
    TRANSACTION_TYPE("transaction_type"),
    AVAILABLE_BALANCE("available_balance"),
    DESCRIPTION("description"),
    TRANSACTION_BY("transaction_by"),
    ROW_ID("row_id");

    private static final String TABLE_NAME = "transaction"; // Adjust if necessary

    private final String column;

    TransactionField(String columnName) {
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
