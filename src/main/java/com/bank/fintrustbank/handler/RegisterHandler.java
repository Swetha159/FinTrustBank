package com.bank.fintrustbank.handler;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.zaxxer.hikari.HikariDataSource;

public class RegisterHandler implements HttpRequestHandler{

	  @Override
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		  
			 String path = request.getPathInfo();
			 if(path.equals("/register"))
			 {
				 handleRegister(request, response);
				 
				 
			 }
			 
	  }

	private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		ServletContext context = request.getServletContext();
		HikariDataSource dataSource = (HikariDataSource) context.getAttribute("dataSource");
		 StringBuilder sb = new StringBuilder();
		BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        JSONObject json = new JSONObject(sb.toString());
        
        String personId = json.getString("person_id");
        String branchId = json.getString("branch_id");
        String name = json.getString("name");
        String email = json.getString("email");
        String phoneNumber = json.getString("phone_number");
        String dob = json.getString("dob"); 
        Long aadhar = json.getLong("aadhar");
        String pan = json.getString("pan");
        String address = json.getString("address");
        String accountType = json.getString("account_type");
        String status = json.getString("status");
        String createdAt = json.getString("created_at"); 
        String modifiedAt = json.getString("modified_at");
        String modifiedBy = json.getString("modified_by");
        
	}
	
}
