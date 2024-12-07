package com.schottenTotten.model;
import java.util.ArrayList;
import java.util.List;

public class Joueur {
	
	// Variables
    private String nom;
    private boolean isIA;
    private List<Carte> main;
    
    // Constructeur
    public Joueur(String nom, boolean isIA) {
        this.nom = nom;
        this.isIA = isIA;
        this.main = new ArrayList<>();
    }

    @Override
    public String toString() {
        return nom; // Retourne le nom du joueur
    }

    // Getter pour avoir le nom d'un joueur
    public String getNom() {
        return nom;
    }

    // Méthode pour renvoyer si c'est une ia ou pas
    public boolean isIA() {
        return isIA;
    }

    // Méthode pour ajputer une carte à la main d'un joueur
    public void ajouterCarte(Carte carte) {
        main.add(carte);
    }

    // Méthode pour afficher la cartes dans la main d'un joueur
    public List<Carte> getMain() {
        return main;
    }
}
