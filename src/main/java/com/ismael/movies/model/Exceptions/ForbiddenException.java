package com.ismael.movies.model.Exceptions;

public class ForbiddenException extends RuntimeException{
    public ForbiddenException (String message){
        super(message);
    }
}
