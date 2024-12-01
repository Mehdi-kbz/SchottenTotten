package com.schottenTotten.ai;

import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Muraille;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IA {
    private Random random = new Random();

    // Méthode pour choisir une carte à jouer de manière aléatoire
    public Carte choisirCarteAleatoire(Joueur joueur) {
        List<Carte> main = joueur.getMain();
        if (main.isEmpty()) return null;
        return main.get(random.nextInt(main.size()));
    }

    // Méthode pour choisir une muraille de manière aléatoire où jouer
    public int choisirMurailleAleatoire(List<Muraille> murailles, Joueur joueur) {
        List<Integer> muraillesDisponibles = new ArrayList<>();
        for (int i = 0; i < murailles.size(); i++) {
            Muraille muraille = murailles.get(i);
            if (!muraille.estCompletePourJoueur(joueur)) {
                muraillesDisponibles.add(i);
            }
        }
        if (muraillesDisponibles.isEmpty()) return -1;
        return muraillesDisponibles.get(random.nextInt(muraillesDisponibles.size()));
    }

    // Méthode pour jouer une carte sur une muraille
    public void jouerCarte(Joueur joueur, List<Muraille> murailles, List<Carte> deck) {
        Carte carteChoisie = choisirCarteAleatoire(joueur);
        if (carteChoisie == null) {
            System.out.println(joueur.getNom() + " (IA) n'a plus de cartes à jouer.");
            return;
        }

        int murailleChoisie = choisirMurailleAleatoire(murailles, joueur);
        if (murailleChoisie == -1) {
            System.out.println("Aucune muraille disponible pour jouer. L'IA passe son tour.");
            return;
        }

        Muraille muraille = murailles.get(murailleChoisie);

        // Ajoute la carte sur la muraille choisie
        if (muraille.ajouterCarte(joueur, carteChoisie)) {
            joueur.getMain().remove(carteChoisie); // Retire la carte de la main après l'avoir jouée
            System.out.println(joueur.getNom() + " (IA) a joué " + carteChoisie + " sur la Muraille " + (murailleChoisie + 1));

            // Pioche une nouvelle carte si le deck n'est pas vide
            if (!deck.isEmpty()) {
                Carte nouvelleCarte = deck.remove(0);
                joueur.ajouterCarte(nouvelleCarte);
                System.out.println(joueur.getNom() + " (IA) a pioché une nouvelle carte : " + nouvelleCarte);
            } else {
                System.out.println("Le deck est vide. Plus de cartes à piocher.");
            }
        } else {
            System.out.println("Impossible de jouer sur la Muraille " + (murailleChoisie + 1) + ". L'IA passera au prochain tour.");
        }
    }
}
