package com.ismael.openstreamify.model.Exceptions;

public class MediaNotProcessedException extends RuntimeException{
    public MediaNotProcessedException(String message){
        super(message);
    }
}
