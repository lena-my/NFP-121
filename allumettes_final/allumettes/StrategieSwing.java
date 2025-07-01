package allumettes;

import swing.InterfaceSwing;

public class StrategieSwing implements Strategie {
    private final Object verrou = new Object();
    private InterfaceSwing frame;
    private String nomJoueur;

    public StrategieSwing(String nomJoueur) {
        this.nomJoueur = nomJoueur;
    }

    @Override
    public int getPrise(Jeu jeu) {
        if (frame == null) {
            // Si la fenêtre n'existe pas, on la crée avec le nom du joueur
            // Le verrou est utilisé pour synchroniser l'accès à la fenêtre entre les threads
            frame = new InterfaceSwing(nomJoueur, jeu, verrou);
        } else {
            // Si la fenêtre existe déjà, on met à jour le nombre d'allumettes restantes
            frame.mettreAJourBoutons();
        }
        frame.setLocationRelativeTo(null); // Centrer la fenetre dans l'écran
        return frame.attendreChoix();
    }

    public void fermerFenetre() {
        if (frame != null) {
            frame.terminer(); // Fecha a janela Swing corretamente
        }
    }
}