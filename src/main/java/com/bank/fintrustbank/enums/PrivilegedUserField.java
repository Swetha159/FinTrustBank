package com.bank.fintrustbank.enums;

public enum PrivilegedUserField {
	  ADMIN_ID("admin_id"),
	    BRANCH_ID("branch_id"),
	    CREATED_AT("created_at"),
	    MODIFIED_AT("modified_at"),
	    MODIFIED_BY("modified_by");

	    private final String columnName;

	    PrivilegedUserField(String columnName) {
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
