package com.mejdaoui.servicesdemaintenance;

public class Fonctionnaire {

    private String idFonct ;
    private String nom ;
    private String prenom ;
    private String email ;
    private String pswd ;
    private String adresse ;
    private String tel ;
    private String secteur ;

    public Fonctionnaire() {
    }

    public Fonctionnaire(String idFonct, String nom, String prenom, String email, String pswd, String adresse, String tel, String secteur) {
        this.idFonct = idFonct;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pswd = pswd;
        this.adresse = adresse;
        this.tel = tel;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }
}
