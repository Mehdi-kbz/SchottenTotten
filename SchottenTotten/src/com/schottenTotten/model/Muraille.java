package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.List;

public class Muraille {
    private String typeFormation; // Exemple : "Suite couleur", "Somme", "Même valeur"
    private boolean estEndommagee;
    private List<Carte> cartesJoueur1;
    private List<Carte> cartesJoueur2;

    public Muraille(String typeFormation) {
        this.typeFormation = typeFormation;
        this.estEndommagee = false;
        this.cartesJoueur1 = new ArrayList<>();
        this.cartesJoueur2 = new ArrayList<>();
    }

    public String getTypeFormation() {
        return typeFormation;
    }

    public boolean isEndommagee() {
        return estEndommagee;
    }

    public void endommager() {
        this.estEndommagee = true;
    }

    public List<Carte> getCartesJoueur1() {
        return cartesJoueur1;
    }

    public List<Carte> getCartesJoueur2() {
        return cartesJoueur2;
    }

    public boolean ajouterCarte(int joueur, Carte carte) {
        if (joueur == 1 && cartesJoueur1.size() < 3) {
            cartesJoueur1.add(carte);
            return true;
        } else if (joueur == 2 && cartesJoueur2.size() < 3) {
            cartesJoueur2.add(carte);
            return true;
        }
        return false; // La carte n'a pas pu être ajoutée (limite atteinte ou règle non respectée)
    }

    public boolean verifierFormation(List<Carte> cartes) {
        switch (typeFormation) {
            case "Suite couleur":
                return verifierSuiteCouleur(cartes);
            case "Somme":
                return verifierSomme(cartes);
            case "Même valeur":
                return verifierMemeValeur(cartes);
            default:
                return false;
        }
    }

    private boolean verifierSuiteCouleur(List<Carte> cartes) {
        if (cartes.size() != 3) return false;

        // Vérifie que les cartes sont de la même couleur
        String couleur = cartes.get(0).getCouleur();
        for (Carte carte : cartes) {
            if (!carte.getCouleur().equals(couleur)) {
                return false;
            }
        }

        // Vérifie si les valeurs forment une suite
        List<Integer> valeurs = cartes.stream()
                .map(Carte::getValeur)
                .sorted()
                .toList();

        return valeurs.get(1) == valeurs.get(0) + 1 && valeurs.get(2) == valeurs.get(1) + 1;
    }

    private boolean verifierSomme(List<Carte> cartes) {
        if (cartes.size() != 3) return false;

        // Vérifie si la somme est supérieure à une valeur arbitraire (exemple : 15)
        int somme = cartes.stream().mapToInt(Carte::getValeur).sum();
        return somme > 15;
    }

    private boolean verifierMemeValeur(List<Carte> cartes) {
        if (cartes.size() != 3) return false;

        // Vérifie que toutes les cartes ont la même valeur
        int valeur = cartes.get(0).getValeur();
        for (Carte carte : cartes) {
            if (carte.getValeur() != valeur) {
                return false;
            }
        }
        return true;
    }

    public int revendiquerBorne() {
        if (cartesJoueur1.size() == 3 && cartesJoueur2.size() == 3) {
            boolean formationJoueur1Valide = verifierFormation(cartesJoueur1);
            boolean formationJoueur2Valide = verifierFormation(cartesJoueur2);

            if (formationJoueur1Valide && !formationJoueur2Valide) {
                endommager();
                return 1; // Joueur 1 revendique la muraille
            } else if (formationJoueur2Valide && !formationJoueur1Valide) {
                endommager();
                return 2; // Joueur 2 revendique la muraille
            } else if (formationJoueur1Valide && formationJoueur2Valide) {
                // Si les deux formations sont valides, compare les valeurs totales
                int sommeJoueur1 = cartesJoueur1.stream().mapToInt(Carte::getValeur).sum();
                int sommeJoueur2 = cartesJoueur2.stream().mapToInt(Carte::getValeur).sum();
                if (sommeJoueur1 > sommeJoueur2) {
                    endommager();
                    return 1;
                } else if (sommeJoueur2 > sommeJoueur1) {
                    endommager();
                    return 2;
                }
            }
        }
        return 0; // Aucun joueur ne peut revendiquer la muraille
    }

    public int getJoueurRevendiquant() {
        if (revendiquerBorne() == 1) return 1;
        if (revendiquerBorne() == 2) return 2;
        return 0;
    }
}



