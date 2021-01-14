package com.example.smartcart;

import java.util.Map;
import java.util.TreeMap;

public class Proizvod {

    private String ime;
    private String masa;
    private final Integer barkod;
    private String zemljaPorijekla;
    private Integer brojUpVoteova = 0;
    private Integer brojDownVoteova = 0;
    private TreeMap<String, Map<Integer, String>> opis; //za review proizvoda

    public Proizvod(String ime, String masa, Integer barkod, String zemljaPorijekla, Integer brojUpVoteova, Integer brojDownVoteova, TreeMap<String, Map<Integer, String>> opis) {

        this.ime = ime;
        this.masa = masa;
        this.barkod = barkod;
        this.zemljaPorijekla = zemljaPorijekla;
        this.brojUpVoteova = brojUpVoteova;
        this.brojDownVoteova = brojDownVoteova;
        this.opis = opis;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getMasa() {
        return masa;
    }

    public void setMasa(String masa) {
        this.masa = masa;
    }

    public String getZemljaPorijekla() {
        return zemljaPorijekla;
    }

    public void setZemljaPorijekla(String zemljaPorijekla) {
        this.zemljaPorijekla = zemljaPorijekla;
    }

    public Integer getBrojUpVoteova() {
        return brojUpVoteova;
    }

    public void setBrojUpVoteova(Integer brojUpVoteova) {
        this.brojUpVoteova = brojUpVoteova;
    }

    public Integer getBrojDownVoteova() {
        return brojDownVoteova;
    }

    public void setBrojDownVoteova(Integer brojDownVoteova) {
        this.brojDownVoteova = brojDownVoteova;
    }

    public TreeMap<String, Map<Integer, String>> getOpis() {
        return opis;
    }

    public void setOpis(TreeMap<String, Map<Integer, String>> opis) {
        this.opis = opis;
    }

    public Integer getBarkod(){
        return barkod;
    }
}
