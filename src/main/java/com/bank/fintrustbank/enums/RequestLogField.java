package com.bank.fintrustbank.enums;

import querybuilder.Column;

public enum RequestLogField implements Column {
    ENDPOINT("endpoint"),
    METHOD("method"),
    REQUEST_ID("request_id"),
    RESPONSE_TIME_MS("response_time_ms"),
    TIMESTAMP("timestamp");

    private static final String TABLE_NAME = "request_log";

    private final String column;

    RequestLogField(String columnName) {
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
