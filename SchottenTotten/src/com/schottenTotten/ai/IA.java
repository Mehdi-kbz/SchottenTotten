package com.schottenTotten.ai;

import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Muraille;
import com.schottenTotten.model.TacticalCard;
import com.schottenTotten.model.Variante;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IA {
    private Random random = new Random();

    // Méthode pour choisir une carte dans la main
    public Carte choisirCarteStrategique(Joueur joueur, List<Muraille> murailles) {
        List<Carte> main = joueur.getMain();

        // Prioriser les cartes tactiques pour des actions stratégiques
        for (Carte carte : main) {
            if (carte instanceof TacticalCard tacticalCard) {
                if (tacticalCard.getType() == TacticalCard.TacticalType.JOKER ||
                    tacticalCard.getType() == TacticalCard.TacticalType.ESPION ||
                    tacticalCard.getType() == TacticalCard.TacticalType.STRATEGE ||
                    tacticalCard.getType() == TacticalCard.TacticalType.TRAITRE ||
                    tacticalCard.getType() == TacticalCard.TacticalType.BANSHEE) {
                    return carte;
                }
            }
        }
        // Si aucune carte tactique prioritaire, jouer une carte normale
        return choisirCarteAleatoire(joueur);
    }

    // Méthode pour jouer une carte tactique spécifique
    public void jouerCarteTactique(TacticalCard tacticalCard, Joueur joueur, List<Muraille> murailles) {
        switch (tacticalCard.getType()) {
            case JOKER -> jouerJoker(tacticalCard, joueur);
            case ESPION -> jouerEspion(tacticalCard, joueur);
            case STRATEGE -> jouerStratege(tacticalCard, joueur, murailles);
            case TRAITRE -> jouerTraitre(tacticalCard, joueur, murailles);
            case BANSHEE -> jouerBanshee(tacticalCard, joueur, murailles);
            default -> System.out.println("L'IA joue une carte tactique générique : " + tacticalCard.getType());
        }
    }

    // Méthode pour jouer une carte Joker
    private void jouerJoker(TacticalCard tacticalCard, Joueur joueur) {
        String couleur = choisirCouleur();
        int valeur = choisirValeur();
        tacticalCard.setCouleur(couleur);
        tacticalCard.setValeur(valeur);
        System.out.println(joueur.getNom() + " (IA) joue Joker avec la couleur " + couleur + " et la valeur " + valeur);
    }

    // Méthode pour jouer une carte Espion
    private void jouerEspion(TacticalCard tacticalCard, Joueur joueur) {
        String couleur = choisirCouleur();
        tacticalCard.setCouleur(couleur);
        System.out.println(joueur.getNom() + " (IA) joue Espion avec la couleur " + couleur);
    }

    // Méthode pour jouer une carte Stratege
    private void jouerStratege(TacticalCard tacticalCard, Joueur joueur, List<Muraille> murailles) {
        System.out.println(joueur.getNom() + " (IA) utilise Stratège.");
        int sourceMurailleIndex = choisirMurailleAleatoire(murailles, joueur);

        if (sourceMurailleIndex != -1) {
            Muraille sourceMuraille = murailles.get(sourceMurailleIndex);
            Carte carteADeplacer = choisirCarteAleatoire(joueur);
            int targetMurailleIndex = choisirMurailleAleatoire(murailles, joueur);

            if (targetMurailleIndex != -1 && carteADeplacer != null) {
                Muraille targetMuraille = murailles.get(targetMurailleIndex);
                System.out.println(joueur.getNom() + " (IA) déplace " + carteADeplacer + " de la Muraille " + (sourceMurailleIndex + 1) + " à la Muraille " + (targetMurailleIndex + 1));
                if (joueur.equals(sourceMuraille.getJoueurRevendiquant())) {
                    sourceMuraille.getCartesJoueur1().remove(carteADeplacer);
                    targetMuraille.getCartesJoueur1().add(carteADeplacer);
                } else {
                    sourceMuraille.getCartesJoueur2().remove(carteADeplacer);
                    targetMuraille.getCartesJoueur2().add(carteADeplacer);
                }
            }
        }
    }

    // Méthode pour jouer une carte Traitre
    private void jouerTraitre(TacticalCard tacticalCard, Joueur joueur, List<Muraille> murailles) {
        System.out.println(joueur.getNom() + " (IA) utilise Traître.");
        int sourceMurailleIndex = choisirMurailleAleatoire(murailles, joueur);

        if (sourceMurailleIndex != -1) {
            Muraille sourceMuraille = murailles.get(sourceMurailleIndex);
            Carte carteAdverse = choisirCarteAdverse(sourceMuraille, joueur);

            if (carteAdverse != null) {
                int targetMurailleIndex = choisirMurailleAleatoire(murailles, joueur);

                if (targetMurailleIndex != -1) {
                    Muraille targetMuraille = murailles.get(targetMurailleIndex);
                    System.out.println(joueur.getNom() + " (IA) déplace une carte adverse vers une autre muraille.");
                    if (joueur.equals(sourceMuraille.getJoueurRevendiquant())) {
                        sourceMuraille.getCartesJoueur2().remove(carteAdverse);
                        targetMuraille.getCartesJoueur2().add(carteAdverse);
                    } else {
                        sourceMuraille.getCartesJoueur1().remove(carteAdverse);
                        targetMuraille.getCartesJoueur1().add(carteAdverse);
                    }
                }
            }
        }
    }

    // Méthode pour jouer une carte Banshee
    private void jouerBanshee(TacticalCard tacticalCard, Joueur joueur, List<Muraille> murailles) {
        System.out.println(joueur.getNom() + " (IA) utilise Banshee.");
        int sourceMurailleIndex = choisirMurailleAleatoire(murailles, joueur);

        if (sourceMurailleIndex != -1) {
            Muraille sourceMuraille = murailles.get(sourceMurailleIndex);
            Carte carteAdverse = choisirCarteAdverse(sourceMuraille, joueur);

            if (carteAdverse != null) {
                System.out.println(joueur.getNom() + " (IA) défausse une carte adverse : " + carteAdverse);
                sourceMuraille.getCartesJoueur2().remove(carteAdverse);
            }
        }
    }

    // Méthode pour choisir une carte adverse
    private Carte choisirCarteAdverse(Muraille muraille, Joueur joueur) {
        List<Carte> cartesAdverses = joueur.equals(muraille.getJoueurRevendiquant()) ? muraille.getCartesJoueur2() : muraille.getCartesJoueur1();
        if (cartesAdverses.isEmpty()) return null;
        return cartesAdverses.get(random.nextInt(cartesAdverses.size()));
    }

    // Méthode pour choisir une carte normale
    public Carte choisirCarteAleatoire(Joueur joueur) {
        List<Carte> main = joueur.getMain();
        if (main.isEmpty()) return null;
        return main.get(random.nextInt(main.size()));
    }

    // Méthode pour choisir une muraille de manière aléatoire
    public int choisirMurailleAleatoire(List<Muraille> murailles, Joueur joueur) {
        List<Integer> muraillesDisponibles = new ArrayList<>();
        for (int i = 0; i < murailles.size(); i++) {
            Muraille muraille = murailles.get(i);
            if (!muraille.estCompletePourJoueur(joueur) && muraille.getHolder() == null) {
                muraillesDisponibles.add(i);
            }
        }
        if (muraillesDisponibles.isEmpty()) return -1;
        return muraillesDisponibles.get(random.nextInt(muraillesDisponibles.size()));
    }

    // Méthode pour choisir une couleur automatiquement
    public String choisirCouleur() {
        String[] couleurs = {"Rouge", "Bleu", "Vert", "Violet", "Jaune", "Orange"};
        return couleurs[random.nextInt(couleurs.length)];
    }

    // Méthode pour choisir une valeur automatiquement
    public int choisirValeur() {
        return random.nextInt(9) + 1;
    }
    
    // Méthode pour piocher une carte depuis le/les deckss
    public Carte piocherCarteIA(Variante variante, List<Carte> deck, List<Carte> tacticalDeck) {
        if (variante == Variante.STANDARD) {
            if (!deck.isEmpty()) {
                return deck.remove(0);
            } else {
                return null; 
            }
        }

        if (variante == Variante.TACTIQUE) {
            if (deck.isEmpty() && tacticalDeck.isEmpty()) {
                return null;
            } else if (deck.isEmpty()) {
                return tacticalDeck.remove(0);
            } else if (tacticalDeck.isEmpty()) {
                return deck.remove(0); 
            } else {
                if (random.nextBoolean()) {
                    return deck.remove(0);
                } else {
                    return tacticalDeck.remove(0);
                }
            }
        }

        return null;
    }

}
