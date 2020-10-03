package com.accountsystem;

import static com.kosprov.jargon2.api.Jargon2.*;

public class Encrypter {

    public static String encrypt(String dataToEncrypt){
        byte[] password = dataToEncrypt.getBytes();

        Hasher hasher = jargon2Hasher()
                .type(Type.ARGON2id)
                .memoryCost(65536)
                .timeCost(3)
                .parallelism(3)
                .saltLength(16)
                .hashLength(16);

        String passwordHash = hasher.password(password).encodedHash();

        return passwordHash;
    }

    public static boolean authorizePassword(String passwordHash, String attemptedPassword){
        Verifier verifier = jargon2Verifier();

        boolean hashesMatch = verifier.hash(passwordHash).password(attemptedPassword.getBytes()).verifyEncoded();
        return hashesMatch;
    }
}
