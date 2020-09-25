package com.accountsystem;

import java.util.UUID;

public class Account {
    private String name;
    private String password;
    private UUID uuid;
    public Account(String name, String password, UUID uuid){
        this.name = name;
        this.password = password;
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }
}
