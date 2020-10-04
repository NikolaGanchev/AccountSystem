package com.accountsystem;

public class NoMatchingAccountException extends Exception{
    public NoMatchingAccountException(String errorMessage){
        super(errorMessage);
    }
}
