package allumettes;

public class Procuration implements Jeu {

    /** Jeu réel. */
    private Jeu jeuReel;

    /** Initialiser le proxy avec le jeu réel.
     * @param jeuReel le jeu réel
     */
    public Procuration(Jeu jeuReel) {
        this.jeuReel = jeuReel;
    }

    @Override
    public int getNombreAllumettes() {
        // Délégation de la méthode au jeu réel
        return jeuReel.getNombreAllumettes();
    }

    @Override
    public void retirer(int nbPrises) {
        // Ici, on bloque toute tentative de modification du jeu
        throw new OperationInterditeException("Triche");
    }

    @Override
    public String toString() {
        return jeuReel.toString();
    }
}

