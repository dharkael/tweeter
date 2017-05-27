package com.example.dharkael.tweeter.api;


public class LoginResponse {
    public final String userId;
    public final Throwable error;


    public LoginResponse(String userId, Throwable error) {
        this.userId = userId;
        this.error = error;
    }

    public LoginResponse(String userId) {
        this(userId, null);
    }

    public LoginResponse(Throwable error) {
        this(null, error);
    }

    public boolean isSuccessful() {
        return userId != null;
    }
}
