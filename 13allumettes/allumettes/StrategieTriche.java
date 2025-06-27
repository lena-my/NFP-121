package allumettes;

public class StrategieTriche implements Strategie {
    /**
     * Cette stratégie retire les allumettes une par une jusqu'à ce qu'il n'en reste que 2.
     *
     * @param jeu le jeu en cours
     * @return le nombre d'allumettes à prendre (toujours 1 pour tricher)
     */

    @Override    
    public int getPrise(Jeu jeu) throws CoupInvalideException, OperationInterditeException {
        System.out.println("[Je triche...]");
        // On retire les allumettes une par une jusqu'à ce qu'il n'en reste que 2.
        while (jeu.getNombreAllumettes() > 2) {               
            // Si le nombre d'allumettes est supérieur à 2, on triche
            // et on retire 1 allumette jusqu'à ce qu'il n'en reste que 2.
            jeu.retirer(1);
        }        
        //  A ce stade, il ne reste que 2 allumettes et on prends 1 pour gagner.
        System.out.println("[" + jeu.toString() + "]");
        return 1;
    }
}
