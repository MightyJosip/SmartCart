package com.example.smartcart.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.smartcart.IzracuniCijene;

@Entity(tableName = "popis")
public class Popis {

    @PrimaryKey
    private int sifPopis;

    @ColumnInfo(name = "nazivPopis")
    private String nazivPopis;

    @ColumnInfo(name = "izrCijene")
    private int nacinIzracuna;

    @ColumnInfo(name = "sifTrgovina")
    private int sifTrgovina;

    public Popis(int sifPopis, String nazivPopis, int nacinIzracuna, int sifTrgovina) {
        this.sifPopis = sifPopis;
        this.nazivPopis = nazivPopis;
        this.nacinIzracuna = nacinIzracuna;
        this.sifTrgovina = sifTrgovina;
    }

    @Ignore
    public Popis(int sifPopis, String nazivPopis) {
        this(sifPopis, nazivPopis, IzracuniCijene.DEFAULT, 69420);
    }


    public int getSifPopis() {
        return sifPopis;
    }

    public String getNazivPopis() {
        return nazivPopis;
    }

    public int getNacinIzracuna() {
        return nacinIzracuna;
    }

    public int getSifTrgovina() {
        return sifTrgovina;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        char space = ' ';
        sb.append(sifPopis).append(space)
                .append(nazivPopis).append(space)
                .append(nacinIzracuna).append(space)
                .append(sifTrgovina);
        return sb.toString();
    }
}
