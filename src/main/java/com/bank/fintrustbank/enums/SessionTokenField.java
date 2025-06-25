package com.bank.fintrustbank.enums;


import querybuilder.Column;

public enum SessionTokenField implements Column {
    PERSON_ID("person_id"),
    JTI("jti"),
    ISSUED_AT("issued_at"),
    SESSION_TYPE("session_type"),
    SESSION_STATUS("session_status");

    private static final String TABLE_NAME = "session_token";

    private final String column;

    SessionTokenField(String columnName) {
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

