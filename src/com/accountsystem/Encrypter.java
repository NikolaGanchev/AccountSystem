package com.accountsystem;

import com.google.crypto.tink.*;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadKeyTemplates;
import com.google.crypto.tink.aead.AesGcmKeyManager;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class Encrypter {
    private static SecureRandom random = new SecureRandom();
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

    public static String encrypt(String dataToEncrypt) throws GeneralSecurityException {
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        KeySpec spec = new PBEKeySpec(dataToEncrypt.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        System.out.printf("salt: %s%n", enc.encodeToString(salt));
        System.out.printf("hash: %s%n", enc.encodeToString(hash));
        return enc.encodeToString(hash);
    }

    public static String encryptWithGivenSalt(String dataToEncrypt, byte[] salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeySpec spec = new PBEKeySpec(dataToEncrypt.toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        System.out.printf("salt: %s%n", enc.encodeToString(salt));
        System.out.printf("hash: %s%n", enc.encodeToString(hash));
        return enc.encodeToString(hash);
    }
}
