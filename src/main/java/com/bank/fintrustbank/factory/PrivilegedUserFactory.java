package com.bank.fintrustbank.factory;

import com.bank.fintrustbank.model.PrivilegedUser;

public class PrivilegedUserFactory {
	
	public static PrivilegedUser createPrivilegedUser(String personId, String branchId, String sessionPersonId) {
		
		PrivilegedUser privilegedUser = new PrivilegedUser(personId, branchId, System.currentTimeMillis(), null,
				sessionPersonId);
		return privilegedUser;
	}

}
