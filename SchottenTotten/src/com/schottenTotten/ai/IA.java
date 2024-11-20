package com.schottenTotten.ai;

import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Muraille;

import java.util.List;
import java.util.Random;

public class IA {
    private Random random = new Random();

    // Méthode pour choisir une carte à jouer de manière stratégique
    public Carte choisirCarteStrategique(Joueur joueur) {
        List<Carte> main = joueur.getMain();
        if (main.isEmpty()) return null;

        // Diversifie les cartes jouées pour éviter la monotonie
        return main.get(random.nextInt(main.size()));
    }


    // Méthode pour choisir une muraille où jouer
    public int choisirMurailleStrategique(List<Muraille> murailles, int joueurNum) {
        // Cherche une muraille où l'IA peut encore jouer
        for (int i = 0; i < murailles.size(); i++) {
            Muraille muraille = murailles.get(i);
            if ((joueurNum == 1 && muraille.getCartesJoueur1().size() < 3) ||
                (joueurNum == 2 && muraille.getCartesJoueur2().size() < 3)) {
                return i; // Retourne l'index de la première muraille valide
            }
        }

        // Si toutes les murailles sont pleines, choisit une muraille aléatoire (cas rare)
        return random.nextInt(murailles.size());
    }
}


