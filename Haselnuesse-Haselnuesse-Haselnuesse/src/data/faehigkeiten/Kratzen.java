package data.faehigkeiten;

import controller.game.trigger.MeldungTrigger;
import java.io.Serializable;

/**
 * REpäsentiert die fähigkeit Kratzen
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class Kratzen implements Faehigkeit, Serializable {

    private int level;
    private int uebung;
    private boolean spieler;

    /**
     * Erstellt eine Instanz von Kratzen
     *
     * @param level das start Level der Fähigkeit
     * @param spieler ob es den Spieler gehört
     */
    public Kratzen(int level, boolean spieler) {
        this.level = level;
        uebung = 0;
        this.spieler = spieler;
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public int getID() {
        return KRATZEN;
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public String getBeschreibeung() {
        return toString() + "(" + getDmgBeschreibung() + ")";
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public int getDmg() {
        return 2 + 2 * level + (int) ((10 + 3 * level) * Math.random());
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public String getDmgBeschreibung() {
        return (2 + 2 * level) + "-" + ((10 + 3 * level) + (2 + 2 * level));
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public String toString() {
        return "Kratzen " + level + " ";
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public int getLevel() {
        return level;
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
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
