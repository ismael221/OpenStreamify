package com.ismael.movies.model.Exceptions;

public class GatewayTimeoutException extends RuntimeException{
    public GatewayTimeoutException(String message){
        super(message);
    }
}
