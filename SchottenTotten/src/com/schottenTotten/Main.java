package com.schottenTotten;

import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Muraille;

public class Main {
    public static void main(String[] args) {
        // Initialisation des cartes
        Carte carte1 = new Carte("Rouge", 5);
        Carte carte2 = new Carte("Bleu", 3);

        // Initialisation des joueurs
        Joueur joueur1 = new Joueur("Alice", false);
        Joueur joueur2 = new Joueur("Bob", true);

        // Ajouter des cartes aux mains des joueurs
        joueur1.ajouterCarte(carte1);
        joueur2.ajouterCarte(carte2);

        // Initialisation d'une borne
        Muraille borne = new Muraille();
        borne.ajouterCarte(1, carte1);
        borne.ajouterCarte(2, carte2);

        // Affichage pour vérification
        System.out.println("Carte de Joueur 1: " + joueur1.getMain());
        System.out.println("Carte de Joueur 2: " + joueur2.getMain());
        System.out.println("Cartes sur la Borne pour Joueur 1: " + borne.getCartesJoueur1());
        System.out.println("Cartes sur la Borne pour Joueur 2: " + borne.getCartesJoueur2());
    }
}
