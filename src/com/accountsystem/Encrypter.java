package com.accountsystem;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.aead.AesGcmKeyManager;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Encrypter {
    private static KeysetHandle keysetHandle;
    private static boolean isInit = false;
    private static Aead aead;
    public static void initEncrypter() throws GeneralSecurityException {
        AeadConfig.register();

        String filename = "keyfile.json";
        if (keyFileExists(filename)){
            try {
                keysetHandle = loadKeyFromFile(filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            keysetHandle = generateKey();
            try {
                writeKeyToFile(keysetHandle, filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        aead = keysetHandle.getPrimitive(Aead.class);
        isInit = true;
    }

    private static boolean isInit() throws GeneralSecurityException {
        if (!isInit){
            initEncrypter();
            return true;
        }
        else{
            return false;
        }
    }

    private static KeysetHandle generateKey() throws GeneralSecurityException {
        KeysetHandle keysetHandle = KeysetHandle.generateNew(AesGcmKeyManager.aes256GcmTemplate());
        return keysetHandle;
    }

    private static void writeKeyToFile(KeysetHandle keysetHandle, String keysetFilename) throws IOException {
        CleartextKeysetHandle.write(keysetHandle, JsonKeysetWriter.withFile(
                new File(keysetFilename)));
    }

    private static KeysetHandle loadKeyFromFile(String keysetFilename) throws IOException, GeneralSecurityException {
        KeysetHandle keysetHandle = CleartextKeysetHandle.read(
                JsonKeysetReader.withFile(new File(keysetFilename)));
        return keysetHandle;
    }

    private static boolean keyFileExists(String keysetFilename) {
        File file = new File(keysetFilename);
        return file.exists();
    }

    public static String encrypt(String dataToEncrypt, String aad) throws GeneralSecurityException {
        isInit();
        byte[] encryptedData = aead.encrypt(dataToEncrypt.getBytes(), aad.getBytes());
        return encryptedData.toString();
    }

    public static String decrypt(String cypheredData, String aad) throws GeneralSecurityException {
        isInit();
        byte[] decryptedData = aead.decrypt(cypheredData.getBytes(), aad.getBytes());
        return decryptedData.toString();
    }
}
