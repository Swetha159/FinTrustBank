package com.bank.fintrustbank.model;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Person {
    private String personId;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
    private String password;
    private String status;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private  Date dob;
    private Long aadhar;
    private String pan;
    private String address;
    private Long createdAt;
    private Long modifiedAt;
    private String modifiedBy;

   public static final String TABLE_NAME ="person";
    public static final  String PERSON_ID = "person_id";
    public static final  String NAME = "name";
    public static final String EMAIL = "email";
    public static final  String PHONE_NUMBER = "phone_number";
    public static final  String ROLE = "role";
    public static final  String PASSWORD = "password";
    public static final  String STATUS = "status";
    public static final  String DOB = "dob";
    public static final  String AADHAR = "aadhar";
    public static final  String PAN = "pan";
    public static final  String ADDRESS = "address";
    public static final  String CREATED_AT = "created_at";
    public static final  String MODIFIED_AT = "modified_at";
    public static final  String MODIFIED_BY = "modified_by";
    
    
    
    
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Long getAadhar() {
        return aadhar;
    }

    public void setAadhar(Long aadhar) {
        this.aadhar = aadhar;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(Long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
