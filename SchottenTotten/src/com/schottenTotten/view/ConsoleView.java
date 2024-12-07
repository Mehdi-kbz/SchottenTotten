package com.schottenTotten.view;

import com.schottenTotten.controller.Jeu;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.TacticalCard;
import com.schottenTotten.model.Variante;
import java.util.Scanner;

public class ConsoleView {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Choix de la variante
        System.out.println("Bienvenue dans Schotten Totten !");
        Variante variante = null;

        while (variante == null) {
            try {
                System.out.println("Choisissez une variante de jeu : ");
                System.out.println("1 : Standard");
                System.out.println("2 : Tactique");
                System.out.println("3 : Experts");
                System.out.print("Votre choix : ");
                int choixVariante = scanner.nextInt();
                if (choixVariante == 1) {
                    variante = Variante.STANDARD;
                } else if (choixVariante == 2) {
                    variante = Variante.TACTIQUE;
                }else if (choixVariante == 3) {
                    variante = Variante.TACTIQUE;// Débuggage temporaire ( Variante.EXPERTS normalement )

            	}else {
                    System.out.println("Choix invalide. Veuillez entrer 1 ou 2.");
                }
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
            }
        }

        scanner.nextLine();

        // Informations sur les joueurs
        System.out.print("Entrez le nom du Joueur 1 : ");
        String nomJoueur1 = scanner.nextLine();

        boolean isIA1 = false;
        while (true) {
            try {
                System.out.print("Le Joueur 1 est-il une IA ? (true/false) : ");
                isIA1 = scanner.nextBoolean();
                break;
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez entrer true ou false.");
                scanner.nextLine();
            }
        }
        scanner.nextLine();

        System.out.print("Entrez le nom du Joueur 2 : ");
        String nomJoueur2 = scanner.nextLine();

        boolean isIA2 = false;
        while (true) {
            try {
                System.out.print("Le Joueur 2 est-il une IA ? (true/false) : ");
                isIA2 = scanner.nextBoolean();
                break;
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez entrer true ou false.");
                scanner.nextLine();
            }
        }

        // Initialisation du jeu
        Jeu jeu = new Jeu(nomJoueur1, isIA1, nomJoueur2, isIA2, variante);
        System.out.println("Le jeu commence avec la variante " + jeu.getVariante() + " !");
        jeu.afficherEtat(nomJoueur1, nomJoueur2);

        boolean partieEnCours = true;
        int compteurTours = 0;

        while (partieEnCours) {
            compteurTours++;
            System.out.println("\n=== Tour " + compteurTours + " ===");

            for (int j = 1; j <= 2; j++) {
                Joueur joueur = jeu.getJoueur(j);

                if (joueur.isIA()) {
                    jeu.jouerTourIA(j);
                    System.out.println("L'IA a terminé son tour.");
                } else {
                    boolean carteJouee = false;
                    while (!carteJouee) {
                        try {
                            // Afficher l'état des decks et de la pile de défausse
                            System.out.println("Nombre de cartes restantes dans le deck : " + jeu.getTailleDeck());
                            System.out.println("Cartes restantes dans le deck : " + jeu.getDeck());
                            System.out.println("Nombre de cartes restantes dans le Tactical deck : " + jeu.getTailleTacticalDeck());
                            System.out.print("Cartes restantes dans le Tactical deck : ");
                            jeu.getTacticalDeck().forEach(carte -> {
                                if (carte instanceof TacticalCard) {
                                    System.out.print(((TacticalCard) carte).getType() + " ");
                                } else {
                                    System.out.print("Carte inconnue ");
                                }
                            });
                            System.out.println("\nPile de défausse : ");
                            jeu.afficherDiscardPile();

                            // Choix de la carte
                            System.out.println(joueur.getNom() + ", choisissez une carte à jouer :");
                            for (int i = 0; i < joueur.getMain().size(); i++) {
                                System.out.println((i + 1) + ": " + joueur.getMain().get(i));
                            }

                            System.out.print("Numéro de la carte : ");
                            int numCarte = scanner.nextInt() - 1;

                            if (numCarte < 0 || numCarte >= joueur.getMain().size()) {
                                System.out.println("Numéro invalide. Veuillez réessayer.");
                                continue;
                            }

                            Carte carteChoisie = joueur.getMain().get(numCarte);
                            System.out.println("Carte choisie : " + carteChoisie);

                            // Choix de la muraille
                            System.out.print("Choisissez la muraille (1-9) : ");
                            int numeroMuraille = scanner.nextInt() - 1;

                            if (numeroMuraille < 0 || numeroMuraille >= 9) {
                                System.out.println("Numéro de muraille invalide. Veuillez réessayer.");
                                continue;
                            }

                            // Jouer la carte
                            jeu.jouerTour(numeroMuraille, jeu.getJoueur(j), carteChoisie, scanner);
                            joueur.getMain().remove(numCarte);

                            // Pioche
                            Carte nouvelleCarte = jeu.piocherCarte(variante, scanner);
                            if (nouvelleCarte != null) {
                                joueur.ajouterCarte(nouvelleCarte);
                                System.out.println("Vous avez pioché : " + nouvelleCarte);
                            } else {
                                System.out.println("Aucune carte n'a été piochée.");
                            }

                            carteJouee = true;

                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez réessayer.");
                            scanner.nextLine();
                        }
                    }
                }
            }

            jeu.afficherEtat(nomJoueur1, nomJoueur2);

            // Vérifier victoire
            if (jeu.verifierVictoire()) {
                partieEnCours = false;
            }

            // Prévention boucle infinie
            if (compteurTours > 120) {
                System.out.println("Nombre maximum de tours atteint. Fin de la partie !");
                partieEnCours = false;
            }
        }

        // Fin du jeu
        System.out.println("La partie est terminée !");
        while (true) {
            System.out.print("Voulez-vous rejouer ? (yes/no) : ");
            String reponse = scanner.next().toLowerCase();
            if (reponse.equals("yes")) {
                main(new String[0]);
                break;
            } else if (reponse.equals("no")) {
                System.out.println("Merci d'avoir joué ! À la prochaine.");
                break;
            } else {
                System.out.println("Réponse invalide. Veuillez entrer yes ou no.");
            }
        }

        scanner.close();
    }
}
