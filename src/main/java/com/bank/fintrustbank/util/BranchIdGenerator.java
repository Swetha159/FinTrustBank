package com.bank.fintrustbank.util;

public class BranchIdGenerator {
	
	  public static String generateBranchId(String location) {
	        String prefix = "FTB";
	        int hash = Math.abs(location.toLowerCase().hashCode());
	        int branchNumber = hash % 1000; 
	        return prefix + branchNumber;
	    }
}
