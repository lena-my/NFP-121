package allumettes;

public class Arbitre {
    private final Joueur j1, j2;
    private final boolean confiant;
    private Joueur joueurCourant;

    public Arbitre(Joueur j1, Joueur j2, boolean confiant) {
        this.j1 = j1;
        this.j2 = j2;
        this.confiant = confiant; // Indiquer si l'arbitre est confiant ou méfiant
        this.joueurCourant = j1; // Initialiser le joueur courant
    }

    public void arbitrer(Jeu jeu) {
        while (jeu.getNombreAllumettes() > 0) {
            try {
                // Jouer un tour
                jouerTour(jeu);

                // Vérifier si le jeu est terminé après le coup joué
                if (jeu.getNombreAllumettes() == 0) {
                    // le jeu est terminé. le joueur courant a perdu
                    System.out.println(joueurCourant.getNom() + " perd !");

                    // et le joueur suivant a gagné
                    Joueur gagnant = (joueurCourant == j1) ? j2 : j1;
                    System.out.println(gagnant.getNom() + " gagne !");
                    break;
                }else {
                    // alternance des joueurs
                    changeJoueur();
                }
            } catch (OperationInterditeException e) {
                System.out.println("Abandon de la partie car " + joueurCourant.getNom() + " triche !");
                return;  // return ici termine la partie si un joueur triche
            }
        }
    }

    private void jouerTour(Jeu jeu) throws OperationInterditeException  {
        int prise;
        Jeu procuration = new Procuration(jeu);

        boolean imprimirEtat = true; // Indicateur pour afficher l'état du jeu
        while (true) {
            // Afficher une ligne vide avant l'état du jeu
            // System.out.println();
            if (imprimirEtat) {
                // Afficher l'état du jeu
                System.out.println(jeu.toString());
                imprimirEtat = false; // Ne pas afficher l'état du jeu à chaque itération
            }

            if (joueurCourant.strategie instanceof StrategieHumain) {
                // Si le joueur utilise une stratégie humaine, on lui demande combien d'allumettes il veut prendre
                System.out.print(joueurCourant.getNom() + ", combien d'allumettes ? ");
			}
            try {
                // Utilise getPrise de la stratégie du joueur courant pour obtenir le nombre d'allumettes à prendre
                if (confiant) {
                    // Si l'arbitre est confiant, on envoie le jeu réel
                    prise = joueurCourant.getPrise(jeu);
                } else {
                    // l'arbitre est méfiant, on utilise une procuration
                    prise = joueurCourant.getPrise(procuration);
                }

                // On assume que la prise est valide et l'arbitre retire les allumettes du jeu
                jeu.retirer(prise);

                
                System.out.println();
                break;

            } catch (NumberFormatException e) {
                System.out.println("Vous devez donner un entier.");
                imprimirEtat = false; // ne pas afficher l'état du jeu à la prochaine itération
                continue;
            } catch (CoupInvalideException e) {
                // cas de triche ou entrée vide
                int coup = e.getCoup();
                int max = Math.min(3, jeu.getNombreAllumettes());
                String probleme = e.getProbleme();
                // Si la prise est vide, on répète la question
                // Si la prise est < 1, on affiche un message d'erreur
                // Si la prise est > max, on affiche un message d'erreur
                if(probleme.equalsIgnoreCase("vide")){
                    imprimirEtat = false;
                    continue;
                } else if (coup < 1)  {
                    System.out.println("Impossible ! Nombre invalide : " + coup + " (< 1)");
                    System.out.println();
                    imprimirEtat = true; // affiche l'état du jeu à la prochaine itération
                } else if (coup > max)  {
                    System.out.println("Impossible ! Nombre invalide : " + coup + " (> " + max + ")");
                    System.out.println();
                    imprimirEtat = true; // affiche l'état du jeu à la prochaine itération
                } else {
                    System.out.println("##### NUNCA DEVERIA TER CAIDO AQUI ######");
                    System.out.println();
                    imprimirEtat = true; // affiche l'état du jeu à la prochaine itération
                }
            }
        }
    }

    private void changeJoueur() {
        // Alternance des joueurs
        joueurCourant = (joueurCourant == j1) ? j2 : j1;
    }
}