package allumettes;

public class StrategieNaif implements Strategie {
    /**
     * Cette stratégie choisit un nombre aléatoire d'allumettes à prendre,
     * entre 1 et 3, sans tenir compte de la situation du jeu.
     *
     * @param jeu le jeu en cours
     * @return le nombre d'allumettes à prendre (entre 1 et 3)
     */
    @Override
    public int getPrise(Jeu jeu) {
        int prise;
        prise = 1 + (int)(Math.random() * Math.min(jeu.getNombreAllumettes(), 3));
        return prise;
    }
}
