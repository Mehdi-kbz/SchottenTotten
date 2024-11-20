package com.schottenTotten.tactique;

import com.schottenTotten.model.Carte;

public class CarteRenfort extends Carte {
    private int bonus;

    public CarteRenfort(String couleur, int valeur, int bonus) {
        super(couleur, valeur);
        this.bonus = bonus;
    }

    @Override
    public int getValeur() {
        return super.getValeur() + bonus; // Ajoute le bonus à la valeur de la carte
    }

    @Override
    public String toString() {
        return super.toString() + " (Bonus: " + bonus + ")";
    }
}
