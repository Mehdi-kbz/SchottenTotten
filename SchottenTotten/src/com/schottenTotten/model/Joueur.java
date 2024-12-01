package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.List;

public class Joueur {
    private String nom;
    private boolean isIA;
    private List<Carte> main;

    public Joueur(String nom, boolean isIA) {
        this.nom = nom;
        this.isIA = isIA;
        this.main = new ArrayList<>();
    }

    @Override
    public String toString() {
        return nom; // Retourne le nom du joueur
    }

    public String getNom() {
        return nom;
    }

    public boolean isIA() {
        return isIA;
    }

    public void ajouterCarte(Carte carte) {
        main.add(carte);
    }

    public List<Carte> getMain() {
        return main;
    }
}
