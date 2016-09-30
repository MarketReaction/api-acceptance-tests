package com.jassoft.markets.client.exception;

/**
 * Created by jonshaw on 11/05/2016.
 */
public class ApiClientException extends Exception {

    public ApiClientException(String message) {
        super(message);
    }

    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
