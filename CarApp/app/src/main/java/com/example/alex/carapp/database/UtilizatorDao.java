package com.example.alex.carapp.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.alex.carapp.vo.Utilizator;

import java.util.List;

@Dao
public interface UtilizatorDao {
    @Query("SELECT * FROM utilizator WHERE nume LIKE :nume AND parola LIKE :parola")
    List<Utilizator> findUtilizator(String nume, String parola);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUtilizator(Utilizator utilizator);
}
