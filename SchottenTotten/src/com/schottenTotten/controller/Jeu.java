package com.schottenTotten.controller;

import com.schottenTotten.model.Muraille;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Variante;
import com.schottenTotten.ai.IA;
import com.schottenTotten.tactique.CarteRenfort;
import com.schottenTotten.tactique.CarteBlocage;
import com.schottenTotten.stats.ScoreManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jeu {
    private Joueur joueur1;
    private Joueur joueur2;
    private List<Muraille> murailles;
    private List<Carte> deck;
    private IA ia;
    private ScoreManager scoreManager;
    private Variante variante;

    public Jeu(String nomJoueur1, boolean isIA1, String nomJoueur2, boolean isIA2, Variante variante) {
        this.joueur1 = new Joueur(nomJoueur1, isIA1);
        this.joueur2 = new Joueur(nomJoueur2, isIA2);
        this.murailles = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.ia = new IA();
        this.scoreManager = new ScoreManager();
        this.variante = variante;

        // Initialisation des murailles et des cartes
        initialiserMurailles();
        initialiserCartes();
    }

    private void initialiserMurailles() {
        // Ajoute des Murailles avec différents types de formation
        murailles.add(new Muraille("Suite couleur"));
        murailles.add(new Muraille("Somme"));
        murailles.add(new Muraille("Même valeur"));
        murailles.add(new Muraille("Suite couleur"));
        murailles.add(new Muraille("Somme"));
        murailles.add(new Muraille("Même valeur"));
        murailles.add(new Muraille("Suite couleur"));
        murailles.add(new Muraille("Somme"));
        murailles.add(new Muraille("Même valeur"));
    }

    private void initialiserCartes() {
        // Création des cartes normales
        String[] couleurs = {"Rouge", "Bleu", "Vert"};
        for (String couleur : couleurs) {
            for (int valeur = 1; valeur <= 9; valeur++) {
                deck.add(new Carte(couleur, valeur));
            }
        }

        // Ajout des cartes spéciales pour la variante Tactique
        if (variante == Variante.TACTIQUE) {
            deck.add(new CarteRenfort("Rouge", 5, 2));
            deck.add(new CarteRenfort("Bleu", 3, 3));
            deck.add(new CarteBlocage());
            deck.add(new CarteBlocage());
        }

        // Mélange des cartes
        Collections.shuffle(deck);

        // Distribution des cartes aux joueurs
        int nombreCartesParJoueur = 9;
        for (int i = 0; i < nombreCartesParJoueur; i++) {
            joueur1.ajouterCarte(deck.remove(0));
            joueur2.ajouterCarte(deck.remove(0));
        }
    }

    public void afficherEtat() {
        System.out.println("État actuel des murailles :");
        for (int i = 0; i < murailles.size(); i++) {
            Muraille muraille = murailles.get(i);
            System.out.println("Muraille " + (i + 1) + " (" + muraille.getTypeFormation() + ") :");
            System.out.println("  Joueur 1 : " + muraille.getCartesJoueur1());
            System.out.println("  Joueur 2 : " + muraille.getCartesJoueur2());
            if (muraille.isEndommagee()) {
                System.out.println("  --> Cette muraille est endommagée !");
            }
        }
    }

    public Joueur getJoueur(int joueurNum) {
        return joueurNum == 1 ? joueur1 : joueur2;
    }

    public void jouerTour(int numeroMuraille, int joueur, Carte carte) {
        Muraille muraille = murailles.get(numeroMuraille);
        if (muraille.ajouterCarte(joueur, carte)) {
            System.out.println("Carte jouée sur la Muraille " + (numeroMuraille + 1));
        } else {
            System.out.println("Impossible de jouer sur cette Muraille (limite atteinte ou règle non respectée).");
        }
    }

    public void jouerTourIA(int joueurNum) {
        Joueur joueur = getJoueur(joueurNum);
        Carte carteChoisie = ia.choisirCarteStrategique(joueur);

        if (carteChoisie != null) {
            int murailleChoisie = ia.choisirMurailleStrategique(murailles, joueurNum);
            jouerTour(murailleChoisie, joueurNum, carteChoisie);
            joueur.getMain().remove(carteChoisie); // Retire la carte après l'avoir jouée
            System.out.println(joueur.getNom() + " (IA) a joué " + carteChoisie + " sur la Muraille " + (murailleChoisie + 1));
        } else {
            System.out.println(joueur.getNom() + " (IA) n'a plus de cartes à jouer.");
        }
    }

    public boolean verifierVictoire() {
        int muraillesRevendiqueesJoueur1 = 0;
        int muraillesRevendiqueesJoueur2 = 0;

        for (Muraille muraille : murailles) {
            int gagnant = muraille.revendiquerBorne();
            if (gagnant == 1) {
                muraillesRevendiqueesJoueur1++;
            } else if (gagnant == 2) {
                muraillesRevendiqueesJoueur2++;
            }
        }

        if (muraillesRevendiqueesJoueur1 >= 5) {
            System.out.println(joueur1.getNom() + " remporte la partie !");
            scoreManager.ajouterVictoire(joueur1.getNom());
            scoreManager.afficherScores();
            return true;
        } else if (muraillesRevendiqueesJoueur2 >= 5) {
            System.out.println(joueur2.getNom() + " remporte la partie !");
            scoreManager.ajouterVictoire(joueur2.getNom());
            scoreManager.afficherScores();
            return true;
        }

        // Condition de fin : Les deux joueurs n'ont plus de cartes
        if (joueur1.getMain().isEmpty() && joueur2.getMain().isEmpty()) {
            System.out.println("Les deux joueurs n'ont plus de cartes. Fin de la partie !");
            
            // Ajout du joueur ayant revendiqué le plus de murailles
            if (muraillesRevendiqueesJoueur1 > muraillesRevendiqueesJoueur2) {
                scoreManager.ajouterVictoire(joueur1.getNom());
                System.out.println(joueur1.getNom() + " remporte la partie par domination des murailles !");
            } else if (muraillesRevendiqueesJoueur2 > muraillesRevendiqueesJoueur1) {
                scoreManager.ajouterVictoire(joueur2.getNom());
                System.out.println(joueur2.getNom() + " remporte la partie par domination des murailles !");
            } else {
                System.out.println("Égalité parfaite ! Aucun joueur ne remporte cette partie.");
            }

            scoreManager.afficherScores();
            return true;
        }

        return false;
    }

    public Variante getVariante() {
        return variante;
    }
}

