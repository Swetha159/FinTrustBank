package com.bank.fintrustbank.model;

public class ResetToken {
	
	private String email ;
	private Long expiry ;
	private String token ; 
	private Boolean used ;
	
	public ResetToken()
	{
		
	}
	
	public ResetToken(String email ,Long expiry , String token   )
	{
		this.setEmail(email);
		this.setExpiry(expiry);
		this.setToken(token);
		this.setUsed(false);
	}
	
	public Boolean getUsed() {
		return used;
	}
	public void setUsed(Boolean used) {
		this.used = used;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Long getExpiry() {
		return expiry;
	}
	public void setExpiry(Long expiry) {
		this.expiry = expiry;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	} 

}
