package data;

import java.io.Serializable;

/**
 * Dies Repräsentiert das Datenobjekt eines Gegenstandes mit Name, Art, und
 * Position.
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Gegenstand implements Serializable {

    /**
     * Definert die Art der Gegenstandes
     */
    public static final int HASELNUSS = 10, UNREIFE_KASTANIE = 11, BUCHEKERN = 12, MANDEL = 13, WALLNUSS = 14;

    /**
     * Liefert einen zufälligen Gegenstand des Spiel-Levels zurück
     *
     * @param spielLevel das aktuelle Spiel-Level
     * @return den zufälligen Gegenstand
     */
    public static Gegenstand randomGegenstand(int spielLevel) {
        double haselnuss, kastanie = 0, mandel = 0, wallnuss = 0;
        double rand = Math.random();
        switch (spielLevel) {
            case 1:
            case 2:
                haselnuss = 0.3;
                break;
            case 3:
                haselnuss = 0.2;
                mandel = 0.3;

                break;
            case 4:
                haselnuss = 0.2;
                mandel = 0.3;
                kastanie = 0.3;


                break;
            case 5:
            default:
                haselnuss = 0.2;
                mandel = 0.2;
                kastanie = 0.2;
                wallnuss = 0.2;

        }
        if (rand < haselnuss) {
            return new Gegenstand(HASELNUSS);
        } else if (rand - haselnuss < mandel) {
            return new Gegenstand(MANDEL);
        } else if (rand - haselnuss - mandel < kastanie) {
            return new Gegenstand(UNREIFE_KASTANIE);
        } else if (rand - haselnuss - mandel - kastanie < wallnuss) {
            return new Gegenstand(WALLNUSS);
        } else {
            return new Gegenstand(BUCHEKERN);
        }

    }
    /**
     * das Feld auf dem der Gegenstand sitzt
     */
    protected MapDataFeld position;
    /**
     * die Art des Gegenstandes - siehe Konstanten in dieser Klasse
     */
    protected int art;

    /**
     * Instanziert den Gegenstand
     *
     * @param art die Art des Gegenstandes - Konstanten siehe diese Klasse
     */
    public Gegenstand(int art) {

        this.art = art;
    }

    /**
     * Gibt die Art des Gegenstandes zurück - siehe Konstanten in dieser Klasse
     *
     * @return eine Art-Konstante (dieser Klasse)
     */
    public int getArt() {
        return art;
    }

    /**
     * Gibt den Namen zurück
     *
     * @return den Namen
     */
    public String getName() {

        switch (art) {
            case BUCHEKERN:
                return "Buchecker";
            case HASELNUSS:
                return "Haselnuss";
            case UNREIFE_KASTANIE:
                return "unreife Kastanie";
            case MANDEL:
                return "Mandel";
            case WALLNUSS:
                return "Wallnuss";
            default:
                return "Unbekannter Gegenstand";
        }
    }

    /**
     * ein equals für HashMap /HashSet
     *
     * @return Hash
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.art;
        return hash;
    }

    /**
     * Überprüft ob die Art der Gegenstände gleich ist
     *
     * @param obj der andere Gegenstand
     * @return true wenn die Art der Gegenstände gleich ist
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Gegenstand other = (Gegenstand) obj;
        if (this.art != other.art) {
            return false;
        }
        return true;
    }

    /**
     * löscht sich von der Position und die Position
     */
    public void wurdeAufgenommen() {
        if (position != null) {
            position.removeGegenstand(this);
            position = null;
        }

    }

    /**
     * Setzt die neue Position
     *
     * @param aThis die Position
     */
    void setPosition(MapDataFeld aThis) {
        position = aThis;
    }
}
