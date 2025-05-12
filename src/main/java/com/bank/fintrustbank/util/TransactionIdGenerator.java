package com.bank.fintrustbank.util;

import java.util.Random;

public class TransactionIdGenerator {

    public static long generateTransactionId(long timestamp, long accountNumber) {
        Random random = new Random();
        
        long randomValue = random.nextInt(99999999); 
        long transactionId = timestamp % 1000000000L; 
        transactionId = transactionId * 100 + randomValue % 100;

        while (String.valueOf(transactionId).length() < 15) {
            transactionId *= 10; 
        }

        return transactionId;
    }

    public static void main(String[] args) {
        long timestamp = System.currentTimeMillis();
        long accountNumber = 1234567890; 

        long transactionId = generateTransactionId(timestamp, accountNumber);
        System.out.println("Generated Transaction ID: " + transactionId);
    }
}
