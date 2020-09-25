package com.accountsystem;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import com.accountsystem.Account;

public class AccountManager{
    public static Account registerAccount(String name, String pass){
        UUID uuid = createUUID();
        Account account = new Account(name, pass, uuid);
        writeToJson(name, pass, uuid);
        return account;
    }

    private static UUID createUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid;
    }

    private static boolean writeToJson(String name, String password, UUID uuid){
        JSONObject json = new JSONObject();
        HashMap<String, String> map = new HashMap<>();
        map.put("Name", name);
        map.put("Password", password);
        map.put("UUID", uuid.toString());
        json.putAll(map);
        try {
            FileWriter file = setFile(uuid);
            file.append(json.toJSONString());
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private static FileWriter setFile(UUID uuid) throws IOException {
        String fileLocation = new File(".").getCanonicalPath() + "/Accounts/" + uuid.toString() + ".json";
        FileWriter file = new FileWriter(fileLocation);
        return file;
    }
}
