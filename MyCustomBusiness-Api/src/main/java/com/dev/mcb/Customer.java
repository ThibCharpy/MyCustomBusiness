package com.dev.mcb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Customer {

    private long id;
    private String name;
    private String password;
    private String mail;

    public Customer() {
    }

    public Customer(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty
    public String getMail() {
        return mail;
    }

    @JsonProperty
    public void setMail(String mail) {
        this.mail = mail;
    }
}
