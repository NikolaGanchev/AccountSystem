package com.accountsystem;

import javax.lang.model.type.NullType;
import java.util.UUID;

public class Account {
    private String name;
    private String password;
    private UUID uuid;
    private boolean isNull = false;
    public Account(String name, String password, UUID uuid){
        this.name = name;
        this.password = password;
        this.uuid = uuid;
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
        return uuid.toString();
    }
}
