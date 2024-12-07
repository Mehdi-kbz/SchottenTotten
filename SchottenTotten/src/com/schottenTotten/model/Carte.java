package com.schottenTotten.model;

public class Carte {
	
	// Variables
    private String couleur;
    private int valeur;
    
    // Constructeur
    public Carte(String couleur, int valeur) {
        this.couleur = couleur != null ? couleur : ""; // Avoid null
        this.valeur = valeur;
    }

    // Getters et Setters
    public String getCouleur() {
        return couleur;
    }

    public int getValeur() {
        return valeur;
    }
    
    public void setCouleur(String couleur) {
        // Par défaut, ne fait rien.
        // Cette méthode sera surchargée dans TacticalCard.
    }
    
    @Override
    public String toString() {
        return couleur + " " + valeur;
    }
}
