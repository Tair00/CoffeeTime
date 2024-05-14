package com.example.coffeetime.Interface;

public interface FirebaseTokenListener {
    void onTokenReceived(String token);
    void onTokenError(Exception e);
}

