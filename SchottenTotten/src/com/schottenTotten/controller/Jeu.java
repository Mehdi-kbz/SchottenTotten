package com.schottenTotten.controller;

import com.schottenTotten.model.Muraille;
import com.schottenTotten.model.TacticalCard;
import com.schottenTotten.model.Carte;
import com.schottenTotten.model.Joueur;
import com.schottenTotten.model.Variante;
import com.schottenTotten.ai.IA;
import com.schottenTotten.stats.ScoreManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Jeu {
	
	// Variables
    private Joueur joueur1;
    private Joueur joueur2;
    private IA ia;
    private ScoreManager scoreManager;
    private Variante variante;
    private List<Muraille> murailles;
    public List<Carte> deck;    
    public List<Carte> TacticalDeck;
    private List<Carte> discardPile = new ArrayList<>();
    
    /////////// Méthodes ///////
    public List<Carte> getDeck() {
        return deck;
    }
    
    public List<Carte> getTacticalDeck() {
        return TacticalDeck;
    }
    
    // Constructeur
    public Jeu(String nomJoueur1, boolean isIA1, String nomJoueur2, boolean isIA2, Variante variante) {
        this.joueur1 = new Joueur(nomJoueur1, isIA1);
        this.joueur2 = new Joueur(nomJoueur2, isIA2);
        this.murailles = new ArrayList<>();
        this.deck = new ArrayList<>();
        this.TacticalDeck = new ArrayList<>();
        this.ia = new IA();
        this.scoreManager = new ScoreManager();
        this.variante = variante;

        // Initialisation des murailles et des cartes
        initialiserMurailles();
        initialiserCartes();
    }

    private void initialiserMurailles() {
        // Ajoute des Murailles avec différents types de formation
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
        murailles.add(new Muraille(joueur1,joueur2));
    }

    private void initialiserCartes() {
        // Création des cartes normales
        String[] couleurs = {"Rouge", "Bleu", "Vert", "Violet", "Jaune", "Orange"};
        for (String couleur : couleurs) {
            for (int valeur = 1; valeur <= 9; valeur++) {
                deck.add(new Carte(couleur, valeur));
            }
        }

        // Ajout des cartes spéciales pour la variante Tactique
        if (variante == Variante.TACTIQUE || variante == Variante.EXPERTS) {

        	// Ajout des cartes Troupes d'élite
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.JOKER));
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.JOKER));
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.ESPION));
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.PORTE_BOUCLIER));

        	// Ajout des cartes Modes de combat
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.COLIN_MAILLARD));
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.COMBAT_DE_BOUE));

        	// Ajout des cartes Ruses
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.CHASSEUR_DE_TETE));
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.STRATEGE));
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.BANSHEE));
        	TacticalDeck.add(new TacticalCard(TacticalCard.TacticalType.TRAITRE));

        	// Mélange du deck tactique
        	Collections.shuffle(TacticalDeck);

        }

        // Mélange des cartes
        Collections.shuffle(deck);
        Collections.shuffle(TacticalDeck);

        // Distribution initiale des cartes aux joueurs
        if (variante == Variante.TACTIQUE || variante == Variante.EXPERTS) {
        	int nombreCartesParJoueur = 7;
	        for (int i = 0; i < nombreCartesParJoueur-1; i++) {
	            joueur1.ajouterCarte(deck.remove(0));
	            joueur2.ajouterCarte(deck.remove(0));
	        }
	        joueur1.ajouterCarte(TacticalDeck.remove(0));
            joueur2.ajouterCarte(TacticalDeck.remove(0));
        }
            
        if (variante == Variante.STANDARD) {
	        int nombreCartesParJoueur = 6;
	        for (int i = 0; i < nombreCartesParJoueur; i++) {
	            joueur1.ajouterCarte(deck.remove(0));
	            joueur2.ajouterCarte(deck.remove(0));
	        }
        }
    }
     
    public void afficherEtat(String nomJoueur1, String nomJoueur2) {
        System.out.println("=== État actuel des murailles ===");
        for (int i = 0; i < murailles.size(); i++) {
            Muraille muraille = murailles.get(i);
            Joueur titulaire = muraille.getJoueurRevendiquant();
            String titulaireNom = (titulaire != null) ? titulaire.getNom() : "Non revendiquée";

            System.out.println("Muraille " + (i + 1) + " (" + titulaireNom + ") :");
            System.out.println(nomJoueur1 + " : " + muraille.getCartesJoueur1());
            System.out.println(nomJoueur2 + " : " + muraille.getCartesJoueur2());
        }
    }

    public Joueur getJoueur(int joueurNum) {
        return joueurNum == 1 ? joueur1 : joueur2;
    }

    public void jouerTour(int numeroMuraille, Joueur joueur, Carte carte, Scanner scanner) {
        Muraille muraille = murailles.get(numeroMuraille);

        if (carte instanceof TacticalCard) {
            TacticalCard tacticalCard = (TacticalCard) carte;

            switch (tacticalCard.getType()) {

            	case JOKER:
	                // Ajouter le Joker à la muraille
	                if (muraille.ajouterCarte(joueur, tacticalCard)) {
	                    System.out.println(joueur.getNom() + " a joué Joker sur la Muraille " + (numeroMuraille + 1));
	                } else {
	                    System.out.println("Impossible de jouer le Joker sur la Muraille " + (numeroMuraille + 1));
	                }
	                break;
	
	            case ESPION:
	                // Gérer la carte Espion
	                if (muraille.ajouterCarte(joueur, tacticalCard)) {
	                    System.out.println(joueur.getNom() + " joue Espion sur la Muraille " + (numeroMuraille + 1));
	                } else {
	                    System.out.println("Impossible de jouer l'Espion sur la Muraille " + (numeroMuraille + 1));
	                }
                
	            case PORTE_BOUCLIER:
                    // Ajouter la carte tactique comme si c'était une carte normale
                    if (muraille.ajouterCarte(joueur, tacticalCard)) {
                        System.out.println(joueur.getNom() + " a joué " + tacticalCard + " sur la Muraille " + (numeroMuraille + 1));
                    } else {
                        System.out.println("Impossible de jouer la carte tactique sur la Muraille " + (numeroMuraille + 1));
                    }
                    break;
                    
	            case COLIN_MAILLARD:
	                muraille.activerColinMaillard();
	                System.out.println(joueur.getNom() + " a activé Colin-Maillard sur la Muraille " + (numeroMuraille + 1));
	                discardPile.add(carte); // Défausser la carte après utilisation
	                joueur.getMain().remove(carte); // Retirer la carte de la main
	                break;
	
	            case COMBAT_DE_BOUE:
                    tacticalCard.activerCombatDeBoue(muraille);
                    System.out.println(joueur.getNom() + " a activé Combat de Boue sur la Muraille " + (numeroMuraille + 1));
                    break;
                    
                case CHASSEUR_DE_TETE:
                    System.out.println(joueur.getNom() + " joue Chasseur de Tête.");
                    List<Carte> cartesPiochees = new ArrayList<>();

                    // Étape 1 : Retirer la carte Chasseur de Tête de la main immédiatement
                    joueur.getMain().remove(carte); // Enlever la carte jouée de la main
                    discardPile.add(carte); // Ajouter la carte à la pile de défausse
                    System.out.println("La carte Chasseur de Tête a été défaussée dans la pile de défausse.");

                    // Étape 2 : Piocher trois cartes
                    int cartesRestantes = 3;
                    while (cartesRestantes > 0) {
                        try {
                            System.out.println("Choisissez la pioche pour prendre une carte :");
                            System.out.println("1 : Deck principal (" + deck.size() + " cartes restantes)");
                            System.out.println("2 : Deck tactique (" + TacticalDeck.size() + " cartes restantes)");

                            int choix = scanner.nextInt();
                            if (choix == 1 && !deck.isEmpty()) {
                                Carte cartePiochee = deck.remove(0);
                                cartesPiochees.add(cartePiochee);
                                cartesRestantes--;
                            } else if (choix == 2 && !TacticalDeck.isEmpty()) {
                                Carte cartePiochee = TacticalDeck.remove(0);
                                cartesPiochees.add(cartePiochee);
                                cartesRestantes--;
                            } else {
                                System.out.println("Choix invalide ou pioche vide. Réessayez.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Réessayez.");
                            scanner.nextLine(); // Nettoyer le buffer
                        }
                    }

                    // Ajouter les cartes piochées à la main du joueur
                    joueur.getMain().addAll(cartesPiochees);
                    System.out.println("Cartes piochées : " + cartesPiochees);

                    // Étape 3 : Choisir deux cartes à replacer sous leur pioche d'origine
                    List<Carte> cartesARemettre = new ArrayList<>();
                    while (cartesARemettre.size() < 2) {
                        try {
                            System.out.println("Choisissez une carte de votre main à replacer sous sa pioche (1-" + joueur.getMain().size() + "):");
                            for (int i = 0; i < joueur.getMain().size(); i++) {
                                System.out.println((i + 1) + ": " + joueur.getMain().get(i));
                            }

                            int choixCarte = scanner.nextInt() - 1;
                            if (choixCarte >= 0 && choixCarte < joueur.getMain().size()) {
                                Carte carteChoisie = joueur.getMain().remove(choixCarte);
                                cartesARemettre.add(carteChoisie);
                            } else {
                                System.out.println("Choix invalide. Réessayez.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Réessayez.");
                            scanner.nextLine(); // Nettoyer le buffer
                        }
                    }

                    // Étape 4 : Placer les cartes choisies sous leur pioche respective
                    for (Carte carteARemettre : cartesARemettre) {
                        if (carteARemettre instanceof TacticalCard) {
                            TacticalDeck.add(TacticalDeck.size(), carteARemettre); // Ajouter à la fin du deck tactique
                        } else {
                            deck.add(deck.size(), carteARemettre); // Ajouter à la fin du deck principal
                        }
                    }
                    System.out.println("Les cartes " + cartesARemettre + " ont été replacées sous leur pioche respective.");
                    break;

                case STRATEGE:
                    System.out.println(joueur.getNom() + " joue Stratège.");

                    // Étape 1 : Choisir une muraille non revendiquée contenant une carte du joueur
                    Muraille murailleSourceStratege = null; // Renommée pour éviter les conflits
                    while (murailleSourceStratege == null) {
                        try {
                            System.out.println("Choisissez une Muraille non revendiquée contenant une carte de votre côté à déplacer :");
                            for (int i = 0; i < murailles.size(); i++) {
                                List<Carte> cartesJoueur = joueur.equals(joueur1) ? murailles.get(i).getCartesJoueur1() : murailles.get(i).getCartesJoueur2();

                                if (murailles.get(i).getHolder() == null && !cartesJoueur.isEmpty()) {
                                    System.out.println((i + 1) + ": Muraille " + (i + 1));
                                }
                            }

                            int murailleSourceIndex = scanner.nextInt() - 1;
                            if (murailleSourceIndex >= 0 && murailleSourceIndex < murailles.size()) {
                                List<Carte> cartesJoueur = joueur.equals(joueur1) ? murailles.get(murailleSourceIndex).getCartesJoueur1() : murailles.get(murailleSourceIndex).getCartesJoueur2();
                                if (murailles.get(murailleSourceIndex).getHolder() == null && !cartesJoueur.isEmpty()) {
                                    murailleSourceStratege = murailles.get(murailleSourceIndex);
                                } else {
                                    System.out.println("Choix invalide. Veuillez réessayer.");
                                }
                            } else {
                                System.out.println("Choix invalide. Veuillez réessayer.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                            scanner.nextLine(); // Nettoie le buffer
                        }
                    }

                    // Étape 2 : Choisir une carte à déplacer
                    Carte carteChoisieStratege = null; // Renommée pour éviter les conflits
                    while (carteChoisieStratege == null) {
                        try {
                            List<Carte> cartesJoueur = joueur.equals(joueur1) ? murailleSourceStratege.getCartesJoueur1() : murailleSourceStratege.getCartesJoueur2();

                            System.out.println("Choisissez une carte de votre côté à déplacer (1-" + cartesJoueur.size() + "):");
                            for (int i = 0; i < cartesJoueur.size(); i++) {
                                System.out.println((i + 1) + ": " + cartesJoueur.get(i));
                            }

                            int carteChoisieIndex = scanner.nextInt() - 1;
                            if (carteChoisieIndex >= 0 && carteChoisieIndex < cartesJoueur.size()) {
                                carteChoisieStratege = cartesJoueur.remove(carteChoisieIndex);
                            } else {
                                System.out.println("Choix invalide. Veuillez réessayer.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                            scanner.nextLine(); // Nettoie le buffer
                        }
                    }

                    // Étape 3 : Choisir une action : Déplacer ou défausser
                    while (true) {
                        try {
                            System.out.println("Que souhaitez-vous faire avec la carte ?");
                            System.out.println("1: Déplacer sur une autre Muraille non revendiquée");
                            System.out.println("2: Défausser la carte");
                            int choixAction = scanner.nextInt();

                            if (choixAction == 1) {
                                // Déplacer vers une autre muraille non revendiquée
                                Muraille murailleCibleStratege = null; // Renommée pour éviter les conflits
                                while (murailleCibleStratege == null) {
                                    try {
                                        System.out.println("Choisissez une autre Muraille non revendiquée pour déplacer la carte :");
                                        for (int i = 0; i < murailles.size(); i++) {
                                            if (murailles.get(i).getHolder() == null && !murailles.get(i).equals(murailleSourceStratege)) {
                                                System.out.println((i + 1) + ": Muraille " + (i + 1));
                                            }
                                        }

                                        int murailleCibleIndex = scanner.nextInt() - 1;
                                        if (murailleCibleIndex >= 0 && murailleCibleIndex < murailles.size() &&
                                            murailles.get(murailleCibleIndex).getHolder() == null && !murailles.get(murailleCibleIndex).equals(murailleSourceStratege)) {
                                            murailleCibleStratege = murailles.get(murailleCibleIndex);
                                        } else {
                                            System.out.println("Choix invalide. Veuillez réessayer.");
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                                        scanner.nextLine(); // Nettoie le buffer
                                    }
                                }

                                // Ajouter la carte à la muraille cible
                                if (joueur.equals(joueur1)) {
                                    murailleCibleStratege.getCartesJoueur1().add(carteChoisieStratege);
                                } else {
                                    murailleCibleStratege.getCartesJoueur2().add(carteChoisieStratege);
                                }
                                System.out.println("La carte " + carteChoisieStratege + " a été déplacée vers la Muraille " + (murailles.indexOf(murailleCibleStratege) + 1) + ".");
                                break;

                            } else if (choixAction == 2) {
                                // Défausser la carte
                                discardPile.add(carteChoisieStratege);
                                System.out.println("La carte " + carteChoisieStratege + " a été défaussée.");
                                break;
                            } else {
                                System.out.println("Choix invalide. Veuillez entrer 1 ou 2.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                            scanner.nextLine(); // Nettoie le buffer
                        }
                    }
                    break;
                	
                case BANSHEE:
                    System.out.println(joueur.getNom() + " joue Banshee.");
                    
                    // Choisir une Muraille non revendiquée contenant des cartes adverses
                    Muraille murailleSourceBanshee = null;
                    while (murailleSourceBanshee == null) {
                        try {
                            System.out.println("Choisissez une Muraille non revendiquée contenant une carte adverse à défausser :");
                            for (int i = 0; i < murailles.size(); i++) {
                                if (murailles.get(i).getHolder() == null && !murailles.get(i).getCartesJoueur2().isEmpty()) {
                                    System.out.println((i + 1) + ": Muraille " + (i + 1));
                                }
                            }

                            int murailleSourceIndex = scanner.nextInt() - 1;
                            if (murailleSourceIndex >= 0 && murailleSourceIndex < murailles.size() &&
                                murailles.get(murailleSourceIndex).getHolder() == null &&
                                !murailles.get(murailleSourceIndex).getCartesJoueur2().isEmpty()) {
                                murailleSourceBanshee = murailles.get(murailleSourceIndex);
                            } else {
                                System.out.println("Choix invalide. Veuillez réessayer.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                            scanner.nextLine(); // Nettoie le buffer
                        }
                    }
                    
                    // Choisir une carte adverse à défausser
                    Carte carteAdverseBanshee = null;
                    while (carteAdverseBanshee == null) {
                        try {
                            System.out.println("Choisissez une carte adverse à défausser (1-" + murailleSourceBanshee.getCartesJoueur2().size() + "):");
                            for (int i = 0; i < murailleSourceBanshee.getCartesJoueur2().size(); i++) {
                                System.out.println((i + 1) + ": " + murailleSourceBanshee.getCartesJoueur2().get(i));
                            }

                            int carteAdverseIndex = scanner.nextInt() - 1;
                            if (carteAdverseIndex >= 0 && carteAdverseIndex < murailleSourceBanshee.getCartesJoueur2().size()) {
                                carteAdverseBanshee = murailleSourceBanshee.getCartesJoueur2().remove(carteAdverseIndex);
                            } else {
                                System.out.println("Choix invalide. Veuillez réessayer.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                            scanner.nextLine(); // Nettoie le buffer
                        }
                    }
                    
                    // Ajouter la carte à la pile de défausse
                    discardPile.add(carteAdverseBanshee);
                    System.out.println("La carte " + carteAdverseBanshee + " a été défaussée dans la pile de défausse.");
                    break;

                case TRAITRE:
                    Muraille murailleSource = null;
                    while (murailleSource == null) {
                        try {
                            // Déterminez les murailles adverses disponibles
                            System.out.println("Choisissez une Muraille non revendiquée contenant une carte adverse à déplacer :");
                            List<Integer> muraillesDisponibles = new ArrayList<>();
                            for (int i = 0; i < murailles.size(); i++) {
                                List<Carte> cartesAdverses = joueur.equals(joueur1) ? murailles.get(i).getCartesJoueur2() : murailles.get(i).getCartesJoueur1();

                                if (murailles.get(i).getHolder() == null && !cartesAdverses.isEmpty()) {
                                    muraillesDisponibles.add(i);
                                    System.out.println((i + 1) + ": Muraille " + (i + 1));
                                }
                            }

                            if (muraillesDisponibles.isEmpty()) {
                                System.out.println("Aucune muraille adverse non revendiquée disponible. La carte Traître n'a aucun effet.");
                                return;
                            }

                            int murailleSourceIndex = scanner.nextInt() - 1;

                            if (muraillesDisponibles.contains(murailleSourceIndex)) {
                                murailleSource = murailles.get(murailleSourceIndex);
                            } else {
                                System.out.println("Choix invalide. Veuillez réessayer.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                            scanner.nextLine(); // Nettoie le buffer du scanner
                        }
                    }

                    Carte carteAdverse = null;
                    while (carteAdverse == null) {
                        try {
                            // Déterminez les cartes adverses disponibles dans la muraille source
                            List<Carte> cartesAdverses = joueur.equals(joueur1) ? murailleSource.getCartesJoueur2() : murailleSource.getCartesJoueur1();

                            System.out.println("Choisissez une carte adverse à déplacer (1-" + cartesAdverses.size() + "):");
                            for (int i = 0; i < cartesAdverses.size(); i++) {
                                System.out.println((i + 1) + ": " + cartesAdverses.get(i));
                            }

                            int carteAdverseIndex = scanner.nextInt() - 1;

                            if (carteAdverseIndex >= 0 && carteAdverseIndex < cartesAdverses.size()) {
                                carteAdverse = cartesAdverses.remove(carteAdverseIndex);
                            } else {
                                System.out.println("Choix invalide. Veuillez réessayer.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                            scanner.nextLine(); // Nettoie le buffer du scanner
                        }
                    }

                    Muraille murailleCible = null;
                    while (murailleCible == null) {
                        try {
                            System.out.println("Choisissez une Muraille non revendiquée où déplacer la carte :");
                            List<Integer> muraillesDisponibles = new ArrayList<>();
                            for (int i = 0; i < murailles.size(); i++) {
                                if (murailles.get(i).getHolder() == null && !murailles.get(i).equals(murailleSource)) {
                                    muraillesDisponibles.add(i);
                                    System.out.println((i + 1) + ": Muraille " + (i + 1));
                                }
                            }

                            if (muraillesDisponibles.isEmpty()) {
                                System.out.println("Aucune autre Muraille disponible. La carte retourne au deck adverse.");
                                if (joueur.equals(joueur1)) {
                                    murailleSource.getCartesJoueur2().add(carteAdverse);
                                } else {
                                    murailleSource.getCartesJoueur1().add(carteAdverse);
                                }
                                return;
                            }

                            int murailleCibleIndex = scanner.nextInt() - 1;

                            if (muraillesDisponibles.contains(murailleCibleIndex)) {
                                murailleCible = murailles.get(murailleCibleIndex);
                            } else {
                                System.out.println("Choix invalide. Veuillez réessayer.");
                            }
                        } catch (Exception e) {
                            System.out.println("Entrée invalide. Veuillez entrer un numéro valide.");
                            scanner.nextLine(); // Nettoie le buffer du scanner
                        }
                    }

                    if (murailleCible != null) {
                        if (joueur.equals(joueur1)) {
                            murailleCible.getCartesJoueur1().add(carteAdverse);
                        } else {
                            murailleCible.getCartesJoueur2().add(carteAdverse);
                        }
                        System.out.println("La carte " + carteAdverse + " a été déplacée vers la Muraille " + (murailles.indexOf(murailleCible) + 1) + ".");
                    }
                    break;
                    
                

                default:
                    System.out.println("Carte tactique jouée : " + tacticalCard.getType());
                    break;
            }
        } else {
            // Logique pour les cartes normales
            if (muraille.ajouterCarte(joueur, carte)) {
                System.out.println(joueur.getNom() + " a joué " + carte + " sur la Muraille " + (numeroMuraille + 1));
                
                // Vérifier si la muraille peut être revendiquée
                Joueur titulaire = muraille.revendiquerBorne();
                if (titulaire != null) {
                    System.out.println("La Muraille " + (numeroMuraille + 1) + " a été revendiquée par " + titulaire.getNom() + " !");
                } else {
                    System.out.println("La Muraille " + (numeroMuraille + 1) + " n'est pas encore revendiquée.");
                }
            } else {
                System.out.println("Impossible de jouer sur la Muraille " + (numeroMuraille + 1) + " (limite atteinte ou règle non respectée).");
            }
        }

    }

    public void jouerTourIA(int joueurNum) {
        Joueur joueur = getJoueur(joueurNum);

        System.out.println(joueur.getNom() + " (IA) commence son tour.");

        // Choisir une carte stratégique (priorité aux tactiques)
        Carte carteChoisie = ia.choisirCarteStrategique(joueur, murailles);
        if (carteChoisie == null) {
            System.out.println(joueur.getNom() + " (IA) n'a plus de cartes à jouer.");
            return;
        }

        // Vérifier si la carte est une carte tactique
        if (carteChoisie instanceof TacticalCard tacticalCard) {
            System.out.println(joueur.getNom() + " (IA) a choisi de jouer la carte tactique : " + tacticalCard.getType());
            ia.jouerCarteTactique(tacticalCard, joueur, murailles); // Jouer la carte tactique
            joueur.getMain().remove(carteChoisie); // Retirer la carte de la main
            discardPile.add(carteChoisie); // Ajouter la carte à la pile de défausse
        } else {
            // Jouer une carte normale
            int murailleChoisie = ia.choisirMurailleAleatoire(murailles, joueur);
            if (murailleChoisie == -1) {
                System.out.println("Aucune muraille disponible pour jouer. " + joueur.getNom() + " (IA) passe son tour.");
                return;
            }

            Muraille muraille = murailles.get(murailleChoisie);
            if (muraille.ajouterCarte(joueur, carteChoisie)) {
                joueur.getMain().remove(carteChoisie); // Retirer la carte de la main après l'avoir jouée
                System.out.println(joueur.getNom() + " (IA) a joué " + carteChoisie + " sur la Muraille " + (murailleChoisie + 1));

                // Vérifier si la muraille peut être revendiquée
                Joueur titulaire = muraille.revendiquerBorne();
                if (titulaire != null) {
                    System.out.println("La Muraille " + (murailleChoisie + 1) + " a été revendiquée par " + titulaire.getNom() + " !");
                } else {
                    System.out.println("La Muraille " + (murailleChoisie + 1) + " n'est pas encore revendiquée.");
                }
            } else {
                System.out.println("Impossible de jouer sur la Muraille " + (murailleChoisie + 1) + ". L'IA passe son tour.");
            }
        }

        // Piocher une nouvelle carte
        Carte nouvelleCarte = ia.piocherCarteIA(variante, deck, TacticalDeck);
        if (nouvelleCarte != null) {
            joueur.ajouterCarte(nouvelleCarte);
            System.out.println(joueur.getNom() + " (IA) a pioché : " + nouvelleCarte);
        } else {
            System.out.println(joueur.getNom() + " (IA) n'a pas pu piocher de carte (tous les decks sont vides).");
        }

        System.out.println("L'IA a terminé son tour.");
    }

    public boolean verifierVictoire() {
        int muraillesRevendiqueesJoueur1 = 0;
        int muraillesRevendiqueesJoueur2 = 0;

        for (Muraille muraille : murailles) {
            Joueur gagnant = muraille.getJoueurRevendiquant();
            if (gagnant == joueur1 ) {
                muraillesRevendiqueesJoueur1++;
            } else if (gagnant == joueur2) {
                muraillesRevendiqueesJoueur2++;
            }
        }

        if (muraillesRevendiqueesJoueur1 >= 5) {
            System.out.println(joueur1.getNom() + " remporte la partie !");
            scoreManager.ajouterVictoire(joueur1.getNom());
            scoreManager.afficherScores();
            return true;
        } else if (muraillesRevendiqueesJoueur2 >= 5) {
            System.out.println(joueur2.getNom() + " remporte la partie !");
            scoreManager.ajouterVictoire(joueur2.getNom());
            scoreManager.afficherScores();
            return true;
        }

        // Condition de fin : Les deux joueurs n'ont plus de cartes
        if (joueur1.getMain().isEmpty() && joueur2.getMain().isEmpty()) {
            System.out.println("Les deux joueurs n'ont plus de cartes. Fin de la partie !");
            
            // Ajout du joueur ayant revendiqué le plus de murailles
            if (muraillesRevendiqueesJoueur1 > muraillesRevendiqueesJoueur2) {
                scoreManager.ajouterVictoire(joueur1.getNom());
                System.out.println(joueur1.getNom() + " remporte la partie par domination des murailles !");
            } else if (muraillesRevendiqueesJoueur2 > muraillesRevendiqueesJoueur1) {
                scoreManager.ajouterVictoire(joueur2.getNom());
                System.out.println(joueur2.getNom() + " remporte la partie par domination des murailles !");
            } 
            scoreManager.afficherScores();
            return true;
        }

        return false;
    }

    public void afficherDiscardPile() {
        System.out.println("=== Pile de défausse ===");
        if (discardPile.isEmpty()) {
            System.out.println("La pile de défausse est vide.");
        } else {
            for (Carte carte : discardPile) {
                System.out.println(carte);
            }
        }
    }

    public List<Carte> getDiscardPile() {
        return discardPile;
    }

    public Variante getVariante() {
        return variante;
    }

    public Carte piocherCarte(Variante variante, Scanner scanner) {
        // Cas STANDARD : Piocher uniquement depuis le deck principal
        if (variante == Variante.STANDARD) {
            if (!deck.isEmpty()) {
                return deck.remove(0);
            } else {
                System.out.println("Le deck est vide. Aucune carte n'a pu être piochée.");
                return null;
            }
        }

        // Cas TACTIQUE : Choisir entre les deux decks
        if (variante == Variante.TACTIQUE || variante == Variante.EXPERTS) {
            while (true) {
                if (deck.isEmpty() && TacticalDeck.isEmpty()) {
                    System.out.println("Les deux decks sont vides. Aucune carte n'a pu être piochée.");
                    return null;
                } else if (deck.isEmpty()) {
                    System.out.println("Le deck principal est vide. Vous piochez automatiquement depuis le deck tactique.");
                    return TacticalDeck.remove(0);
                } else if (TacticalDeck.isEmpty()) {
                    System.out.println("Le deck tactique est vide. Vous piochez automatiquement depuis le deck principal.");
                    return deck.remove(0);
                }

                // Proposez au joueur de choisir entre les deux decks
                System.out.println("Choisissez le deck à partir duquel piocher :");
                System.out.println("1 : Deck principal (" + deck.size() + " cartes restantes)");
                System.out.println("2 : Deck tactique (" + TacticalDeck.size() + " cartes restantes)");

                try {
                    int choix = scanner.nextInt();
                    if (choix == 1) {
                        return deck.remove(0);
                    } else if (choix == 2) {
                        return TacticalDeck.remove(0);
                    } else {
                        System.out.println("Choix invalide. Veuillez entrer 1 ou 2.");
                    }
                } catch (Exception e) {
                    System.out.println("Entrée invalide. Veuillez entrer un nombre.");
                    scanner.nextLine(); // Vide le scanner
                }
            }
        }

        // Si aucun cas n'est défini
        return null;
    }
    
    public int getTailleDeck() {
        return deck.size();
    }

    public int getTailleTacticalDeck() {
        return TacticalDeck.size();
    }
}
