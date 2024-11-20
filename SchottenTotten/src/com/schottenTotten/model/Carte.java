package com.schottenTotten.model;

public class Carte {
    private String couleur; // couleur de la carte
    private int valeur;     // valeur de la carte

    public Carte(String couleur, int valeur) {
        this.couleur = couleur;
        this.valeur = valeur;
    }

    public String getCouleur() {
        return couleur;
    }

    public int getValeur() {
        return valeur;
    }

    @Override
    public String toString() {
        return couleur + " " + valeur;
    }
}
