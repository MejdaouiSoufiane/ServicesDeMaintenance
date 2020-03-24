package com.mejdaoui.servicesdemaintenance;

import java.util.List;

public class Demande {
    private String idClient ;
    private String titre ;
    private String description ;
    private String service ;
    private String date_dispo ;
    private int heure ;
    private int lat_loc,long_loc ;
    private int age_fonc;
    private String genre_fon;

    public Demande(String idClient, String titre, String description, String service, String date_dispo, int heure, int lat_loc, int long_loc, int age_fonc, String genre_fon) {
        this.idClient = idClient;
        this.titre = titre;
        this.description = description;
        this.service = service;
        this.date_dispo = date_dispo;
        this.heure = heure;
        this.lat_loc = lat_loc;
        this.long_loc = long_loc;
        this.age_fonc = age_fonc;
        this.genre_fon = genre_fon;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDate_dispo() {
        return date_dispo;
    }

    public void setDate_dispo(String date_dispo) {
        this.date_dispo = date_dispo;
    }

    public int getHeure() {
        return heure;
    }

    public void setHeure(int heure) {
        this.heure = heure;
    }

    public int getLat_loc() {
        return lat_loc;
    }

    public void setLat_loc(int lat_loc) {
        this.lat_loc = lat_loc;
    }

    public int getLong_loc() {
        return long_loc;
    }

    public void setLong_loc(int long_loc) {
        this.long_loc = long_loc;
    }

    public int getAge_fonc() {
        return age_fonc;
    }

    public void setAge_fonc(int age_fonc) {
        this.age_fonc = age_fonc;
    }

    public String getGenre_fon() {
        return genre_fon;
    }

    public void setGenre_fon(String genre_fon) {
        this.genre_fon = genre_fon;
    }
}
