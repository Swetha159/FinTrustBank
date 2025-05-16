package com.bank.fintrustbank.service;

import com.bank.fintrustbank.dao.AccountDAO;
import com.bank.fintrustbank.dao.AccountRequestDAO;
import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class NewAccountService {

	PersonDAO personDAO = new PersonDAO();
	AccountDAO accountDAO = new AccountDAO();
	AccountRequestDAO requestDAO = new AccountRequestDAO();
	QueryExecutor qe = new QueryExecutor();
	public boolean createNewCustomerWithAccount(Person person ,Account account) throws TaskException, SQLException
	{
		List<Query> queryList = new ArrayList<>();
		queryList.add(personDAO.getInsertQuery(person));
		queryList.add(accountDAO.getInsertQuery(account));
		queryList.add(requestDAO.getAcceptRequestQuery(person.getPersonId()));
	  if(qe.transact(queryList))
	  {
		  return true;
	  }
	  return false ;
	}

	
}
