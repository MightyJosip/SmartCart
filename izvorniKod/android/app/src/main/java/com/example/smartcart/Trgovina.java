package com.example.smartcart;

import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Trgovina {

    @SerializedName("pk")
    private final int pk;
    @SerializedName("naz_trgovina")
    private String naz_trgovina;
    @SerializedName("adresa_trgovina")
    private String adresa_trgovina;
    @SerializedName("radno_vrijeme_pocetak")
    private Time radno_vrijeme_pocetak;
    @SerializedName("radno_vrijeme_kraj")
    private Time radno_vrijeme_kraj;
    @SerializedName("vlasnik")
    private int vlasnik;
    private List<Proizvod> proizvodi;

    public Trgovina(int pk, String naz_trgovina, String adresa_trgovina, Time radno_vrijeme_pocetak, Time radno_vrijeme_kraj, int vlasnik) {
        this.pk = pk;
        this.naz_trgovina = naz_trgovina;
        this.adresa_trgovina = adresa_trgovina;
        this.radno_vrijeme_pocetak = radno_vrijeme_pocetak;
        this.radno_vrijeme_kraj = radno_vrijeme_kraj;
        this.vlasnik = vlasnik;
        proizvodi = new ArrayList<>();
    }

    public Integer getPk() {
        return pk;
    }

    public String getNaz_trgovina() {
        return naz_trgovina;
    }

    public void setNaz_trgovina(String name) {
        this.naz_trgovina = name;
    }

    public String getAdresa_trgovina() {
        return adresa_trgovina;
    }

    public void setAdresa_trgovina(String adresa) {
        this.adresa_trgovina = adresa;
    }

    public Time getRadno_vrijeme_pocetaka() {
        return radno_vrijeme_pocetak;
    }

    public void setRadno_vrijeme_pocetak(Time radno_vrijeme_pocetak) {
        this.radno_vrijeme_pocetak = radno_vrijeme_pocetak;
    }

    public Time getRadno_vrijeme_kraj() {
        return radno_vrijeme_kraj;
    }

    public void setRadno_vrijeme_kraj(Time radno_vrijeme_kraj) {
        this.radno_vrijeme_kraj = radno_vrijeme_kraj;
    }

    public List<Proizvod> getProizvodi() {
        return proizvodi;
    }

    public int getVlasnik() {
        return vlasnik;
    }

    public void setVlasnik(int vlasnik) {
        this.vlasnik = vlasnik;
    }

    public void setProizvodi(List<Proizvod> proizvodi) {
        this.proizvodi = proizvodi;
    }
}
