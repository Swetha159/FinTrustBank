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

	private final PersonDAO personDAO = new PersonDAO();
	private final AccountDAO accountDAO = new AccountDAO();
	private final AccountRequestDAO requestDAO = new AccountRequestDAO();
	private final QueryExecutor qe = new QueryExecutor();
	public boolean createNewCustomerWithAccount(Person person ,Account account) throws TaskException, SQLException
	{
		List<Query> queryList = new ArrayList<>();
		queryList.add(personDAO.getInsertQuery(person));
		queryList.add(accountDAO.getInsertQuery(account));
		queryList.add(requestDAO.getAcceptRequestQuery(person.getPersonId(),person.getModifiedAt(),person.getModifiedBy()));
	  if(qe.transact(queryList))
	  {
		  return true;
	  }
	  return false ;
	}

	
}
