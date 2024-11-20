package com.schottenTotten.stats;

import java.util.HashMap;
import java.util.Map;

public class ScoreManager {
    private Map<String, Integer> scores;

    public ScoreManager() {
        this.scores = new HashMap<>();
    }

    /**
     * Ajoute une victoire au score du joueur spécifié.
     *
     * @param nomJoueur Le nom du joueur.
     */
    public void ajouterVictoire(String nomJoueur) {
        scores.put(nomJoueur, scores.getOrDefault(nomJoueur, 0) + 1);
    }

    /**
     * Affiche les scores de tous les joueurs.
     */
    public void afficherScores() {
        System.out.println("\nScores des joueurs :");
        if (scores.isEmpty()) {
            System.out.println("Aucun score enregistré.");
        } else {
            scores.forEach((nom, score) -> System.out.println(nom + " : " + score + " victoire(s)"));
        }
    }

    /**
     * Réinitialise les scores de tous les joueurs.
     */
    public void reinitialiserScores() {
        scores.clear();
        System.out.println("Les scores ont été réinitialisés !");
    }

    /**
     * Retourne le score d'un joueur spécifique.
     *
     * @param nomJoueur Le nom du joueur.
     * @return Le score du joueur ou 0 s'il n'a pas encore de victoires.
     */
    public int getScore(String nomJoueur) {
        return scores.getOrDefault(nomJoueur, 0);
    }
}
