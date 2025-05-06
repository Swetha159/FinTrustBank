package com.bank.fintrustbank.util;
import java.util.Random;

public class AccountNumberGenerator {

    public static Long generateAccountNumber(String branchId) {
       
        String branchCode = branchId.replaceAll("\\D+", "");
        if (branchCode.length() > 3) {
            branchCode = branchCode.substring(branchCode.length() - 3);
        } else if (branchCode.length() < 3) {
            branchCode = String.format("%03d", Integer.parseInt(branchCode)); 
        }

        
        String timePart = String.valueOf(System.currentTimeMillis());
        timePart = timePart.substring(timePart.length() - 7);

        Random random = new Random();
        int randomPart = random.nextInt(90) + 10; 

        return Long.parseLong(branchCode + timePart + randomPart);
    }

  
}
