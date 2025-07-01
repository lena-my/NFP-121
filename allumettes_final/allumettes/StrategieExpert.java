package allumettes;

public class StrategieExpert implements Strategie {
    /**
     * Cette stratégie est basée sur la théorie du jeu de Nim.
     * Elle calcule le coup optimal en fonction du nombre d'allumettes restantes.
     * 
     * @param jeu le jeu en cours
     * @return le nombre d'allumettes à prendre
     */
    
    @Override
    public int getPrise(Jeu jeu) {
        int allumettes = jeu.getNombreAllumettes(); // Nombre d’allumettes restantes
        int max = jeu.PRISE_MAX; // Nombre maximum d’allumettes que l’on peut prendre
        int prise = (allumettes - 1) % (max + 1); // Calcul du coup optimal selon la stratégie de Nim

        // Si le résultat est 0 ou dépasse la limite, on prend le maximum autorisé ou le nombre restant
        if (prise == 0 || prise > max) {
            prise = Math.min(max, allumettes); // On s’assure de ne pas dépasser les règles du jeu
        }

        return prise; // Retourne le nombre d’allumettes à prendre
    }
}
