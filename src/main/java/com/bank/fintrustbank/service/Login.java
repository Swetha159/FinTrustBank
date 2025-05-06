package com.bank.fintrustbank.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.model.Person;
import com.zoho.training.exceptions.TaskException;


public class Login {

	public static void checkCredentials(String email, String password)  throws SQLException, TaskException{		
		
//		PersonDAO dao = new PersonDAO();
//		Person person =dao.checkCredentials(email, password);
//	    return person ; 
		
	}
	  
}