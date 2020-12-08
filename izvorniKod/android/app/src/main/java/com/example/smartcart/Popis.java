package com.example.smartcart;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "popisi")
public class Popis {

    @PrimaryKey
    private int id;

    private String name;

    private String artikli;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtikli() {
        return artikli;
    }

    public void setArtikli(String artikli) {
        this.artikli = artikli;
    }
}
