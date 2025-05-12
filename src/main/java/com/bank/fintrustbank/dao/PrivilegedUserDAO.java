package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.model.PrivilegedUser;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class PrivilegedUserDAO {

	QueryExecutor qe = new QueryExecutor();
	public Query getInsertQuery(PrivilegedUser privilegedUser) throws TaskException {
		
		Query insertQuery = new QueryBuilder()
							.insert("privileged_users")
							.values(privilegedUser.getAdminId(),privilegedUser.getBranchId(),privilegedUser.getCreatedAt(),
									privilegedUser.getModifiedAt(),privilegedUser.getModifiedBy())
							.build();
		
		return insertQuery;
	}



}
