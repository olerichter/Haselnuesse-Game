package data.faehigkeiten;

import java.io.Serializable;

/**
 * Ist die Fähigkeit Heilen - Verbraucht eine Buchecker Kann nur vom Spieler
 * erlernt werden
 *
 * @version 0.9
 * @author Ole Richter
 */
public class HeilenBuchecker implements Faehigkeit, Serializable {

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public int getID() {
        return HEILEN_BUCHECKER;
    }

    /**
     * Liefert die Beschreibung - "Heilt" + Verursachte Heilung + Hinweis
     *
     * @return "Heilt" + Verursachte Heilung + Hinweis
     */
    @Override
    public String getBeschreibeung() {
        return "Heilt +20 (-1 Buchecker) ";
    }

    /**
     * Liefert die verursachte Heilung
     *
     * @return die verursachte Heilung
     */
    @Override
    public int getDmg() {
        return 20;
    }

    /**
     * Liefert die verursachte Heilung als Text
     *
     * @return die verursachte Heilung als Text
     */
    @Override
    public String getDmgBeschreibung() {
        return "20";
    }

    /**
     * Level spielt für diese Fähigkeit keine Rolle
     *
     * @return 0
     */
    @Override
    public int getLevel() {
        return 0;
    }

    /**
     * Tut nichts
     */
    @Override
    public void benutzt() {
    }

    /**
     * Liefert die verursachte Heilung als Text
     *
     * @return die verursachte Heilung als Text
     */
    @Override
    public String toString() {
        return "+20";
    }
}
