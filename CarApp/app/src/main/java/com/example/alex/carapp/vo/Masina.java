package com.example.alex.carapp.vo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class Masina {
    @NonNull
    @PrimaryKey
    @Expose
    private Integer id;

    @Expose
    private String marca;

    @Expose
    private String combustibil;

    public Masina(Integer id, String marca, String combustibil) {
        this.id = id;
        this.marca = marca;
        this.combustibil = combustibil;
    }

    public Integer getId() {
        return id;
    }

    public String getCombustibil() {
        return combustibil;
    }

    public String getMarca() {
        return marca;
    }

    @Override
    public String toString(){
        return "" + id + " " + marca + " a alimentat cu " + combustibil;
    }
}
