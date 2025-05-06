package com.bank.fintrustbank.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class IdCreation {

    public static String createPersonId() {
        String prefix = "FTB03";
        Random rand = new Random();
        
        long currentTimeMillis = System.currentTimeMillis();
        
        int randomNumber = rand.nextInt(900) + 100;  
        String uniqueString = currentTimeMillis + String.valueOf(randomNumber);

                String hash = generateHash(uniqueString);

        return prefix + hash.substring(0, 7);
    }

    private static String generateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }
}
