package allumettes;

public class JeuSimple implements Jeu {
    private int allumettes;

    public JeuSimple(int allumettes) {
        this.allumettes = allumettes;
    }

    @Override
    public int getNombreAllumettes() {
        return allumettes;
    }

    @Override
    public void retirer(int nb) {
        allumettes -= nb;
    }
}