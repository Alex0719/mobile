package com.example.alex.carapp.database;

import android.content.Context;
import android.preference.PreferenceManager;

public class TokenDatabase {
    private static TokenDatabase instance;
    private String token = null;
    private Context context;

    private TokenDatabase(Context context) {this.context = context;}

    public static TokenDatabase getInstance(Context context) {
        if(instance == null) {
            instance = new TokenDatabase(context);
        }
        return instance;
    }

    public String getToken() {
        if( token != null) return token;
        return PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("token", null);

    }

    public void insertToken(String token) {
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .putString("token", token)
                .apply();
        this.token = token;
    }

    public void deleteToken(){
        this.token = null;
        PreferenceManager
                .getDefaultSharedPreferences(context)
                .edit()
                .remove("token")
                .apply();
    }
}
