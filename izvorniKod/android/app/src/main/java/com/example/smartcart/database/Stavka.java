package com.example.smartcart.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import kotlin.UByteArray;

@Entity(tableName = "stavka")
public class Stavka {

    @PrimaryKey(autoGenerate = true)
    private int sifStavka;


    // treba dodati anotaciju @ForeignKey
    @ColumnInfo(name = "sifPopis")
    private int sifPopis;

    @ColumnInfo(name = "barkod")
    private String barkod;

    @ColumnInfo(name = "cijena")
    private double cijena;

    @ColumnInfo(name = "filtarFunkcija")
    private String filtarFunkcija;

    @ColumnInfo(name = "uKosarici")
    private boolean uKosarici;

    @ColumnInfo(name = "kolicina")
    private int kolicina;

    @ColumnInfo(name = "naziv")
    private String naziv;

    @Ignore
    public Stavka() {

    }

    public Stavka(int sifStavka, int sifPopis, String barkod, double cijena, String filtarFunkcija, boolean uKosarici,
                  int kolicina, String naziv) {
        setSifStavka(sifStavka);
        setSifPopis(sifPopis);
        setBarkod(barkod);
        setCijena(cijena);
        setFiltarFunkcija(filtarFunkcija);
        setUKosarici(uKosarici);
        setKolicina(kolicina);
        setNaziv(naziv);
    }

    public int getSifPopis() {
        return sifPopis;
    }

    public void setSifPopis(int sifPopis) {
        this.sifPopis = sifPopis;
    }

    public int getSifStavka() {
        return sifStavka;
    }

    public void setSifStavka(int sifStavka) {
        this.sifStavka = sifStavka;
    }

    public String getBarkod() {
        return barkod;
    }

    public void setBarkod(String barkod) {
        this.barkod = barkod;
    }

    public double getCijena() {
        return cijena;
    }

    public void setCijena(double cijena) {
        this.cijena = cijena;
    }

    public String getFiltarFunkcija() {
        return filtarFunkcija;
    }

    public void setFiltarFunkcija(String filtarFunkcija) {
        this.filtarFunkcija = filtarFunkcija;
    }

    public boolean getUKosarici() {
        return uKosarici;
    }

    public void setUKosarici(boolean uKosarici) {
        this.uKosarici = uKosarici;
    }

    public int getKolicina() {
        return kolicina;
    }

    public void setKolicina(int kolicina) {
        this.kolicina = kolicina;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }


}
