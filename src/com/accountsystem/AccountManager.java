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
    public static Account registerAccount(String name, String pass) throws SQLException, InvalidTypeException {
        if (nameExists(name)){
            return new Account();
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

    public static Account registerAccountWithEmail(String name, String pass, String email) throws SQLException, InvalidTypeException, AccountException {
        if (emailExists(email)){
            throw new AccountException();
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
        Encrypter.initEncrypter();
        String encryptedData = Encrypter.encrypt(password);
        return encryptedData;
    }

    public static Account logIntoAccountWithEmail(String email, String password) throws SQLException, InvalidTypeException {

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
        return new Account();
    }

    private static boolean nameExists(String name) throws SQLException, InvalidTypeException {
        return database.checkIfExists(DatabaseColumns.USER_NAME, name);
    }

    private static boolean emailExists(String email) throws SQLException, InvalidTypeException {
        return database.checkIfExists(DatabaseColumns.USER_EMAIL, email);
    }

    public static boolean deleteAccount(Account account) throws SQLException {
        database.deleteAccount(account);
        return true;
    }
}
