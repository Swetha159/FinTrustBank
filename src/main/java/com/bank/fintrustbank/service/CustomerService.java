package com.bank.fintrustbank.service;

import java.sql.SQLException;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;

public class CustomerService {
	
	PersonDAO personDAO = new PersonDAO();
	AccountDAO accountDAO = new AccountDAO();
	QueryExecutor qe = new QueryExecutor();
	
	
	public boolean updateCustomerStatus(String status, String personId, Long modifiedAt, String sessionPersonId) throws TaskException {
		
		try {
			qe.beginTransaction();

			Query updateAccountStatusQuery = accountDAO.getUpdateStatusQuery(status,personId,modifiedAt,sessionPersonId);
			int accountResult = qe.execute(updateAccountStatusQuery.getQuery(), updateAccountStatusQuery.getValues());
			Query updatePersonStatusQuery = personDAO.getUpdateStatusQuery(personId, status, modifiedAt, sessionPersonId);
			int personResult = qe.execute(updatePersonStatusQuery.getQuery(), updatePersonStatusQuery.getValues());
			if (accountResult == 1 && personResult == 1) {
				qe.commitTransaction();
				return true;
			} else {
				qe.rollbackTransaction();
				return false;
			}

		} catch (TaskException | SQLException e) {
			e.printStackTrace();
			try {
				qe.rollbackTransaction();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new TaskException(e.getMessage(), e);
		}
	}
	
	

}
