package com.accountsystem;

import java.util.UUID;

public class Account {
    private String name;
    private String password;
    private UUID uuid;
    private String email;
    private boolean isNull = false;

    public Account(String name, String password, UUID uuid){
        this.name = name;
        this.password = password;
        this.uuid = uuid;
    }

    public Account(String name, String password, UUID uuid, String email){
        this.name = name;
        this.password = password;
        this.uuid = uuid;
        this.email = email;
    }

    public Account(){
        isNull = true;
    }

    public boolean isNull() {
        return isNull;
    }

    public String getName() {
        return this.name;
    }

    public String getUUID() {
        return this.uuid.toString();
    }

    public String getEmail() { return this.email;}
}
