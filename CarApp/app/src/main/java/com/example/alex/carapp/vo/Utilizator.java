package com.example.alex.carapp.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Utilizator {
    @PrimaryKey
    @NonNull
    @SerializedName("nume")
    @Expose
    private String nume;
    @SerializedName("parola")
    @Expose
    private String parola;

    public Utilizator(String nume, String parola){
        this.nume = nume;
        this.parola = parola;
    }

    public String getNume() {
        return nume;
    }

    public String getParola() {
        return parola;
    }
}
