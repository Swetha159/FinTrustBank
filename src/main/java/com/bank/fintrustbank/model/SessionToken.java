package com.bank.fintrustbank.model;


public class SessionToken {
    private String personId;
    private String jti;
    private Long issuedAt ; 
    private String sessionType ; 
    private String sessionStatus;

    public SessionToken(String personId, String jti , Long issuedAt , String sessionType, String sessionStatus) {
        this.personId = personId;
        this.jti = jti;
        this.issuedAt = issuedAt ; 
        this.sessionType = sessionType ; 
        this.sessionStatus = sessionStatus ; 
    }

    public String getPersonId() { return personId; }
    public String getJti() { return jti; }
    public Long getIssuedAt() { return issuedAt; }
    public String getSessionType() { return sessionType; }
    public String getSessionStatus() { return sessionStatus; }

    public void setPersonId(String personId) { this.personId = personId; }
    public void setJti(String jti) { this.jti = jti;}
    public void setIssuedAt(Long issuedAt) { this.issuedAt = issuedAt; }
    public void setSessionType(String sessionType) { this.sessionType = sessionType; }
    public void setSessionStatus(String sessionStatus) { this.sessionStatus = sessionStatus; }
    
}
