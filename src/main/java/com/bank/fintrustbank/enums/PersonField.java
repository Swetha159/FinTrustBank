package com.bank.fintrustbank.enums;

import querybuilder.Column;

public enum PersonField implements Column {
    
    PERSON_ID("person_id"),
    NAME("name"),
    EMAIL("email"),
    PHONE_NUMBER("phone_number"),
    ROLE("role"),
    PASSWORD("password"),
    STATUS("status"),
    DOB("dob"),
    AADHAR("aadhar"),
    PAN("pan"),
    ADDRESS("address"),
    CREATED_AT("created_at"),
    MODIFIED_AT("modified_at"),
    MODIFIED_BY("modified_by");
    
    private static final String TABLE_NAME = "person";  // Adjust if needed
    
    private final String column;

    PersonField(String columnName) {
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
    
    
    
    
    
    
    
 



  

   

