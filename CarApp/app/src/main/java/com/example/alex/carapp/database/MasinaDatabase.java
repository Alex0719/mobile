package com.example.alex.carapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.alex.carapp.vo.Masina;
import com.example.alex.carapp.vo.Utilizator;

@Database(entities = {Masina.class, Utilizator.class}, version = 1)
public abstract class MasinaDatabase extends RoomDatabase {
    private static MasinaDatabase INSTANCE;

    public abstract MasinaDao masinaDao();
    public abstract UtilizatorDao utilizatorDao();

    public static MasinaDatabase getMasinaDatabase(Context context) {
        if( INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, MasinaDatabase.class, "masinaDatabase.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {INSTANCE = null;}
}
