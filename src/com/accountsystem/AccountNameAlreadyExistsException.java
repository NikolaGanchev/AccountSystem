package com.accountsystem;

public class AccountNameAlreadyExistsException extends Exception{
    public AccountNameAlreadyExistsException(String errorMessage){
        super(errorMessage);
    }
}
