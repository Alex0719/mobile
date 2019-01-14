package com.example.alex.carapp.vo;

import java.util.ArrayList;
import java.util.List;

public class Page {
    private int number;
    private List<Masina> masini = new ArrayList<>();

    public int getNumber() {
        return number;
    }

    public List<Masina> getMasini() {
        return masini;
    }

    @Override
    public String toString() {
        return "Page{" +
                "number=" + number +
                ", masini=" + masini +
                '}';
    }
}
