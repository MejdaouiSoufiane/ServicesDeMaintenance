package com.mejdaoui.servicesdemaintenance;

public class Client {

    private String idClient ;
    private String nom ;
    private String prenom ;
    private String email ;
    private String pswd ;
    private String adresse ;
    private String tel ;

    public Client(){

    }

    public Client(String idClient, String nom, String prenom, String email, String pswd, String adresse, String tel) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.pswd = pswd;
        this.adresse = adresse;
        this.tel = tel;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
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
}
