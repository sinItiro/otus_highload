package com.otus.network.exception;

public class NotExecuteQueryException extends RuntimeException{
    public NotExecuteQueryException(String message, Exception e) {
        super(message, e);
    }
    public NotExecuteQueryException(String message) {
        super(message);
    }
}
