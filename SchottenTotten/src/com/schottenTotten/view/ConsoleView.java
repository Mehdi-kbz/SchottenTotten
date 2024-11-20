package com.schottenTotten.view;

import com.schottenTotten.controller.Jeu;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Variante;

import java.util.Scanner;

public class ConsoleView {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Choix de la variante
        System.out.println("Bienvenue dans Schotten Totten !");
        System.out.println("Choisissez une variante de jeu : ");
        System.out.println("1 : Standard");
        System.out.println("2 : Tactique");

        Variante variante = null;
        while (variante == null) {
            try {
                System.out.print("Votre choix : ");
                int choixVariante = scanner.nextInt();
                variante = (choixVariante == 2) ? Variante.TACTIQUE : Variante.STANDARD;
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez entrer 1 pour Standard ou 2 pour Tactique.");
                scanner.nextLine(); // Vide le scanner
            }
        }

        scanner.nextLine(); // Consomme la fin de ligne

        // Informations sur les joueurs
        System.out.println("Entrez le nom du Joueur 1 : ");
        String nomJoueur1 = scanner.nextLine();

        System.out.println("Le Joueur 1 est-il une IA ? (true/false) : ");
        boolean isIA1 = scanner.nextBoolean();
        scanner.nextLine();

        System.out.println("Entrez le nom du Joueur 2 : ");
        String nomJoueur2 = scanner.nextLine();

        System.out.println("Le Joueur 2 est-il une IA ? (true/false) : ");
        boolean isIA2 = scanner.nextBoolean();

        // Initialisation du jeu
        Jeu jeu = new Jeu(nomJoueur1, isIA1, nomJoueur2, isIA2, variante);
        System.out.println("Le jeu commence avec la variante " + jeu.getVariante() + " !");
        jeu.afficherEtat();

        boolean partieEnCours = true;
        int compteurTours = 0;

        while (partieEnCours) {
            compteurTours++;
            System.out.println("\n=== Tour " + compteurTours + " ===");

            for (int j = 1; j <= 2; j++) {
                Joueur joueur = jeu.getJoueur(j);

                if (joueur.isIA()) {
                    // Tour de l'IA
                    jeu.jouerTourIA(j);
                } else {
                    // Tour d'un joueur humain
                    boolean carteJouee = false;
                    while (!carteJouee) {
                        try {
                            System.out.println(joueur.getNom() + ", choisissez une carte à jouer :");

                            for (int i = 0; i < joueur.getMain().size(); i++) {
                                System.out.println((i + 1) + ": " + joueur.getMain().get(i));
                            }

                            System.out.print("Numéro de la carte : ");
                            int numCarte = scanner.nextInt() - 1;
                            Carte carteChoisie = joueur.getMain().get(numCarte);

                            System.out.print("Choisissez la muraille (1-9) : ");
                            int numeroMuraille = scanner.nextInt() - 1;

                            jeu.jouerTour(numeroMuraille, j, carteChoisie);
                            joueur.getMain().remove(numCarte); // Retire la carte après avoir joué
                            carteJouee = true;
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez réessayer.");
                            scanner.nextLine(); // Vide le scanner
                        }
                    }
                }
            }

            jeu.afficherEtat();

            // Vérifie les conditions de victoire ou d'arrêt
            if (jeu.verifierVictoire()) {
                partieEnCours = false;
            }

            // Limite les tours pour éviter une boucle infinie
            if (compteurTours > 1000) {
                System.out.println("Nombre maximum de tours atteint. Fin de la partie !");
                partieEnCours = false;
            }
        }

        // Fin de la partie
        System.out.println("La partie est terminée !");
        System.out.print("Voulez-vous rejouer ? (yes/no) : ");
        scanner.nextLine(); // Vide le scanner
        String reponse = scanner.nextLine().toLowerCase();

        if (reponse.equals("yes")) {
            main(new String[0]); // Redémarre la partie
        } else {
            System.out.println("Merci d'avoir joué ! À la prochaine.");
        }

        scanner.close();
    }
}

