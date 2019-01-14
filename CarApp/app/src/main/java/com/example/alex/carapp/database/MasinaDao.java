package com.example.alex.carapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.alex.carapp.vo.Masina;

import java.util.List;

@Dao
public interface MasinaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(List<Masina> masina);

    @Update
    void update(Masina masina);

    @Query("SELECT * FROM masina")
    List<Masina> load();
}
