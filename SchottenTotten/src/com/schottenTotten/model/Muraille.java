package com.schottenTotten.model;

import java.util.ArrayList;
import java.util.List;

public class Muraille {
    private String typeFormation;
    private List<Carte> cartesJoueur1;
    private List<Carte> cartesJoueur2;
    private boolean endommagee;

    public Muraille(String typeFormation) {
        this.typeFormation = typeFormation;
        this.cartesJoueur1 = new ArrayList<>();
        this.cartesJoueur2 = new ArrayList<>();
        this.endommagee = false;
    }

    public String getTypeFormation() {
        return typeFormation;
    }

    public List<Carte> getCartesJoueur1() {
        return cartesJoueur1;
    }

    public List<Carte> getCartesJoueur2() {
        return cartesJoueur2;
    }

    public boolean isEndommagee() {
        return endommagee;
    }

    public void setEndommagee(boolean endommagee) {
        this.endommagee = endommagee;
    }

    public boolean ajouterCarte(int joueur, Carte carte) {
        List<Carte> cible = joueur == 1 ? cartesJoueur1 : cartesJoueur2;
        if (cible.size() >= 3) {
            System.out.println("Limite de 3 cartes atteinte pour cette muraille.");
            return false;
        }
        cible.add(carte);
        return true;
    }

    public int revendiquerBorne() {
        if (!peutEtreRevendiquee()) return 0;

        int forceJoueur1 = evaluerForceCombinaison(cartesJoueur1);
        int forceJoueur2 = evaluerForceCombinaison(cartesJoueur2);

        return Integer.compare(forceJoueur1, forceJoueur2);
    }

    public boolean peutEtreRevendiquee() {
        if (cartesJoueur1.size() == 3 && cartesJoueur2.size() == 3) return true;
        if (cartesJoueur1.size() == 3) return adversairePeutBattre(false);
        if (cartesJoueur2.size() == 3) return adversairePeutBattre(true);
        return false;
    }

    private boolean adversairePeutBattre(boolean coteJoueur1) {
        List<Carte> cartesProprietaire = coteJoueur1 ? cartesJoueur1 : cartesJoueur2;
        List<Carte> cartesAdversaire = coteJoueur1 ? cartesJoueur2 : cartesJoueur1;

        return !combinaisonEstImbattable(cartesProprietaire, cartesAdversaire);
    }

    private boolean combinaisonEstImbattable(List<Carte> cartesProprietaire, List<Carte> cartesAdversaire) {
        return evaluerForceCombinaison(cartesProprietaire) > evaluerForceCombinaison(cartesAdversaire);
    }

    public int evaluerForceCombinaison(List<Carte> cartes) {
        if (estSuiteCouleur(cartes)) return 5;
        if (estBrelan(cartes)) return 4;
        if (estCouleur(cartes)) return 3;
        if (estSuite(cartes)) return 2;
        return 1;
    }

    private boolean estSuiteCouleur(List<Carte> cartes) {
        return cartes.stream().map(Carte::getCouleur).distinct().count() == 1 && estSuite(cartes);
    }

    private boolean estBrelan(List<Carte> cartes) {
        return cartes.stream().map(Carte::getValeur).distinct().count() == 1;
    }

    private boolean estCouleur(List<Carte> cartes) {
        return cartes.stream().map(Carte::getCouleur).distinct().count() == 1;
    }

    private boolean estSuite(List<Carte> cartes) {
        List<Integer> valeurs = cartes.stream().map(Carte::getValeur).sorted().toList();
        return valeurs.get(2) - valeurs.get(0) == 2 && valeurs.get(1) - valeurs.get(0) == 1;
    }
}


