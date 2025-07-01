package allumettes;
// import swing.MatchGameFrame;

/** Lance une partie des 13 allumettes en fonction des arguments fournis
 * sur la ligne de commande.
 * @author	Xavier Crégut
 * @version	$Revision: 1.5 $
 */
public class Jouer {

	/** Lancer une partie. En argument sont donnés les deux joueurs sous
	 * la forme nom@stratégie.
	 * @param args la description des deux joueurs
	 */
	public static void main(String[] args) {
		// swing.MatchGameFrame.main(new String[0]);

		try {
			boolean confiant;
			String[] joueurs; // arguments des joueurs
			Jeu jeu; // le jeu en cours

			verifierNombreArguments(args); // vérifie le nombre d'arguments

			// Créer les joueurs à partir des arguments, dont la ligne de commande est
			// de la forme nom@stratégie, où stratégie est l'une des suivantes :
			// naif, rapide, expert, humain, tricheur.

			// Exemple : java allumettes.Jouer -confiant Xavier@humain Ordinateur@naif
			// On peut aussi utiliser "-confiant" pour l'arbitre (optionnel).

			if (args.length == 3 && args[0].equals("-confiant")) {
				confiant = true;
				joueurs = new String[] { args[1], args[2] };
			} else if (args.length == 2) {
				confiant = false;
				joueurs = args;
			} else {
				throw new ConfigurationException("Le nombre d'arguments n'est pas valide.");
			}

			Joueur j1 = creerJoueur(joueurs[0]);
			Joueur j2 = creerJoueur(joueurs[1]);

			// Créer le jeu
			jeu = new JeuReel();

			/** Lancer la partie avec l'arbitre et les joueurs
			 * l'arbitre est responsable de la gestion du jeu et il envoye le jeu
			 * aux joueurs pour qu'ils puissent décider combien d'allumettes prendre.
			 * si confiant, l'arbitre envoye le jeu directement aux joueurs,
			 * sinon, jeu est une procuration qui empeche les joueurs de tricher sur la classe JeuReel.
			 */

			// Créer l'arbitre avec les deux joueurs
			Arbitre arbitre = new Arbitre(j1, j2, confiant);

			// Lance la partie
			arbitre.arbitrer(jeu);

		} catch (ConfigurationException e) {
			System.out.println();
			System.out.println("Erreur : " + e.getMessage());
			afficherUsage();
			System.exit(1);
		}
	}

	private static void verifierNombreArguments(String[] args) {
		final int nbJoueurs = 2;
		if (args.length < nbJoueurs) {
			throw new ConfigurationException("Trop peu d'arguments : "
					+ args.length);
		}
		if (args.length > nbJoueurs + 1) { // +1 pour l'option -confiant
			throw new ConfigurationException("Trop d'arguments : "
					+ args.length);
		}
	}

	private static Joueur creerJoueur(String arg) {
        String[] nomEtStrategie = arg.split("@");
        if (nomEtStrategie.length != 2) {
            throw new ConfigurationException("Format joueur invalide : " + arg);
        }
        String nom = nomEtStrategie[0];
        Strategie strategie = getStrategie(nomEtStrategie[1], nom);
        return new Joueur(nom, strategie);
    }

    public static Strategie getStrategie(String strategieNom, String nomJoueur) {
        return switch (strategieNom.toLowerCase()) {
            case "naif"     -> new StrategieNaif();
            case "rapide"   -> new StrategieRapide();
            case "expert"   -> new StrategieExpert();
            case "humain"   -> new StrategieHumain();
            case "tricheur" -> new StrategieTriche();
            case "lente"    -> new StrategieLente();
            case "swing"    -> new StrategieSwing(nomJoueur);
            default -> throw new ConfigurationException("Stratégie inconnue : " + strategieNom);
        };
    }

		/** Afficher des indications sur la manière d'exécuter cette classe. */
		public static void afficherUsage() {
			System.out.println("""
	Usage :
		java allumettes.Jouer joueur1 joueur2
			joueur est de la forme nom@stratégie
			strategie = naif | rapide | expert | humain | tricheur | lente

		Exemple :
			java allumettes.Jouer Xavier@humain Ordinateur@naif
	""");
		}

}
