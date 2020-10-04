package com.accountsystem;

import com.sun.jdi.InvalidTypeException;
import java.security.GeneralSecurityException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import javax.security.auth.login.AccountException;

public class AccountManager{
    final private static String NAME_KEY = "userName";
    final private static String PASS_KEY = "userPassword";
    final private static String UUID_KEY = "userUUID";
    final private static String EMAIL_KEY = "userEmail";
    static Database database = new Database();

    @Deprecated
    public static Account registerAccount(String name, String pass) throws SQLException, InvalidTypeException, AccountNameAlreadyExistsException {
        if (nameExists(name)){
            throw new AccountNameAlreadyExistsException("Account name '" + name + "' already exists.");
        }
        UUID uuid = createUUID();
        try {
            pass = encryptPassword(pass);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Account account = new Account(name, pass, uuid);

        return account;
    }

    public static Account registerAccountWithEmail(String name, String pass, String email) throws SQLException, InvalidTypeException, AccountException, AccountEmailAlreadyExistsException {
        if (emailExists(email)){
            throw new AccountEmailAlreadyExistsException("Account email '" + email + "' already exists.");
        }
        UUID uuid = createUUID();
        try {
            pass = encryptPassword(pass);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        Account account = new Account(name, pass, uuid, email);
        Database database = new Database();
        database.createDatabaseEntry(name, pass, uuid.toString(), email);
        return account;
    }

    private static UUID createUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    private static String encryptPassword(String password) throws GeneralSecurityException {
        String encryptedData = Encrypter.encrypt(password);
        return encryptedData;
    }

    public static Account logIntoAccountWithEmail(String email, String password) throws SQLException, InvalidTypeException, NoMatchingAccountException {

        ResultSet resultSet = database.getDatabaseProfiles(DatabaseColumns.USER_EMAIL, email);
        while(resultSet.next()){
            String currentAccountPassword = resultSet.getString(PASS_KEY);
            if(Encrypter.authorizePassword(currentAccountPassword, password)){
                String currentAccountEmail = resultSet.getString(EMAIL_KEY);
                String currentAccountName = resultSet.getString(NAME_KEY);
                String currentAccountUUID = resultSet.getString(UUID_KEY);
                return new Account(currentAccountName, currentAccountPassword, UUID.fromString(currentAccountUUID), currentAccountEmail);
            }
        }
        throw new NoMatchingAccountException("Could not find account with matching credentials.");
    }

    private static boolean nameExists(String name) throws SQLException, InvalidTypeException {
        return database.checkIfExists(DatabaseColumns.USER_NAME, name);
    }

    public static boolean emailExists(String email) throws SQLException, InvalidTypeException {
        return database.checkIfExists(DatabaseColumns.USER_EMAIL, email);
    }

    public static boolean deleteAccount(Account account) throws SQLException {
        database.deleteAccount(account);
        return true;
    }
}
