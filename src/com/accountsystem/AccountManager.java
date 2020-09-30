package com.accountsystem;

import com.google.crypto.tink.KeysetHandle;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.*;

import com.accountsystem.Account;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.crypto.Data;

public class AccountManager{
    private static HashMap<String, String> uuidNamesHashMap = new HashMap<>();

    static {
        try {
            loadHashMapValues();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    final private static String NAMEKEY = "Name";
    final private static String PASSKEY = "Password";
    final private static String UUIDKEY = "UUID";

    public static Account registerAccount(String name, String pass) throws IOException, ParseException {
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
        writeToJson(name, pass, uuid);
        return account;
    }

    public static Account registerAccountWithEmail(String name, String pass, String email) throws IOException, ParseException, SQLException {
        //TODO Make email
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

    private static void loadHashMapValues() throws IOException, ParseException {
        File folder = new File(new File(".").getCanonicalPath() + "/Accounts");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String filename = listOfFiles[i].getName();
                JSONObject jsonObject = readFromJson(filename.substring(0, filename.length() - 5));

                addNameAndUUIDToHashMap(jsonObject.get(NAMEKEY).toString(), UUID.fromString(jsonObject.get(UUIDKEY).toString()));
            }
        }
    }
    private static UUID createUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    private static void addNameAndUUIDToHashMap(String name, UUID uuid){
        uuidNamesHashMap.put(uuid.toString(), name);
    }

    private static String encryptPassword(String password) throws GeneralSecurityException {
        Encrypter.initEncrypter();
        String encryptedData = Encrypter.encrypt(password);
        return encryptedData;
    }

    private static JSONObject createJSONObject(String name, String password, UUID uuid){
        JSONObject json = new JSONObject();
        HashMap<String, String> map = new HashMap<>();
        map.put(NAMEKEY, name);
        map.put(PASSKEY, password);
        map.put(UUIDKEY, uuid.toString());
        addNameAndUUIDToHashMap(name, uuid);
        json.putAll(map);
        return json;
    }

    private static boolean writeToJson(String name, String password, UUID uuid){
        JSONObject json = createJSONObject(name, password, uuid);
        try {
            FileWriter file = getFileToWriteTo(uuid);
            file.append(json.toJSONString());
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static FileWriter getFileToWriteTo(UUID uuid) throws IOException {
        String fileLocation = new File(".").getCanonicalPath() + "/Accounts/" + uuid.toString() + ".json";
        FileWriter file = new FileWriter(fileLocation);
        return file;
    }

    private static FileReader getFileToReadFrom(String uuid) throws IOException {
        String fileLocation = new File(".").getCanonicalPath() + "/Accounts/" + uuid + ".json";
        FileReader fileReader = new FileReader(fileLocation);
        return fileReader;
    }

    private static JSONObject readFromJson(String uuid) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        FileReader reader = getFileToReadFrom(uuid);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        return jsonObject;
    }

    public static Account logIntoAccount(String name, String password) throws IOException, ParseException, GeneralSecurityException {
        Set<String> uuidSet = getUUIDSetByName(name);
        for (Object obj : uuidSet) {
            JSONObject jsonObject = readFromJson(obj.toString());
            String encryptedPass = jsonObject.get(PASSKEY).toString();
            if (Encrypter.authorizePassword(encryptedPass, password)) {
                return new Account(name, encryptedPass, UUID.fromString(obj.toString()));
            }
        }
        return new Account();
    }

    private static Set<String> getUUIDSetByName(String name){
        Set<String> keys = new HashSet<String>();
        for (Map.Entry<String, String> entry : uuidNamesHashMap.entrySet()) {
            if (Objects.equals(name, entry.getValue())) {
                keys.add(entry.getKey());
            }
        }
        return keys;
    }

    private static boolean nameExists(String name) throws IOException, ParseException {
        return uuidNamesHashMap.containsValue(name);
    }
}
