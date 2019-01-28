package com.dev.mcb;

/**
 * User class
 */
public class User {

    /** The ID */
    public Long id;

    /** The username */
    public String username;

    /** The email */
    public String email;

    /** The password */
    public String password;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
