package data.faehigkeiten;

import java.io.Serializable;

/**
 * Ist die Fähigkeit Klauen - klaut dem Spieler einen Gegenstand Kann nicht vom
 * Spieler erlernt werden
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Klauen implements Faehigkeit, Serializable {

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public int getID() {
        return KLAUEN;
    }

    /**
     * Beschreibt die Fähigkeit
     *
     * @return "Klaut einen Gegenstand"
     */
    @Override
    public String getBeschreibeung() {
        return "Klaut einen Gegenstand";
    }

    /**
     * Liefert 0 zurück, da kein Schaden verursacht wird
     *
     * @return 0;
     */
    @Override
    public int getDmg() {
        return 0;
    }

    /**
     * Beschreibt die ausgefürte Fähigkeit
     *
     * @return "1 Gegenstand geklaut"
     */
    @Override
    public String getDmgBeschreibung() {
        return "1 Gegenstand geklaut";
    }

    /**
     * Level spielt keine Rolle für diese Fähigkeit
     *
     * @return 0
     */
    @Override
    public int getLevel() {
        return 0;
    }

    /**
     * Tut nichts.
     */
    @Override
    public void benutzt() {
    }

    /**
     * Liefert getDmgBeschreibung()
     *
     * @return getDmgBeschreibung()
     */
    @Override
    public String toString() {
        return getDmgBeschreibung();
    }
}
