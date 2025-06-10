package com.bank.fintrustbank.enums;

import querybuilder.Column;

public enum ResetTokenField implements Column {
     EMAIL("email") , 
     TOKEN("token") , 
     EXPIRY("expiry") , 
     USED("used");

    private static final String TABLE_NAME = "reset_tokens";  

    private final String column;

    ResetTokenField(String columnName) {
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
