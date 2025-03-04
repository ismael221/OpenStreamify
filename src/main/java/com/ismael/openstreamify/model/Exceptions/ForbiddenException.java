package com.ismael.openstreamify.model.Exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException (String message){
        super(message);
    }
}
