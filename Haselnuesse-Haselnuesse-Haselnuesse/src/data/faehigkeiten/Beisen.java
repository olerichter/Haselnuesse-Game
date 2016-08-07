package data.faehigkeiten;

import controller.game.trigger.MeldungTrigger;
import java.io.Serializable;

/**
 * Beisen ist die Attacke Beißen
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class Beisen implements Faehigkeit, Serializable {

    private int level;
    private int uebung;
    private boolean spieler;

    /**
     * Erstellt eine Instanz von Beißen
     *
     * @param level das Startlevel der Fähigkeit
     * @param spieler ob die fähigkeit dem Spieler gehört
     *
     */
    public Beisen(int level, boolean spieler) {
        this.level = level;
        this.spieler = spieler;
        uebung = 0;
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public int getID() {
        return BEISEN;
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public String getBeschreibeung() {
        return toString() + "(" + getDmgBeschreibung() + ")";
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public int getDmg() {
        return 5 + 2 * level + (int) ((3 + 2 * level) * Math.random());
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public String getDmgBeschreibung() {
        return (5 + 2 * level) + "-" + ((3 + 2 * level) + (5 + 2 * level));
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public String toString() {
        return "Beißen " + level + " ";
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public int getLevel() {
        return level;
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konforn
    @Override
    public void benutzt() {
        uebung += 5 + (int) (5 * Math.random());
        if (uebung > 100 * level) {
            uebung -= 100 * level;
            level++;
            if (spieler) {
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.POSITIV, "Fähigkeit verbessert: " + toString());
            } else {
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.NEGATIV, "Gegner hat Fähigkeit verbessert: " + toString());
            }
        }
    }
}
