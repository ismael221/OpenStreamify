package com.ismael.movies.model.Exceptions;

public class MediaNotProcessedException extends RuntimeException{
    public MediaNotProcessedException(String message){
        super(message);
    }
}
