package com.bank.fintrustbank.util;

public class IFSCCodeGenerator {
	
	public static String createCode(String branchId, String location) 
	{
		String prefix = "FNTBO";
		
        int hash = Math.abs(branchId.hashCode());
        String numericPart = String.format("%06d", hash % 1000000);

        return prefix + numericPart;
    }


}
