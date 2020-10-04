package com.accountsystem;

public class AccountEmailAlreadyExistsException extends Exception{
    public AccountEmailAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
