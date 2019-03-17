package com.dev.mcb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class HashedPasswordUtil {

    public HashedPasswordUtil() {
    }

    /**
     * Generate a salt value to hash password
     * @return the salt
     */
    public String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return new String(salt);
    }

    /**
     * Create a Hashed password used to store encrypted password in database
     * @param password the client's send password
     * @return a hashed password
     */
    public String getHashedPassword(String password, String salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(Byte.parseByte(salt));
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    /**
     * Te test if an input password is equals to a hashed password
     * @param password the tested password
     * @param hashedPassword the hashed password
     * @param salt the salt
     * @return true if the password hased is equals to hashed passwords
     */
    public boolean passwordEqualToHash(String password, String hashedPassword, String salt) {
        return hashedPassword.equals(getHashedPassword(password, salt));
    }
}
