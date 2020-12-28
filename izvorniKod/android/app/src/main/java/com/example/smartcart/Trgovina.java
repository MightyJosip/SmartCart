package com.example.smartcart;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class Trgovina {

    private final int identikator;
    private String ime;
    private String adresa;
    private Time vrijemeOtvaranja;
    private Time vrijemeZatvaranja;
    private List<Proizvod> proizvodi;

    public Trgovina(int identikator, String ime, String adresa, Time vrijemeOtvaranja, Time vrijemeZatvaranja, int vlasnik) {
        this.identikator = identikator;
        this.ime = ime;
        this.adresa = adresa;
        this.vrijemeOtvaranja = vrijemeOtvaranja;
        this.vrijemeZatvaranja = vrijemeZatvaranja;
        proizvodi = new ArrayList<>();
    }

    public Integer getIdentikator() {
        return identikator;
    }

    public String getName() {
        return ime;
    }

    public void setName(String name) {
        this.ime = name;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Time getVrijemeOtvaranja() {
        return vrijemeOtvaranja;
    }

    public void setVrijemeOtvaranja(Time vrijemeOtvaranja) {
        this.vrijemeOtvaranja = vrijemeOtvaranja;
    }

    public Time getVrijemeZatvaranja() {
        return vrijemeZatvaranja;
    }

    public void setVrijemeZatvaranja(Time vrijemeZatvaranja) {
        this.vrijemeZatvaranja = vrijemeZatvaranja;
    }

    public List<Proizvod> getProizvodi() {
        return proizvodi;
    }

    public void setProizvodi(List<Proizvod> proizvodi) {
        this.proizvodi = proizvodi;
    }
}
