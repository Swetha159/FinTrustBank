package com.bank.fintrustbank.dao;

import java.sql.SQLException;

import com.bank.fintrustbank.model.AccountRequest;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class AccountRequestDAO {

	public boolean addRequest(AccountRequest acRequest) throws TaskException, SQLException {

		QueryExecutor qe = new QueryExecutor();
		Query addRequestQuery = new QueryBuilder().insert("AccountRequest")
				.values(acRequest.getPersonId(), acRequest.getBranchId(), acRequest.getName(), acRequest.getEmail(),
						acRequest.getPhoneNumber(), acRequest.getDob(), acRequest.getAadhar(), acRequest.getPan(),
						acRequest.getAddress(), acRequest.getAccountType(), acRequest.getRequestStatus(),
						acRequest.getCreatedAt(), acRequest.getModifiedAt(), acRequest.getModifiedBy())
				.build();
		System.out.println(addRequestQuery.getQuery());
		int result = qe.execute(addRequestQuery.getQuery(), addRequestQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}

}
