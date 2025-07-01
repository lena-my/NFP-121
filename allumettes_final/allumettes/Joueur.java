package allumettes;

/** La classe Joueur modélise un joueur. Un joueur a un nom.
 * Il a aussi une stratégie qui lui permet de décider
 * combien d'allumettes il va prendre à chaque tour.
 */
public class Joueur {

    /** Nom du joueur. */
    private String nom;

    /** Stratégie du joueur. */
    public Strategie strategie;

    /** Constructeur du joueur.
     * @param nom le nom du joueur
     * @param strategie la stratégie du joueur
     */
    public Joueur(String nom, Strategie strategie) {
        this.nom = nom;
        this.strategie = strategie;
    }

    /** Obtenir le nom du joueur.
     * @return le nom du joueur
     */
    public String getNom() {
        return nom;
    }

    /** Obtenir le nombre d'allumettes que le joueur veut prendre.
     * @param jeu le jeu en cours
     * @return le nombre d'allumettes à prendre
     */
    public int getPrise(Jeu jeu) throws CoupInvalideException, OperationInterditeException {
        // Utiliser la stratégie du joueur pour obtenir le nombre d'allumettes à prendre
        int prise = strategie.getPrise(jeu);
        // Déclarer la prise d'allumettes dans la console
        declarerLaPrise(prise);
        // returner le nombre d'allumettes à prendre au arbitre
        return prise;
    }

    /** Déclare la prise d'allumettes du joueur.
     * @param prise le nombre d'allumettes prises
     */
    public void declarerLaPrise(int prise) {
        if (prise == 1 || prise == 0 || prise == -1) {
            // singulier (une allumette, 0 ou -1 allumette)
            System.out.println(this.nom + " prend " + prise + " allumette.");
        } else {
            // texte pluriel (plusieurs allumettes)
            System.out.println(this.nom + " prend " + prise + " allumettes.");
        }
    }
}


