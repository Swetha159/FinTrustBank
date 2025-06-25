package com.bank.fintrustbank.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bank.fintrustbank.dao.BranchDAO;
import com.bank.fintrustbank.dao.PersonDAO;
import com.bank.fintrustbank.dao.PrivilegedUserDAO;
import com.bank.fintrustbank.model.Person;
import com.bank.fintrustbank.model.PrivilegedUser;
import com.bank.fintrustbank.util.QueryExecutor;
import com.zoho.training.exceptions.TaskException;

import querybuilder.Query;

public class AdminService {

	private final PersonDAO personDAO = new PersonDAO();
	private final PrivilegedUserDAO privilegedUserDAO = new PrivilegedUserDAO();
	
	private final QueryExecutor qe = new QueryExecutor();
	public boolean addNewAdmin(Person person, PrivilegedUser privilegedUser) throws TaskException, SQLException {
		
		try {
			qe.beginTransaction();

			
			
			Query personInsertQuery = personDAO.getInsertQuery(person);
			int personResult = qe.execute(personInsertQuery.getQuery(), personInsertQuery.getValues());
			Query privilegedUserInsertQuery = privilegedUserDAO.getInsertQuery(privilegedUser);
			int privilegedUserResult = qe.execute(privilegedUserInsertQuery.getQuery(), privilegedUserInsertQuery.getValues());
			if(person.getRole().equals("SUPERADMIN"))
			{
				Query updateAdminQuery = new BranchDAO().getUpdateAdminQuery(privilegedUser.getBranchId(),person.getPersonId() , System.currentTimeMillis() , person.getModifiedBy());
				int branchResult = qe.execute(updateAdminQuery.getQuery(), updateAdminQuery.getValues());
			}
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
	public boolean makeSuperAdmin(String branchId, String personId, Long modifiedAt,String sessionPersonId) throws TaskException {
		try {
			qe.beginTransaction();

			Query roleUpdateQuery = personDAO.getUpdateRoleQuery("SUPERADMIN", personId, modifiedAt, sessionPersonId);
			int personResult = qe.execute(roleUpdateQuery.getQuery(), roleUpdateQuery.getValues());
			Query updateAdminQuery = new BranchDAO().getUpdateAdminQuery(branchId,personId , modifiedAt , sessionPersonId);
			int branchResult = qe.execute(updateAdminQuery.getQuery(), updateAdminQuery.getValues());
		
			if (personResult == 1 && branchResult == 1) {
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
	public boolean changeSuperAdmin(String branchId, String personId, Long modifiedAt,String sessionPersonId) throws TaskException {
		try {
			qe.beginTransaction();
            Query removeSuperAdmin = new BranchDAO().removeManager(personId);
            int removeResult = qe.execute(removeSuperAdmin.getQuery(), removeSuperAdmin .getValues());
			Query roleUpdateQuery = personDAO.getUpdateRoleQuery("SUPERADMIN", personId, modifiedAt, sessionPersonId);
			int personResult = qe.execute(roleUpdateQuery.getQuery(), roleUpdateQuery.getValues());
			Query updateAdminQuery = new BranchDAO().getUpdateAdminQuery(branchId,personId , modifiedAt , sessionPersonId);
			int branchResult = qe.execute(updateAdminQuery.getQuery(), updateAdminQuery.getValues());
			Query privilegedUserUpdateBranchQuery = privilegedUserDAO.getUpdateBranchQuery(branchId, personId);
			int privilegedUserResult = qe.execute(privilegedUserUpdateBranchQuery.getQuery(), privilegedUserUpdateBranchQuery.getValues());
			if (personResult == 1 && branchResult == 1  && removeResult ==1 && privilegedUserResult ==1) {
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
	public boolean makeAdmin(String branchId, String personId, Long modifiedAt, String sessionPersonId) throws TaskException {
		try {
			qe.beginTransaction();

			Query roleUpdateQuery = personDAO.getUpdateRoleQuery("ADMIN", personId, modifiedAt, sessionPersonId);
			int personResult = qe.execute(roleUpdateQuery.getQuery(), roleUpdateQuery.getValues());
			Query updateAdminQuery = new BranchDAO().getUpdateAdminQuery(branchId,(String) null,modifiedAt , sessionPersonId);
			int branchResult = qe.execute(updateAdminQuery.getQuery(), updateAdminQuery.getValues());
			if (personResult == 1 && branchResult == 1) {
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

