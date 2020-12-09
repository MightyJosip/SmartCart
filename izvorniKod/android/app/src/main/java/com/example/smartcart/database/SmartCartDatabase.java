package com.example.smartcart.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Popis.class, Stavka.class}, version = 2)
public abstract class SmartCartDatabase extends RoomDatabase {

    private static SmartCartDatabase db;
    private static final String DB_NAME = "smartcart";

    public abstract PopisDao popisDao();

    public abstract StavkaDao stavkaDao();

    public static SmartCartDatabase getInstance(Context c) {
        if (db != null)
            return db;

        db = Room.databaseBuilder(c.getApplicationContext(), SmartCartDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .build();
        Log.i("getInstance", "Stvorena lokalna baza " + DB_NAME);
        return db;
    }



}
