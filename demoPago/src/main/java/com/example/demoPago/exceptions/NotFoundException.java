package com.example.demoPago.exceptions;


public class NotFoundException extends RuntimeException{
    public NotFoundException(String message){
        super((message));
    }
}
