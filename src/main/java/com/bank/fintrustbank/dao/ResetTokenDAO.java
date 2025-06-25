package com.bank.fintrustbank.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.bank.fintrustbank.enums.ResetTokenField;
import com.bank.fintrustbank.model.ResetToken;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;
import querybuilder.QueryBuilder;

public class ResetTokenDAO {

	private final QueryExecutor qe = new QueryExecutor();

	public boolean saveToken(ResetToken resetToken) throws SQLException, TaskException {

		Query saveTokenQuery = new QueryBuilder().insert(ResetTokenField.TOKEN)
				.values(resetToken.getEmail(), resetToken.getToken(), resetToken.getExpiry(), resetToken.getUsed())
				.build();

		System.out.println(saveTokenQuery.getQuery());
	
		int result = qe.execute(saveTokenQuery.getQuery(), saveTokenQuery.getValues());
	

		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;
	}

	public Map<String, Object> getExpiryAndUsed(String token) throws SQLException, TaskException {

		Query isValidToken = new QueryBuilder().select(ResetTokenField.EXPIRY, ResetTokenField.USED)
				.from(ResetTokenField.TOKEN).where(ResetTokenField.TOKEN, "=", token).build();

		System.out.println(isValidToken.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(isValidToken.getQuery(), isValidToken.getValues());

		if (!(result.isEmpty())) {
			return result.get(0);
		}
		return null;

	}

	public String getEmail(String token) throws SQLException, TaskException {

		Query getEmailQuery = new QueryBuilder().select(ResetTokenField.EMAIL).from(ResetTokenField.TOKEN)
				.where(ResetTokenField.TOKEN, "=", token).build();
		System.out.println(getEmailQuery.getQuery());
		List<Map<String, Object>> result = qe.executeQuery(getEmailQuery.getQuery(), getEmailQuery.getValues());

		if (!(result.isEmpty())) {
			Map<String, Object> map = result.get(0);
			return (String) map.get("email");
		}
		return null;

	}

	public Boolean markUsed(String token) throws SQLException, TaskException {

		Query markUsedQuery = new QueryBuilder().update(ResetTokenField.TOKEN).set(ResetTokenField.USED, true)
				.where(ResetTokenField.TOKEN, "=", token).build();

		System.out.println(markUsedQuery.getQuery());
		int result = qe.execute(markUsedQuery.getQuery(), markUsedQuery.getValues());
		System.out.println(result);
		if (result > 0) {
			return true;
		}
		return false;

	}
}
