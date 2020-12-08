package com.example.smartcart;

import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface MyDao {

    @Insert
    public void addPopis(Popis popis);
}
