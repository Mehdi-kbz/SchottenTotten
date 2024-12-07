package com.schottenTotten.stats;
import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
	// Variables
    private Map<String, Integer> scores;

    // Constructeur
    public ScoreManager() {
        this.scores = new HashMap<>();
    }

    // Méthode pour ajouter une victoire au score du joueur spécifié en argument
    public void ajouterVictoire(String nomJoueur) {
        scores.put(nomJoueur, scores.getOrDefault(nomJoueur, 0) + 1);
    }

    // Méthode pour afficher les scores de tous les joueurs.
    public void afficherScores() {
        System.out.println("\nScores des joueurs :");
        if (scores.isEmpty()) {
            System.out.println("Aucun score enregistré.");
        } else {
            scores.forEach((nom, score) -> System.out.println(nom + " : " + score + " victoire(s)"));
        }
    }

    // Méthode pour Réinitialise les scores de tous les joueurs
    public void reinitialiserScores() {
        scores.clear();
        System.out.println("Les scores ont été réinitialisés !");
    }

    // Méthode pour donner le score du joueur
    public int getScore(String nomJoueur) {
        return scores.getOrDefault(nomJoueur, 0);
    }
}
