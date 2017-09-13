package com.ePurchase.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String id){
        super("Entity not found with id : " + id);
    }
}
