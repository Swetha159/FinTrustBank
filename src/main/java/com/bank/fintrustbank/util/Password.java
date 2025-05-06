package com.bank.fintrustbank.util;

import org.mindrot.jbcrypt.BCrypt;

public class Password {

	private static final int SALT_SIZE = 12;

	public static boolean verifyPassword(String plainText , String hashedText)
	{
		if(BCrypt.checkpw(plainText, hashedText))
		{
			return true;
		}
		else
		{
			return false ; 
		}
	}
	
	public static String createHash(String plainText)
	{
		String hash = BCrypt.hashpw(plainText, BCrypt.gensalt(SALT_SIZE));
		return hash;
	}
}
