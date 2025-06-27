package allumettes;

public interface Strategie {
    /** Nombre d'allumettes que le joueur veut prendre.
     * @param jeu le jeu en cours
     * @return le nombre d'allumettes à prendre
     */
    int getPrise(Jeu jeu) throws CoupInvalideException, OperationInterditeException;
}