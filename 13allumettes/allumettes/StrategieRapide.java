package allumettes;

public class StrategieRapide implements Strategie {
    /**
     * Cette stratégie est basée sur le fait que le joueur prend toujours
     * le maximum d'allumettes possible, jusqu'à 3.
     * 
     * @param jeu le jeu en cours
     * @return le nombre d'allumettes à prendre
     */
    @Override
    public int getPrise(Jeu jeu) {        
        int prise;
        prise = Math.min(jeu.getNombreAllumettes(), 3);        
        return prise;
    }
}