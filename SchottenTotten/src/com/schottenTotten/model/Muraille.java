package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.List;

public class Muraille {
    private Joueur holder; // Désigne le joueur qui a capturé la muraille
    private Joueur joueur1;
    private Joueur joueur2;
    private List<Carte> cartesJoueur1;
    private List<Carte> cartesJoueur2;

    public Muraille(Joueur joueur1, Joueur joueur2) {
        this.joueur1 = joueur1;
        this.joueur2 = joueur2;
        this.cartesJoueur1 = new ArrayList<>();
        this.cartesJoueur2 = new ArrayList<>();
    }

    public Joueur getHolder() {
        return holder;
    }

    public List<Carte> getCartesJoueur1() {
        return cartesJoueur1;
    }

    public List<Carte> getCartesJoueur2() {
        return cartesJoueur2;
    }

    public boolean ajouterCarte(Joueur joueur, Carte carte) {
        if (joueur.equals(joueur1) && cartesJoueur1.size() < 3) {
            cartesJoueur1.add(carte);
            return true;
        } else if (joueur.equals(joueur2) && cartesJoueur2.size() < 3) {
            cartesJoueur2.add(carte);
            return true;
        }
        return false; // La carte n'a pas pu être ajoutée (limite atteinte ou règle non respectée)
    }

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

    private boolean verifierSuite(List<Carte> cartes) {
        if (cartes.size() != 3) return false;

        List<Integer> valeurs = cartes.stream()
                .map(Carte::getValeur)
                .sorted()
                .toList();

        return valeurs.get(1) == valeurs.get(0) + 1 && valeurs.get(2) == valeurs.get(1) + 1;
    }

    private int calculerSomme(List<Carte> cartes) {
        if (cartes.size() != 3) throw new IllegalArgumentException("La liste doit contenir exactement 3 cartes.");
        return cartes.stream()
                .mapToInt(Carte::getValeur)
                .sum();
    }

    private int determinerForceCombinaison(List<Carte> cartes) {
        if (verifierSuiteCouleur(cartes)) return 5;
        if (verifierBrelan(cartes)) return 4;
        if (verifierCouleur(cartes)) return 3;
        if (verifierSuite(cartes)) return 2;
        return 1; // Si aucune combinaison forte, utiliser la somme
    }

    public Joueur revendiquerBorne() {
        if (cartesJoueur1.size() == 3 && cartesJoueur2.size() == 3) {
            int forceJoueur1 = determinerForceCombinaison(cartesJoueur1);
            int forceJoueur2 = determinerForceCombinaison(cartesJoueur2);

            if (forceJoueur1 > forceJoueur2) {
                holder = joueur1;
                return joueur1;
            } else if (forceJoueur2 > forceJoueur1) {
                holder = joueur2;
                return joueur2;
            } else {
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
        return null; // Pas de revendication possible
    }

    public Joueur getJoueurRevendiquant() {
        return holder; // Retourne le joueur qui a revendiqué la borne, ou null si aucune revendication
    }
    public boolean estComplete(List<Carte> cartes) {
        if (cartes.size() != 3) return false;
        else return true;
    }
    
    
    public boolean estCompletePourJoueur(Joueur joueur) {
        if (joueur == joueur1) {
            return cartesJoueur1.size() >= 3;
        } else if (joueur == joueur2) {
            return cartesJoueur2.size() >= 3;
        }
        return false; // Should not happen if the input is correct
    }

    
}
    
    
    
    
    
    
    
    
    
    
    
    
    
