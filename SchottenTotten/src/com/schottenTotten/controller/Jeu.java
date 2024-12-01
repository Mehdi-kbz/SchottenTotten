package com.schottenTotten.controller;

import com.schottenTotten.model.Muraille;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.CarteBlocage;
import com.schottenTotten.model.CarteRenfort;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Variante;
import com.schottenTotten.ai.IA;
import com.schottenTotten.stats.ScoreManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Jeu {
    private Joueur joueur1;
    private Joueur joueur2;
    private List<Muraille> murailles;
    public List<Carte> deck;
    public List<Carte> getDeck() {
        return deck;
    }
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
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
    }

    private void initialiserCartes() {
        // Création des cartes normales
        String[] couleurs = {"Rouge", "Bleu", "Vert", "Violet", "Jaune", "Orange"};
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

        // Distribution initiale des cartes aux joueurs
        int nombreCartesParJoueur = 6;
        for (int i = 0; i < nombreCartesParJoueur; i++) {
            joueur1.ajouterCarte(deck.remove(0));
            joueur2.ajouterCarte(deck.remove(0));
        }
    }
    public void afficherEtat(String nomJoueur1, String nomJoueur2) {
        System.out.println("=== État actuel des murailles ===");
        for (int i = 0; i < murailles.size(); i++) {
            Muraille muraille = murailles.get(i);
            Joueur titulaire = muraille.getJoueurRevendiquant();
            String titulaireNom = (titulaire != null) ? titulaire.getNom() : "Non revendiquée";

            System.out.println("Muraille " + (i + 1) + " (" + titulaireNom + ") :");
            System.out.println(nomJoueur1 + " : " + muraille.getCartesJoueur1());
            System.out.println(nomJoueur2 + " : " + muraille.getCartesJoueur2());
        }
    }


    public Joueur getJoueur(int joueurNum) {
        return joueurNum == 1 ? joueur1 : joueur2;
    }

    public void jouerTour(int numeroMuraille, Joueur joueur, Carte carte) {
        Muraille muraille = murailles.get(numeroMuraille);
        if (muraille.ajouterCarte(joueur, carte)) {
            System.out.println(joueur.getNom() + " a joué " + carte + " sur la Muraille " + (numeroMuraille + 1));
            
            // Vérifier si la muraille peut être revendiquée
            Joueur titulaire = muraille.revendiquerBorne();
            if (titulaire != null) {
                System.out.println("La Muraille " + (numeroMuraille + 1) + " a été revendiquée par " + titulaire.getNom() + " !");
            } else {
                System.out.println("La Muraille " + (numeroMuraille + 1) + " n'est pas encore revendiquée.");
            }
            
            
        } else {
            System.out.println("Impossible de jouer sur la Muraille " + (numeroMuraille + 1) + " (limite atteinte ou règle non respectée).");
        }
    }


    public void jouerTourIA(int joueurNum) {
        // Récupère l'objet Joueur correspondant au numéro du joueur
        Joueur joueur = getJoueur(joueurNum);
        
        // Choisir une carte aléatoire de la main du joueur
        Carte carteChoisie = ia.choisirCarteAleatoire(joueur);
        if (carteChoisie == null) {
            System.out.println(joueur.getNom() + " (IA) n'a plus de cartes à jouer.");
            return;
        }

        // Choisir une muraille disponible pour jouer
        int murailleChoisie = ia.choisirMurailleAleatoire(murailles, joueur);
        if (murailleChoisie == -1) {
            System.out.println("Aucune muraille disponible pour jouer. " + joueur.getNom() + " (IA) passe son tour.");
            return;
        }

        // Tente de jouer la carte sur la muraille choisie
        Muraille muraille = murailles.get(murailleChoisie);
        if (muraille.ajouterCarte(joueur, carteChoisie)) {
            joueur.getMain().remove(carteChoisie); // Retire la carte de la main après l'avoir jouée
            System.out.println(joueur.getNom() + " (IA) a joué " + carteChoisie + " sur la Muraille " + (murailleChoisie + 1));
            
            // Vérifier si la muraille peut être revendiquée
            Joueur titulaire = muraille.revendiquerBorne();
            if (titulaire != null) {
                System.out.println("La Muraille " + (murailleChoisie + 1) + " a été revendiquée par " + titulaire.getNom() + " !");
            } else {
                System.out.println("La Muraille " + (murailleChoisie + 1) + " n'est pas encore revendiquée.");
            }
            
            // Piocher une nouvelle carte pour l'IA si le deck n'est pas vide
            Carte nouvelleCarte = piocherCarte();
            if (nouvelleCarte != null) {
                joueur.ajouterCarte(nouvelleCarte);
                System.out.println(joueur.getNom() + " (IA) a pioché une nouvelle carte : " + nouvelleCarte);
            } else {
                System.out.println("Le deck est vide. Plus de cartes à piocher.");
            }
        } else {
            // Si jouer la carte échoue, l'IA ne joue pas ce tour
            System.out.println("Impossible de jouer sur la Muraille " + (murailleChoisie + 1) + ". " + joueur.getNom() + " (IA) passe son tour.");
        }
    }


    public boolean verifierVictoire() {
        int muraillesRevendiqueesJoueur1 = 0;
        int muraillesRevendiqueesJoueur2 = 0;

        for (Muraille muraille : murailles) {
            Joueur gagnant = muraille.getJoueurRevendiquant();
            if (gagnant == joueur1 ) {
                muraillesRevendiqueesJoueur1++;
            } else if (gagnant == joueur2) {
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
            } 
            scoreManager.afficherScores();
            return true;
        }

        return false;
    }


    public Variante getVariante() {
        return variante;
    }

    public Carte piocherCarte() {
        if (!deck.isEmpty()) {
            return deck.remove(0); // Retire et retourne la première carte du deck
        }
        return null; // Si le deck est vide, retourne null
    }
    
    public int getTailleDeck() {
        return deck.size();
    }

}
