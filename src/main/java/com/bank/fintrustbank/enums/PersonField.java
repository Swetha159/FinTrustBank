package com.bank.fintrustbank.enums;

public enum PersonField {
	
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
	
	private final String column;
	
	PersonField(String columnName) {
		this.column = columnName;
			}


	public String getColumnName()
	{
		return this.column;
	}
	
	@Override
	public String toString() {
		return column;
	}
}
