package com.bank.fintrustbank.factory;

import java.io.IOException;

import com.bank.fintrustbank.model.Account;
import com.bank.fintrustbank.util.AccountNumberGenerator;
import com.zoho.training.exceptions.TaskException;

public class AccountFactory {
	
	public static Account createAccount(String sessionPersonId, 
			String branchId, String customerId, String accountType) throws TaskException, IOException {
		Account account = new Account();
	
	    account.setBranchId(branchId);
	    account.setAccountType(accountType);
	    account.setCustomerId(customerId);

	    account.setAccountNo(AccountNumberGenerator.generateAccountNumber(branchId));
	    account.setModifiedBy(sessionPersonId);
	    account.setCreatedAt(System.currentTimeMillis());
	    account.setStatus("ACTIVE");
	    account.setBalance(0.00);
	    
	    return account;
	        
	    
	}
}
