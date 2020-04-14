package com.mejdaoui.servicesdemaintenance;

import android.net.Uri;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Demande {
    private String idDemande;
    private String idClient ;
    private List<String> idFonct;
    private String titre ;
    private String description ;
    private String service ;
    private String date_dispo ;
    private Date date_demande;
    private String heure ;
    private int lat_loc ;
    private int long_loc ;
    private String age_fonc;
    private String genre_fon;
    private String uri_picture;
    private String adr_picture;
    private String etat;

    public Demande() {

    }


    public Demande(String idDemande, String idClient, String titre, String description, String service, String date_dispo, String heure, int lat_loc, int long_loc, String age_fonc, String genre_fon,String adr_picture,String etat) {
        this.idDemande = idDemande;
        this.date_demande = new Date();
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
        this.adr_picture=adr_picture;
        this.etat = etat;
    }


    public String getIdDemande() {
        return idDemande;
    }

    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
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

    public String getHeure() {
        return heure;
    }

    public void setHeure(String heure) {
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

    public String getAge_fonc() {
        return age_fonc;
    }

    public void setAge_fonc(String age_fonc) {
        this.age_fonc = age_fonc;
    }

    public String getGenre_fon() {
        return genre_fon;
    }

    public void setGenre_fon(String genre_fon) {
        this.genre_fon = genre_fon;
    }

    public String getUri_picture() {
        return uri_picture;
    }

    public void setUri_picture(String uri_picture) {
        this.uri_picture = uri_picture;
    }

    public String getAdr_picture() {
        return adr_picture;
    }

    public void setAdr_picture(String adr_picture) {
        this.adr_picture = adr_picture;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }


    public List<String> getIdFonct() {
        return idFonct;
    }

    public void setIdFonct(List<String> idFonct) {
        this.idFonct = idFonct;
    }

    public Date getDate() {
        return date_demande;
    }

    public void setDate(Date date_demande) {
        this.date_demande = date_demande;
    }

    @Override
    public String toString() {
        return "Demande{" +
                "idDemande='" + idDemande + '\'' +
                ", idClient='" + idClient + '\'' +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", service='" + service + '\'' +
                ", date_dispo='" + date_dispo + '\'' +
                ", heure='" + heure + '\'' +
                ", lat_loc=" + lat_loc +
                ", long_loc=" + long_loc +
                ", age_fonc='" + age_fonc + '\'' +
                ", genre_fon='" + genre_fon + '\'' +
                ", uri_picture=" + uri_picture +
                ", adr_picture='" + adr_picture + '\'' +
                ", etat='" + etat + '\'' +
                '}';
    }
}

