package com.otus.network.exception;

public class NoConnectionException extends RuntimeException {
    public NoConnectionException(Exception e) {
        super(e);
    }
}
