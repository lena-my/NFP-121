package allumettes;

public class JeuReel implements Jeu {
    private int allumettes;

    /**
    * Créer un jeu réel avec un nombre initial d'allumettes.    
    * @param nbAllumettes nombre d'allumettes initiales (par défaut 13)
    */
    public JeuReel() {
        this.allumettes = 13; // Nombre initial d'allumettes dans le jeu
    }

    @Override
    public int getNombreAllumettes() {
        return allumettes;
    }

    @Override
    public void retirer(int nb) throws CoupInvalideException {
        // Vérifie que le nombre d'allumettes à retirer est valide
        if (nb < 1 || nb > PRISE_MAX || nb > allumettes) {
            throw new CoupInvalideException(nb, "Nombre d'allumettes à retirer invalide : " + nb);
        }
        allumettes -= nb;
    }

    @Override
    public String toString() {
        return "Allumettes restantes : " + allumettes;
    }
}
