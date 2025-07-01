package swing;

import javax.swing.*;

import allumettes.Jeu;

import java.awt.*;
import java.util.stream.IntStream;

public class InterfaceSwing extends JFrame {
    private int allumettesRestantes = 13;                 // quantité d'allumettes restantes
    private Jeu jeu;
    private final JLabel compteurLbl = new JLabel();
    private final JButton tricheBtn = new JButton("tricher");
    private final JTextField tricheTexte = new JTextField(3);
    private final JButton[] getAllumetteBtn = new JButton[3];
    private int choix = 0;
    private final Object verrou;

    // constructeur pour (String, int, Object)
    // Permet de créer l'interface avec un nombre d'allumettes initial et un verrou
    // pour la synchronisation entre les threads
    public InterfaceSwing(String nomJoueur, Jeu jeu, Object verrou) {
        super(nomJoueur);
        this.verrou = verrou;
        this.jeu = jeu; // référence au jeu pour pouvoir tricher
        this.allumettesRestantes = jeu.getNombreAllumettes(); // initialise le nombre d'allumettes restantes
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        JPanel triche = new JPanel(new FlowLayout(FlowLayout.LEFT));
        triche.add(tricheBtn);
        triche.add(tricheTexte);
        add(triche, BorderLayout.NORTH);

        compteurLbl.setHorizontalAlignment(SwingConstants.CENTER);
        compteurLbl.setFont(compteurLbl.getFont().deriveFont(26f));
        add(compteurLbl, BorderLayout.CENTER);

        JPanel picks = new JPanel(new FlowLayout());
        IntStream.rangeClosed(1, 3).forEach(i -> {
            getAllumetteBtn[i - 1] = new JButton(String.valueOf(i));
            int value = i;
            getAllumetteBtn[i - 1].addActionListener(e -> {
                synchronized (verrou) {
                    choix = value;
                    verrou.notify();
                }
            });
            picks.add(getAllumetteBtn[i - 1]);
        });
        add(picks, BorderLayout.SOUTH);

        tricheBtn.addActionListener(e -> triche());

        pack();
        setLocationRelativeTo(null);
        mettreAJourBoutons();
        setVisible(false);
    }

    // methode pour mettre à jour le nombre d'allumettes restantes
    public void setAllumettesRestantes(int n) {
        this.allumettesRestantes = n;
        mettreAJourBoutons();
    }

    // attendreChoix() est une méthode bloquante qui attend que l'utilisateur
    // fasse un choix en cliquant sur un des boutons d'allumettes.
    // Elle utilise un verrou pour synchroniser l'accès entre les threads.
    // Elle retourne le choix de l'utilisateur (1, 2 ou 3) ou 0 si l'utilisateur n'a pas fait de choix.
    // Le titre de la fenêtre est mis à jour pour s'assurer qu'il est visible.
    public int attendreChoix() {
        choix = 0;
        setVisible(true);
        setTitle(getTitle()); // Garante que o título seja exibido
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Permite fechar/minimizar
        synchronized (verrou) {
            while (choix == 0) {
                try {
                    verrou.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        setVisible(false);
        return choix;
    }

    // Triche: permet au joueur de retirer un nombre d'allumettes spécifié dans le champ texte
    private void triche() {
        try {
            int n = Integer.parseInt(tricheTexte.getText());
            this.jeu.retirer(n); // Retire n allumettes du jeu
            setAllumettesRestantes(jeu.getNombreAllumettes());
            tricheTexte.setText("");
        } catch (NumberFormatException ignored) { // Si le texte n'est pas un nombre, on ignore l'erreur
        } catch (allumettes.OperationInterditeException e) {
            // Si une opération interdite est détectée, on affiche un message d'erreur
            // Cela peut être dû à une tentative de tricherie ou à une opération non permise
            // comme retirer plus d'allumettes que disponibles
            JOptionPane.showMessageDialog(this, "Triche détectée : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (allumettes.CoupInvalideException e) {
            // Si le coup est invalide, on affiche un message d'erreur
            // Cela peut être dû à une tentative de retirer un nombre d'allumettes invalide
            JOptionPane.showMessageDialog(this, "Coup invalide : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void mettreAJourBoutons() {
        compteurLbl.setText(String.valueOf(allumettesRestantes));
        for (int i = 0; i < 3; i++)
            getAllumetteBtn[i].setEnabled(allumettesRestantes >= i + 1);
        if (allumettesRestantes <= 0) dispose();
    }

    /* ---------- contrôle depuis l'extérieur ---------- */
    public void afficheTour() { setVisible(true); }
    public void cacherTour() { setVisible(false); }

    // Método para encerrar a janela e o thread Swing ao final do jogo
    public void terminer() {
        dispose();
    }
}
