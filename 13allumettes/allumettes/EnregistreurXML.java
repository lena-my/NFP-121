package allumettes;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe pour enregistrer le déroulement d'une partie d'allumettes en XML.
 * Utilise les classes Java standard pour générer un document XML valide selon la DTD deroulement.dtd.
 */
public class EnregistreurXML {
    private StringBuilder xmlContent;
    private List<Coup> coups;
    private Joueur joueur1;
    private Joueur joueur2;
    private boolean partieTerminee;
    private String nomTricheur;
    private String datePartie;

    /**
     * Classe interne pour représenter un coup joué.
     */
    private static class Coup {
        int numero;
        String joueur;
        int prise;
        int allumettesRestantes;

        Coup(int numero, String joueur, int prise, int allumettesRestantes) {
            this.numero = numero;
            this.joueur = joueur;
            this.prise = prise;
            this.allumettesRestantes = allumettesRestantes;
        }
    }

    /**
     * Constructeur de l'enregistreur XML.
     */
    public EnregistreurXML() {
        this.xmlContent = new StringBuilder();
        this.coups = new ArrayList<>();
        this.partieTerminee = false;
        this.datePartie = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Démarre l'enregistrement d'une nouvelle partie.
     * @param j1 le premier joueur
     * @param j2 le deuxième joueur
     */
    public void demarrerPartie(Joueur j1, Joueur j2) {
        this.joueur1 = j1;
        this.joueur2 = j2;
        this.coups.clear();
        this.partieTerminee = false;
        this.nomTricheur = null;

        // Initialiser le contenu XML
        xmlContent.setLength(0);
        xmlContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xmlContent.append("<!DOCTYPE deroulement SYSTEM \"deroulement.dtd\">\n");
        xmlContent.append("<deroulement>\n");
        xmlContent.append("  <partie date=\"").append(datePartie).append("\">\n");
        
        // Ajouter les informations des joueurs
        xmlContent.append("    <joueur1 nom=\"").append(escapeXml(j1.getNom())).append("\" ");
        xmlContent.append("strategie=\"").append(getNomStrategie(j1.strategie)).append("\"/>\n");
        
        xmlContent.append("    <joueur2 nom=\"").append(escapeXml(j2.getNom())).append("\" ");
        xmlContent.append("strategie=\"").append(getNomStrategie(j2.strategie)).append("\"/>\n");
        
        xmlContent.append("    <coups>\n");
    }

    /**
     * Enregistre un coup joué.
     * @param numeroCoup le numéro du coup
     * @param nomJoueur le nom du joueur
     * @param prise le nombre d'allumettes prises
     * @param allumettesRestantes le nombre d'allumettes restantes après le coup
     */
    public void enregistrerCoup(int numeroCoup, String nomJoueur, int prise, int allumettesRestantes) {
        if (!partieTerminee) {
            Coup coup = new Coup(numeroCoup, nomJoueur, prise, allumettesRestantes);
            this.coups.add(coup);

            xmlContent.append("      <coup numero=\"").append(numeroCoup).append("\" ");
            xmlContent.append("joueur=\"").append(escapeXml(nomJoueur)).append("\" ");
            xmlContent.append("prise=\"").append(prise).append("\" ");
            xmlContent.append("allumettes_restantes=\"").append(allumettesRestantes).append("\"/>\n");
        }
    }

    /**
     * Termine la partie avec un gagnant.
     * @param nomGagnant le nom du joueur gagnant
     */
    public void terminerPartieAvecGagnant(String nomGagnant) {
        if (!partieTerminee) {
            xmlContent.append("    </coups>\n");
            xmlContent.append("    <resultat>\n");
            xmlContent.append("      <gagnant nom=\"").append(escapeXml(nomGagnant)).append("\"/>\n");
            xmlContent.append("    </resultat>\n");
            xmlContent.append("  </partie>\n");
            xmlContent.append("</deroulement>");
            this.partieTerminee = true;
        }
    }

    /**
     * Termine la partie avec un tricheur.
     * @param nomTricheur le nom du joueur qui a triché
     */
    public void terminerPartieAvecTricheur(String nomTricheur) {
        if (!partieTerminee) {
            this.nomTricheur = nomTricheur;
            xmlContent.append("    </coups>\n");
            xmlContent.append("    <resultat>\n");
            xmlContent.append("      <tricheur nom=\"").append(escapeXml(nomTricheur)).append("\"/>\n");
            xmlContent.append("    </resultat>\n");
            xmlContent.append("  </partie>\n");
            xmlContent.append("</deroulement>");
            this.partieTerminee = true;
        }
    }

    /**
     * Sauvegarde le document XML dans un fichier.
     * @param nomFichier le nom du fichier de sortie
     * @throws IOException en cas d'erreur d'écriture
     */
    public void sauvegarder(String nomFichier) throws IOException {
        try (FileWriter writer = new FileWriter(nomFichier)) {
            writer.write(xmlContent.toString());
        }
    }

    /**
     * Échappe les caractères spéciaux XML.
     * @param text le texte à échapper
     * @return le texte échappé
     */
    private String escapeXml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&apos;");
    }

    /**
     * Obtient le nom de la stratégie d'un joueur.
     * @param strategie la stratégie du joueur
     * @return le nom de la stratégie
     */
    private String getNomStrategie(Strategie strategie) {
        if (strategie instanceof StrategieNaif) {
            return "naif";
        } else if (strategie instanceof StrategieRapide) {
            return "rapide";
        } else if (strategie instanceof StrategieExpert) {
            return "expert";
        } else if (strategie instanceof StrategieHumain) {
            return "humain";
        } else if (strategie instanceof StrategieTriche) {
            return "tricheur";
        } else if (strategie instanceof StrategieLente) {
            return "lente";
        } else if (strategie instanceof StrategieSwing) {
            return "swing";
        } else {
            return "inconnue";
        }
    }

    /**
     * Obtient le contenu XML généré.
     * @return le contenu XML
     */
    public String getXmlContent() {
        return xmlContent.toString();
    }
} 