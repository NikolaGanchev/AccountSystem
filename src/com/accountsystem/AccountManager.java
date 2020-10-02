package com.accountsystem;

import com.sun.jdi.InvalidTypeException;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.*;

import org.json.simple.parser.ParseException;

public class AccountManager{
    final private static String NAMEKEY = "Name";
    final private static String PASSKEY = "Password";
    final private static String UUIDKEY = "UUID";
    static Database database = new Database();

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

    public static Account registerAccountWithEmail(String name, String pass, String email) throws IOException, ParseException, SQLException, InvalidTypeException {
        if (emailExists(email)){
            return new Account();
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

    /*private static void loadHashMapValues() throws IOException, ParseException {
        File folder = new File(new File(".").getCanonicalPath() + "/Accounts");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                JSONObject jsonObject = readFromJson(filename.substring(0, filename.length() - 5));

                addNameAndUUIDToHashMap(jsonObject.get(NAMEKEY).toString(), UUID.fromString(jsonObject.get(UUIDKEY).toString()));
            }
        }
    }*/
    private static UUID createUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    /*private static void addNameAndUUIDToHashMap(String name, UUID uuid){
        uuidNamesHashMap.put(uuid.toString(), name);
    }*/

    private static String encryptPassword(String password) throws GeneralSecurityException {
        Encrypter.initEncrypter();
        String encryptedData = Encrypter.encrypt(password);
        return encryptedData;
    }

    /*public static Account logIntoAccount(String name, String password) throws IOException, ParseException, GeneralSecurityException {
        Set<String> uuidSet = getUUIDSetByName(name);
        for (Object obj : uuidSet) {
            if (Encrypter.authorizePassword(encryptedPass, password)) {
                return new Account(name, encryptedPass, UUID.fromString(obj.toString()));
            }
        }
        return new Account();
    }*/

    /*private static Set<String> getUUIDSetByName(String name){
        Set<String> keys = new HashSet<String>();
        for (Map.Entry<String, String> entry : uuidNamesHashMap.entrySet()) {
            if (Objects.equals(name, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }*/

    private static boolean nameExists(String name) throws SQLException, InvalidTypeException {
        return database.checkIfExists(DATABASECOLUMNS.userName, name);
    }

    private static boolean emailExists(String email) throws SQLException, InvalidTypeException {
        return database.checkIfExists(DATABASECOLUMNS.userEmail, email);
    }
}
