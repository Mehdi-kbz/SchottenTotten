package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Muraille {
	
	// Variables
    private Joueur holder; // Désigne le joueur qui a capturé la muraille
    private Joueur joueur1;
    private Joueur joueur2;
    private List<Carte> cartesJoueur1;
    private List<Carte> cartesJoueur2;
    private boolean combatDeBoueActif;	// Indique si Combat-de-Boue est actif sur cette muraille
    private boolean colinMaillardActif; // Indique si Colin-Maillard est actif sur cette muraille

    // Constructeur
    public Muraille(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.cartesJoueur1 = new ArrayList<>();
        this.cartesJoueur2 = new ArrayList<>();
        this.colinMaillardActif = false; // Par défaut, l'effet Colin-Maillard est désactivé

    }

    // Getters
    public Joueur getHolder() {
        return holder;
    }

    public List<Carte> getCartesJoueur1() {
        return cartesJoueur1;
    }

    public List<Carte> getCartesJoueur2() {
        return cartesJoueur2;
    }

    
    
    
    
    // Méthodes pour gérer la carte combat de Boue
    public boolean isCombatDeBoueActif() {
        return combatDeBoueActif;
    }
    
    // Méthode pour activer la cartte Combat-De-Boue
    public void activerCombatDeBoue() {
        this.combatDeBoueActif = true;
        System.out.println("Combat de Boue activé sur cette muraille !");
    }

    // Méthode pour savoir si la carte Colin-Maillard est active
    public boolean isColinMaillardActif() {
        return colinMaillardActif;
    }

    // Méthode pour activer la cartte Colin-Maillard
    public void activerColinMaillard() {
        this.colinMaillardActif = true;
    }
    
    // Méthode pour ajouter une carte à un côté d'un joueur spécifié sur la muraille (borne)
    public boolean ajouterCarte(Joueur joueur, Carte carte) {
        int limite = combatDeBoueActif ? 4 : 3; // Limite de cartes selon l'état de Combat de Boue

        if (joueur.equals(joueur1) && cartesJoueur1.size() < limite) {
            cartesJoueur1.add(carte);
            return true;
        } else if (joueur.equals(joueur2) && cartesJoueur2.size() < limite) {
            cartesJoueur2.add(carte);
            return true;
        }
        return false;
    }

    
    ///////// Méthodes pour la logique de revendiquation des bornes (Murailles) /////////
   /////////             classées par ordre décroissant de force               /////////

    
    // Méthode pour vérifier si les cartes jouées constituent une suite de même couleur ou pas
    private boolean verifierSuiteCouleur(List<Carte> cartes) {
        if (cartes.size() != 3) return false;

        String couleur = cartes.get(0).getCouleur();
        for (Carte carte : cartes) {
            if (!carte.getCouleur().equals(couleur)) {
                return false;
            }
        }

        List<Integer> valeurs = cartes.stream()
                .map(Carte::getValeur)
                .sorted()
                .toList();

        return valeurs.get(1) == valeurs.get(0) + 1 && valeurs.get(2) == valeurs.get(1) + 1;
    }

    // Methode pour vérifier si les cartes jouées sont de même Valeur
    private boolean verifierBrelan(List<Carte> cartes) {
        if (cartes.size() != 3) return false;

        int valeur = cartes.get(0).getValeur();
        for (Carte carte : cartes) {
            if (carte.getValeur() != valeur) {
                return false;
            }
        }
        return true;
    }

    // Methode pour vérifier si les cartes jouées sont de même couleur
    private boolean verifierCouleur(List<Carte> cartes) {
        if (cartes.size() != 3) return false;

        String couleur = cartes.get(0).getCouleur();
        for (Carte carte : cartes) {
            if (!carte.getCouleur().equals(couleur)) {
                return false;
            }
        }
        return true;
    }

    // Méthode pour vérifier si les cartes jouées constituent une suite ou pas
    private boolean verifierSuite(List<Carte> cartes) {
        if (cartes.size() != 3) return false;

        List<Integer> valeurs = cartes.stream()
                .map(Carte::getValeur)
                .sorted()
                .toList();

        return valeurs.get(1) == valeurs.get(0) + 1 && valeurs.get(2) == valeurs.get(1) + 1;
    }

    // Méthode pour calculer la somme des cartes
    private int calculerSomme(List<Carte> cartes) {
        return cartes.stream()
                .mapToInt(carte -> {
                    if (carte instanceof TacticalCard) {
                        TacticalCard tacticalCard = (TacticalCard) carte;
                        if (tacticalCard.getType() == TacticalCard.TacticalType.ESPION) {
                            return 7;
                        }
                    }
                    return carte.getValeur();
                })
                .sum();
    }

    // Méthode pour déterminer la combinaison la plus forte
    private int determinerForceCombinaison(List<Carte> cartes) {
        if (verifierSuiteCouleur(cartes)) return 5;
        if (verifierBrelan(cartes)) return 4;
        if (verifierCouleur(cartes)) return 3;
        if (verifierSuite(cartes)) return 2;
        return 1; // Si aucune combinaison forte, utiliser la somme
    }

    // Méthode pour demander la couleur du Joker
    private String demanderCouleurJoker(Joueur joueur) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println(joueur.getNom() + ", choisissez une couleur pour votre Joker :");
                System.out.println("Options : Rouge, Bleu, Vert, Violet, Jaune, Orange");
                String couleur = scanner.nextLine().trim();
                if (List.of("Rouge", "Bleu", "Vert", "Violet", "Jaune", "Orange").contains(couleur)) {
                    return couleur;
                } else {
                    System.out.println("Couleur invalide. Veuillez réessayer.");
                }
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez réessayer.");
                scanner.close();
            }
        }
    }

    // Méthode pour demander la valeur du Joker
    private int demanderValeurJoker(Joueur joueur) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println(joueur.getNom() + ", choisissez une valeur pour votre Joker (1 à 9) :");
                int valeur = scanner.nextInt();
                scanner.nextLine();
                if (valeur >= 1 && valeur <= 9) {
                    return valeur;
                } else {
                    System.out.println("Valeur invalide. Veuillez entrer un nombre entre 1 et 9.");
                }
            } catch (Exception e) {
                System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                scanner.nextLine();
                scanner.close();
            }
        }
    }
    
    // Méthode pour revendiquer une borne
    public Joueur revendiquerBorne() {
        // Détermine la limite en fonction de l'état de Combat de Boue
        int limite = isCombatDeBoueActif() ? 4 : 3;

        // Vérifie si les deux joueurs ont atteint la limite
        if (cartesJoueur1.size() == limite && cartesJoueur2.size() == limite) {
            // Gérer les Jokers pour le joueur 1
            for (Carte carte : cartesJoueur1) {
                if (carte instanceof TacticalCard tacticalCard && tacticalCard.getType() == TacticalCard.TacticalType.JOKER) {
                    if (tacticalCard.getValeur() == 0 || tacticalCard.getCouleur() == null) {
                        tacticalCard.setCouleur(demanderCouleurJoker(joueur1));
                        tacticalCard.setValeur(demanderValeurJoker(joueur1));
                    }
                }
            }

            // Gérer les Jokers pour le joueur 2
            for (Carte carte : cartesJoueur2) {
                if (carte instanceof TacticalCard tacticalCard && tacticalCard.getType() == TacticalCard.TacticalType.JOKER) {
                    if (tacticalCard.getValeur() == 0 || tacticalCard.getCouleur() == null) {
                        tacticalCard.setCouleur(demanderCouleurJoker(joueur2));
                        tacticalCard.setValeur(demanderValeurJoker(joueur2));
                    }
                }
            }

            // Calculer la force des combinaisons
            int forceJoueur1 = determinerForceCombinaison(cartesJoueur1);
            int forceJoueur2 = determinerForceCombinaison(cartesJoueur2);

            if (forceJoueur1 > forceJoueur2) {
                holder = joueur1;
                return joueur1;
            } else if (forceJoueur2 > forceJoueur1) {
                holder = joueur2;
                return joueur2;
            } else {
                // En cas d'égalité des forces, compare les sommes
                int sommeJoueur1 = calculerSomme(cartesJoueur1);
                int sommeJoueur2 = calculerSomme(cartesJoueur2);

                if (sommeJoueur1 > sommeJoueur2) {
                    holder = joueur1;
                    return joueur1;
                } else if (sommeJoueur2 > sommeJoueur1) {
                    holder = joueur2;
                    return joueur2;
                }
            }
        }

        // Pas de revendication possible si les deux joueurs n'ont pas atteint la limite
        return null;
    }

    // Méthode pour donner le joueur revendiquant
    public Joueur getJoueurRevendiquant() {
        return holder; // Retourne le joueur qui a revendiqué la borne, ou null si aucune revendication
    }
    
    // Méthode pour savoir si une borne est complète
    public boolean estComplete(List<Carte> cartes) {
        if (cartes.size() != 3) return false;
        else return true;
    }
    
    // Méthode pour savoir si une borne est complète pour un joueur
    public boolean estCompletePourJoueur(Joueur joueur) {
        if (joueur == joueur1) {
            return cartesJoueur1.size() >= 3;
        } else if (joueur == joueur2) {
            return cartesJoueur2.size() >= 3;
        }
        return false; // Should not happen if the input is correct
    }

    // Méthode pour evaluer la somme des cartes d'un joueur
    public int evaluerMuraille(Joueur joueur) {
        return calculerSomme(joueur.equals(joueur1) ? cartesJoueur1 : cartesJoueur2);
    }
 
}
    
    
    
    
    
    
    
    
    
    
    
    
    
