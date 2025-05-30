package com.bank.fintrustbank.enums;

public enum TransactionField {
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

    private final String columnName;

    TransactionField(String columnName) {
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

