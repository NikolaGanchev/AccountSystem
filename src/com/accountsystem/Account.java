package com.accountsystem;

import com.sun.jdi.InvalidTypeException;

import java.sql.SQLException;
import java.util.UUID;

public class Account {
    private String name;
    private String password;
    private UUID uuid;
    private String email;
    private Database database = new Database();

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

    public String getName() {
        return this.name;
    }

    public String getUUID() {
        return this.uuid.toString();
    }

    public String getEmail() { return this.email;}

    public boolean changeEmail(String newEmail) throws SQLException, InvalidTypeException, AccountEmailAlreadyExistsException {
            if(AccountManager.emailExists(newEmail)){
                throw new AccountEmailAlreadyExistsException("Account email '" + email + "' already exists.");
            }
        boolean success = true;
        try {
            database.updateEntry(this, DatabaseColumns.USER_EMAIL, newEmail);
            return success;
        } catch (SQLException | InvalidTypeException throwables) {
            throwables.printStackTrace();
            success = false;
            return success;
        }
    }

    public boolean changeName(String newName){
        boolean success = true;
        try {
            database.updateEntry(this, DatabaseColumns.USER_NAME, newName);
            return success;
        } catch (SQLException | InvalidTypeException throwables) {
            throwables.printStackTrace();
            success = false;
            return success;
        }
    }

    public boolean changePassword(String newPassword){
        boolean success = true;
        String hashedPassword = Encrypter.encrypt(newPassword);
        try{
            database.updateEntry(this, DatabaseColumns.USER_PASSWORD, hashedPassword);
            return success;
        } catch (SQLException | InvalidTypeException throwables) {
            throwables.printStackTrace();
            success = false;
            return success;
        }
    }
}
