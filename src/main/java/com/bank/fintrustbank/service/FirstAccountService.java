package com.bank.fintrustbank.service;

import java.sql.SQLException;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.dao.AccountRequestDAO;
import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;

public class FirstAccountService {

	PersonDAO personDAO = new PersonDAO();
	AccountDAO accountDAO = new AccountDAO();
	AccountRequestDAO requestDAO = new AccountRequestDAO();
	QueryExecutor qe = new QueryExecutor();

	public boolean requestFirstAccount(AccountRequest acRequest, Person person) throws TaskException {
		try {
			qe.beginTransaction();

			Query personInsertQuery = personDAO.getInsertQuery(person);
			int personResult = qe.execute(personInsertQuery.getQuery(), personInsertQuery.getValues());
			Query requestInsertQuery = requestDAO.getInsertQuery(acRequest);
			int requestResult = qe.execute(requestInsertQuery.getQuery(), requestInsertQuery.getValues());
			if (personResult == 1 && requestResult == 1) {
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

	public boolean approveFirstAccount(Account account) throws TaskException {

		try {
			qe.beginTransaction();

			Query accountInsertQuery = accountDAO.getInsertQuery(account);
			int accountResult = qe.execute(accountInsertQuery.getQuery(), accountInsertQuery.getValues());
			Query requestAcceptQuery = requestDAO.getAcceptRequestQuery(account.getCustomerId() , System.currentTimeMillis() , account.getModifiedBy());
			int requestResult = qe.execute(requestAcceptQuery.getQuery(), requestAcceptQuery.getValues());
			Query makeActiveQuery = personDAO.getUpdateStatusQuery(account.getCustomerId(), "ACTIVE", System.currentTimeMillis() ,account.getModifiedBy());
			int personResult = qe.execute(makeActiveQuery.getQuery(), makeActiveQuery.getValues());
			if (accountResult == 1 && requestResult == 1 && personResult == 1) {
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

	public boolean adminCreateFirstAccount(Person person, Account account) throws TaskException {
		try {
			qe.beginTransaction();
			Query personInsertQuery = personDAO.getInsertQuery(person);
			int personResult = qe.execute(personInsertQuery.getQuery(), personInsertQuery.getValues());
			Query accountInsertQuery = accountDAO.getInsertQuery(account);
			int accountResult = qe.execute(accountInsertQuery.getQuery(), accountInsertQuery.getValues());
		
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

	public boolean rejectFirstAccount(String customerId, String sessionPersonId) throws TaskException {
		
		try {
			qe.beginTransaction();

			Query requestRejectQuery = requestDAO.getRejectRequestQuery(customerId, System.currentTimeMillis() , sessionPersonId);
			int requestResult = qe.execute(requestRejectQuery.getQuery(), requestRejectQuery.getValues());
			Query statusQuery = personDAO.getUpdateStatusQuery(customerId, "REJECTED", System.currentTimeMillis() ,sessionPersonId);
			int personResult = qe.execute(statusQuery.getQuery(), statusQuery.getValues());
			if (requestResult == 1 && personResult == 1) {
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

