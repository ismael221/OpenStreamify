package com.ismael.movies.model.Exceptions;

public class MethodNotAllowedException extends RuntimeException{
    public MethodNotAllowedException(String message){
        super(message);
    }
}
