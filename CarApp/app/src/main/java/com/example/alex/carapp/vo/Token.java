package com.example.alex.carapp.vo;

import android.support.annotation.NonNull;

public class Token {

    @NonNull
    String token;

    @NonNull
    Boolean found;

    public Token(@NonNull Boolean found, @NonNull String token) {
        this.found = found;
        this.token = token;
    }

    @NonNull
    public String getToken() {
        return token;
    }

    @NonNull
    public Boolean getFound() {
        return found;
    }
}
