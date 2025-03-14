package com.ismael.openstreamify.model.Exceptions;

public class MethodNotAllowedException extends RuntimeException{
    public MethodNotAllowedException(String message){
        super(message);
    }
}
