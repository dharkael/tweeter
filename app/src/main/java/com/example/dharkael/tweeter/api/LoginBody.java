package com.example.dharkael.tweeter.api;


public class LoginBody {
    public final String username;
    public final String password;

    public LoginBody(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
