package com.mejdaoui.servicesdemaintenance;

public class ModelCommande {
    private int image;
    private String name,descreption,etat;

    public ModelCommande(int image, String name, String descreption, String etat) {
        this.image = image;
        this.name = name;
        this.descreption = descreption;
        this.etat = etat;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}
