package com.example.smartcart.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StavkaDao {


    @Query("SELECT * FROM stavka WHERE stavka.sifPopis = :sifPopis")
    List<Stavka> dohvatiStavkeZaPopis(int sifPopis);

    @Insert
    void dodajStavke(Stavka... stavke);



}
