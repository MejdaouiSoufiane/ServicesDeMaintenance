package com.mejdaoui.servicesdemaintenance;

import java.util.List;
import java.util.Set;

public class Fonctionnaire {

    private String idFonct ;
    private String nom ;
    private String prenom ;
    private String email ;
    private String pswd ;
    private String adresse ;
    private String ville;
    private String telephone ;
    private List<String> secteur ;
    private String image;

    public Fonctionnaire() {
    }

    public Fonctionnaire(String nom, String prenom, String adresse, String ville, String tel) {
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.telephone = tel;
        this.ville = ville;
    }

    public Fonctionnaire(String idFonct, String nom, String prenom, String email, String adresse, String ville, String telephone, List<String> secteur) {
        this.idFonct = idFonct;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.ville = ville;
        this.telephone = telephone;
        this.secteur = secteur;
    }

    public String getIdFonct() {
        return idFonct;
    }

    public void setIdFonct(String idFonct) {
        this.idFonct = idFonct;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public List<String> getSecteur() {
        return secteur;
    }

    public void setSecteur(List<String> secteur) {
        this.secteur = secteur;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

}
