package com.example.smartcart;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Popis.class}, version = 1)
public abstract class MyAppDatabase extends RoomDatabase {

    public abstract MyDao myDao();
}
