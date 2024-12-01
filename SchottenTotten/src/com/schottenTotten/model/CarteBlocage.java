package com.schottenTotten.model;

public class CarteBlocage extends Carte {
    private boolean active;

    public CarteBlocage() {
        super("Blocage", 0); // Pas de couleur spécifique et valeur nulle
        this.active = true;
    }

    public void desactiver() {
        this.active = false;
    }

    public boolean estActive() {
        return active;
    }

    @Override
    public String toString() {
        return "Carte de Blocage (Active: " + active + ")";
    }
}
