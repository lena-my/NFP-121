package allumettes;

public class StrategieLente implements Strategie {
    /**
     * Cette stratégie est lente et prend toujours 1 allumette.
     * 
     * @param jeu le jeu en cours
     * @return le nombre d'allumettes à prendre
     */
    @Override
    public int getPrise(Jeu jeu) {        
        int prise;
        prise = 1; // Toujours prendre 1 allumette
        return prise;
    }
}