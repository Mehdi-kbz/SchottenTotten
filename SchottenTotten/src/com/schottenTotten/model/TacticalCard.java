package com.schottenTotten.model;

public class TacticalCard extends Carte {
	
	// Variables
    public enum TacticalType {
        JOKER, 
        ESPION, 
        PORTE_BOUCLIER, 
        COLIN_MAILLARD, 
        COMBAT_DE_BOUE, 
        CHASSEUR_DE_TETE, 
        STRATEGE, 
        BANSHEE, 
        TRAITRE
    }
    private TacticalType type;
    private String couleur; // Utilisé pour ESPION ou autres cartes nécessitant une couleur spécifique
    private int valeur;     // Attribut pour les valeurs dynamiques

    // Constructeur pour les TacticalCards
    public TacticalCard(TacticalType type) {
        super(null, 0); // Pas de couleur ou de valeur par défaut pour une carte tactique
        this.type = type;
    }

    // Getter pour le type de carte tactique
    public TacticalType getType() {
        return type;
    }

    // Getter et Setter pour la couleur (utile pour ESPION ou autres)
    @Override
    public String getCouleur() {
        return couleur;
    }
    
    @Override
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }
    
    @Override
    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }
    
    @Override
    public String toString() {
        // Affiche le type et la couleur si défini
        if (couleur != null && (type == TacticalType.ESPION || type == TacticalType.JOKER)) {
            return type.name() + " (" + couleur + ")";
        }
        return type.name(); // Par défaut, affiche uniquement le type
    }



    // Méthodes spécifiques pour chaque type de carte tactique
    public void applyEffect(Muraille muraille, Joueur joueur, Joueur adversaire) {
        switch (type) {
            case JOKER:
                applyJokerEffect(muraille, joueur);
                break;
            case ESPION:
                applyEspionEffect(muraille, joueur);
                break;
            case PORTE_BOUCLIER:
                applyPorteBouclierEffect(muraille, joueur);
                break;
            case COLIN_MAILLARD:
                applyColinMaillardEffect(muraille);
                break;
            case COMBAT_DE_BOUE:
            	activerCombatDeBoue(muraille);
            	applyCombatDeBoueEffect(muraille);
                break;
            case CHASSEUR_DE_TETE:
                applyChasseurDeTeteEffect(joueur);
                break;
            case STRATEGE:
                applyStrategieEffect(muraille, joueur);
                break;
            case BANSHEE:
                applyBansheeEffect(muraille, adversaire);
                break;
            case TRAITRE:
                applyTraitreEffect(muraille, adversaire, joueur);
                break;
        }
    }

    public void activerCombatDeBoue(Muraille muraille) {
        muraille.activerCombatDeBoue();
    }

    private void applyJokerEffect(Muraille muraille, Joueur joueur) {
        System.out.println(joueur.getNom() + " utilise le Joker sur la Muraille " + muraille);
    }

    private void applyEspionEffect(Muraille muraille, Joueur joueur) {
        System.out.println(joueur.getNom() + " utilise Espion sur la Muraille " + muraille);
    }

    private void applyPorteBouclierEffect(Muraille muraille, Joueur joueur) {
        System.out.println(joueur.getNom() + " utilise Porte-Bouclier sur la Muraille " + muraille);
    }

    private void applyColinMaillardEffect(Muraille muraille) {
        System.out.println("Colin-Maillard active son effet sur la Muraille " + muraille);
    }

    private void applyCombatDeBoueEffect(Muraille muraille) {
       System.out.println("Combat de Boue active son effet sur la Muraille " + muraille);
    }

    private void applyChasseurDeTeteEffect(Joueur joueur) {
        System.out.println(joueur.getNom() + " active Chasseur de Tête");
    }

    private void applyStrategieEffect(Muraille muraille, Joueur joueur) {
        System.out.println(joueur.getNom() + " active Stratège sur la Muraille " + muraille);
    }

    private void applyBansheeEffect(Muraille muraille, Joueur adversaire) {
        System.out.println("Banshee retire une carte du côté de " + adversaire.getNom() + " sur la Muraille " + muraille);
    }

    private void applyTraitreEffect(Muraille muraille, Joueur adversaire, Joueur joueur) {
        System.out.println("Traître déplace une carte du côté de " + adversaire.getNom() + " vers " + joueur.getNom());
    }
}
