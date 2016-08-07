package data.faehigkeiten;

/**
 * (ab V0.6) repräsentiert eine Kampffähigkeit für das Kampfsystem
 *
 * @version 0.8b
 * @author Ole Richter
 */
public interface Faehigkeit {

    /**
     * Konstante welche die Fähigkeit identifiziert
     */
    public static final int BEISEN = 100, KRATZEN = 101, WERFEN = 102, KLAUEN = 300, HEILEN_BUCHECKER = 200, HEILEN_HASELNUSS = 201, HEILEN_WALLNUSS = 202;

    /**
     * Liefert die ID - sie Entspricht der Konstanten in Faehigkeit
     *
     * @return id - siehe Konstanten in im interface Faehigkeit
     */
    public int getID();

    /**
     * Liefert die Beschreibeung der Fähigkeit (Name + Level + Schaden)
     *
     * @return die Beschreibeung der Fähigkeit
     */
    public String getBeschreibeung();

    /**
     * Liefert den veruhrsachten Schaden zurück, hängt vom Level ab
     *
     * @return den veruhrsachten Schaden
     */
    public int getDmg();

    /**
     * Lieferet den Schadensbereich als String zurück: min Schaden - max Schaden
     *
     * @return den Schadensbereich
     */
    public String getDmgBeschreibung();

    /**
     * Liefert den Name + Level
     *
     * @return den Name + Level
     */
    @Override
    public String toString();

    /**
     * leifert das Level der Fähigkeit
     *
     * @return das Level
     */
    public int getLevel();

    /**
     * muss Ausgelöst werden wenn die Fähigkeit benutzt wurde, ist für den
     * Levelaufstieg verantwortlich gibt eine Meldung aus wenn ein neues Level
     * erreicht worden ist.
     */
    public void benutzt();
}
