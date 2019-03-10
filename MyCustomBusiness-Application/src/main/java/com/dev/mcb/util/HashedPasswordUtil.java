package com.dev.mcb.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashedPasswordUtil {

    /**
     * a security salt
    */
    private String salt;

    public HashedPasswordUtil(String salt) {
        this.salt = salt;
    }

    /**
     * Create a Hashed password used to store encrypted password in database
     * @param password the client's send password
     * @return a hashed password
     */
    public String getHashedPassword(String password) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(Byte.parseByte(this.salt));
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;
    }
}
