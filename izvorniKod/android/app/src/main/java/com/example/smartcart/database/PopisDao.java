package com.example.smartcart.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PopisDao {

    @Insert
    void dodajPopise(Popis... popisi);

    @Query("SELECT * FROM popis")
    List<Popis> dohvatiSvePopise();

    @Query("SELECT * FROM popis WHERE :ime = nazivPopis")
    List<Popis> dohvatiPoNazivu(String ime);

    // i jos mnoge druge

}
