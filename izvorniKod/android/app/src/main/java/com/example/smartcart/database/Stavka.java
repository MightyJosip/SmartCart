package com.example.smartcart.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "stavka")
public class Stavka {

    @PrimaryKey(autoGenerate = true)
    private int sifStavka;


    // treba dodati anotaciju @ForeignKey
    @ForeignKey(entity = Popis.class , parentColumns = "sifPopis" , childColumns = "sifPopis")  // mozda prebacit u entity
    @ColumnInfo(name = "sifPopis")
    private int sifPopis;

    @ColumnInfo(name = "barkod")
    private String barkod = "653465363";

    @ColumnInfo(name = "cijena")
    private double cijena = 3.99;

    @ColumnInfo(name = "filtarFunkcija")
    private String filtarFunkcija = "nez";

    @ColumnInfo(name = "uKosarici")
    private boolean uKosarici = false;

    @ColumnInfo(name = "kolicina")
    private int kolicina = 1;

    @ColumnInfo(name = "naziv")
    private String naziv;

    @ColumnInfo(name = "omiljeni")
    private boolean omiljeni = false;

    @ColumnInfo(name = "sifTrgovina")
    private int sifTrgovina;



    @Ignore
    public Stavka(int sifPopis, String barkod) {

        this.sifPopis = sifPopis;
        this.barkod = barkod;
    }

    @Ignore
    public Stavka(int sifPopis, String barkod, int sifTrgovina, String naziv) {

        this.sifPopis = sifPopis;
        this.barkod = barkod;
        this.sifTrgovina=sifTrgovina;
        this.naziv =naziv;
    }

    @Ignore
    public Stavka(int sifPopis, String barkod, int sifTrgovina) {

        this.sifPopis = sifPopis;
        this.barkod = barkod;
        this.sifTrgovina=sifTrgovina;

    }


    public Stavka(int sifStavka, int sifPopis, String barkod, double cijena, String filtarFunkcija, boolean uKosarici,
                  int kolicina, String naziv, boolean omiljeni, int sifTrgovina) {
        setSifStavka(sifStavka);
        setSifPopis(sifPopis);
        setBarkod(barkod);
        setCijena(cijena);
        setFiltarFunkcija(filtarFunkcija);
        setUKosarici(uKosarici);
        setKolicina(kolicina);
        setNaziv(naziv);
        setOmiljeni(omiljeni);
        setSifTrgovina(sifTrgovina);
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

    public boolean getOmiljeni() {
        return omiljeni;
    }

    public void setOmiljeni(boolean omiljeni) {
        this.omiljeni = omiljeni;
    }

    public int getSifTrgovina() {
        return sifTrgovina;
    }

    public void setSifTrgovina(int sifTrgovina) {
        this.sifTrgovina = sifTrgovina;
    }

    @Override
    public String toString() {

        if(naziv == null){
            return sifStavka + "  " + sifTrgovina+ "  "+barkod;
        }
        else{
            return naziv + "  "+ sifStavka + "  " + sifTrgovina+ "  "+barkod;
        }



    }

}
