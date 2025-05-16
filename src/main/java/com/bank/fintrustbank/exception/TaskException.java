package com.bank.fintrustbank.exception;

public class TaskException extends Exception {
    public TaskException(String message) {
        super(message);
    }
    public TaskException(String message,Exception exception) {
        super(message,exception);
    }
}