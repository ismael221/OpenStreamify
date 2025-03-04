package com.ismael.openstreamify.model.Exceptions;

public class GatewayTimeoutException extends RuntimeException{
    public GatewayTimeoutException(String message){
        super(message);
    }
}
