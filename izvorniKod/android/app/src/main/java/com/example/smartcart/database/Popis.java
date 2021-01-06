package com.example.smartcart.database;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.smartcart.IzracuniCijenePopisa;

@Entity(tableName = "popis")
public class Popis {

    @PrimaryKey(autoGenerate = true)
    private int sifPopis;

    @ColumnInfo(name = "nazivPopis")
    private String nazivPopis;

    @ColumnInfo(name = "izrCijene")
    private int nacinIzracuna = IzracuniCijenePopisa.DEFAULT;

    @ColumnInfo(name = "sifTrgovina")
    private int sifTrgovina = 69420;

    public Popis(int sifPopis, String nazivPopis, int nacinIzracuna, int sifTrgovina) {
        this.setSifPopis(sifPopis);
        this.setNazivPopis(nazivPopis);
        this.setNacinIzracuna(nacinIzracuna);
        this.setSifTrgovina(sifTrgovina);
    }

    @Ignore
    public Popis(String nazivPopis) {
        this.nazivPopis = nazivPopis;
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
        sb.append(getSifPopis()).append(space)
                .append(getNazivPopis()).append(space)
                .append(getNacinIzracuna()).append(space)
                .append(getSifTrgovina());
        return sb.toString();
    }

    public void setSifPopis(int sifPopis) {
        this.sifPopis = sifPopis;
    }

    public void setNazivPopis(String nazivPopis) {
        this.nazivPopis = nazivPopis;
    }

    public void setNacinIzracuna(int nacinIzracuna) {
        this.nacinIzracuna = nacinIzracuna;
    }

    public void setSifTrgovina(int sifTrgovina) {
        this.sifTrgovina = sifTrgovina;
    }
}
