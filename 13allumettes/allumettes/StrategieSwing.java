package allumettes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StrategieSwing implements Strategie {
    private final Object verrou = new Object();
    private int choix = 0;
    private JFrame frame;
    private JLabel label;
    private JButton[] boutons = new JButton[3];
    private JButton boutonTriche;
    private JTextField tricheField;

    @Override
    public int getPrise(Jeu jeu) throws CoupInvalideException, OperationInterditeException {
        SwingUtilities.invokeLater(() -> {
            try {
                afficherFenetre(jeu);
            } catch (CoupInvalideException | OperationInterditeException e) {
                JOptionPane.showMessageDialog(null, "Erreur : " + e.getMessage());
            }
        });
        synchronized (verrou) {
            while (choix == 0) {
                try {
                    verrou.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        int resultat = choix;
        choix = 0; // reset pour le prochain tour
        SwingUtilities.invokeLater(() -> frame.setVisible(false));
        return resultat;
    }

    private void afficherFenetre(Jeu jeu) throws CoupInvalideException, OperationInterditeException {
        if (frame == null) {
            frame = new JFrame("Allumettes - Joueur Swing");
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setLayout(new FlowLayout());

            label = new JLabel();
            frame.add(label);

            for (int i = 0; i < 3; i++) {
                final int prise = i + 1;
                boutons[i] = new JButton(String.valueOf(prise));
                boutons[i].addActionListener(e -> {
                    synchronized (verrou) {
                        choix = prise;
                        verrou.notify();
                    }
                });
                frame.add(boutons[i]);
            }

            boutonTriche = new JButton("Tricher");
            tricheField = new JTextField("1", 2);
            boutonTriche.addActionListener(e -> {
                int n;
                try {
                    n = Integer.parseInt(tricheField.getText());
                } catch (NumberFormatException ex) {
                    n = 1;
                }
                synchronized (verrou) {
                    if (jeu.getNombreAllumettes() > 1) {
                        try {jeu.retirer(Math.min(n, jeu.getNombreAllumettes() - 1));}
                        catch (CoupInvalideException | OperationInterditeException ex) {
                            JOptionPane.showMessageDialog(frame, "Erreur lors de la triche : " + ex.getMessage());
                            return;
                        }
                        JOptionPane.showMessageDialog(frame, "[Je triche... " + Math.min(n, jeu.getNombreAllumettes() - 1) + " allumettes en moins]");
                    } else {
                        try {
                            jeu.retirer(Integer.MIN_VALUE); // Triche en retirant une allumette même si c'est la dernière
                        } catch (CoupInvalideException | OperationInterditeException ex) {
                            JOptionPane.showMessageDialog(frame, "Erreur lors de la triche : " + ex.getMessage());
                            return;
                        }                        
                        JOptionPane.showMessageDialog(frame, "[Je triche... 1 allumette en plus]");
                    }
                    choix = 0; // On ne joue pas, on attend un vrai choix
                }
            });
            frame.add(boutonTriche);
            frame.add(tricheField);
        }

        label.setText("Allumettes restantes : " + jeu.getNombreAllumettes());
        // Désactive les boutons impossibles
        for (int i = 0; i < 3; i++) {
            boutons[i].setEnabled(jeu.getNombreAllumettes() >= i + 1);
        }
        frame.setVisible(true);
    }
}