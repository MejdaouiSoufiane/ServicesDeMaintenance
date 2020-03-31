package com.mejdaoui.servicesdemaintenance;

public class Client {

    private String idClient ;
    private String nom ;
    private String prenom ;
    private String email ;
    private String pswd ;
    private String adresse ;
    private String telephone ;
    private String ville ;

    public Client(){

    }

    public Client(String idClient, String nom, String prenom, String email, String adresse, String ville, String tel) {
        this.idClient = idClient;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.adresse = adresse;
        this.telephone = tel;
        this.ville = ville;
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

    public String getTelelephone() {
        return telephone;
    }

    public void setTelephone(String tel) {
        this.telephone = tel;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }
}

