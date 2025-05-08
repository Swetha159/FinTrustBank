package com.bank.fintrustbank.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.dao.PrivilegedUserDAO;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.model.PrivilegedUser;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;

public class AdminService {

	PersonDAO personDAO = new PersonDAO();
	PrivilegedUserDAO privilegedUserDAO = new PrivilegedUserDAO();
	QueryExecutor qe = new QueryExecutor();
	public boolean addNewAdmin(Person person, PrivilegedUser privilegedUser) throws TaskException, SQLException {
		
		try {
			qe.beginTransaction();

			Query personInsertQuery = personDAO.getInsertQuery(person);
			int personResult = qe.execute(personInsertQuery.getQuery(), personInsertQuery.getValues());
			Query privilegedUserInsertQuery = privilegedUserDAO.getInsertQuery(privilegedUser);
			int privilegedUserResult = qe.execute(privilegedUserInsertQuery.getQuery(), privilegedUserInsertQuery.getValues());
			if (personResult == 1 && privilegedUserResult == 1) {
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

