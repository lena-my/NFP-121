package allumettes;

public class StrategieHumain implements Strategie {
    /**
     * Cette stratégie permet à un joueur humain de choisir combien
     * d'allumettes il souhaite prendre.
     *
     * @param jeu le jeu en cours
     * @return le nombre d'allumettes à prendre
     */

    @Override
    public int getPrise(Jeu jeu) throws CoupInvalideException, OperationInterditeException {
        String entree = Lecteur.SCANNER.nextLine().trim();
        if (entree.equalsIgnoreCase("triche")) {
            if (jeu.getNombreAllumettes() > 2) {
                jeu.retirer(1);
                System.out.println("[Une allumette en moins, plus que " + jeu.getNombreAllumettes() + ". Chut !]");
            }
            else if (jeu.getNombreAllumettes() == 1) {
                jeu.retirer(-1);
            }
            throw new CoupInvalideException(0,"vide");
        } else {
            return Integer.parseInt(entree);
        }
    }
}

