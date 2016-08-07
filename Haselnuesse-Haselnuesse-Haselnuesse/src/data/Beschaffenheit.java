package data;

import java.io.Serializable;

/**
 * Die Beschaffenheit repräsentiert den Boden eines Feldes, und bei der
 * Generierung die des ganzen Raumes (bei Räumen nur Oberbeschaffenheiten =
 * Klassen -> art % 10 = 0)
 *
 *
 * @version 0.5a
 * @author Ole Richter
 */
public class Beschaffenheit implements Serializable {

    /**
     * Klasse Gras
     */
    public static final int GRAS = 1,
            /**
             * Feld der Klasse Gras
             */
            GRAS_VIELE_GRASBUESCHEL = 14, GRAS_BAUMSTAMM = 11, GRAS_EIN_GRASBUESCHEL = 12, GRAS_BLUMEN = 13, GRAS_LEER = 10, GRAS_0 = 19;
    /**
     * Die Klasse Wasser
     */
    public static final int WASSER = 2,
            /**
             * Feld der Klasse Wasser
             */
            WASSER_1 = 20, WASSER_2 = 21, WASSER_3 = 22, WASSER_4 = 23, WASSER_5 = 24;
    /**
     * Ufer - Unterklasse von Wasser
     */
    public static final int UFER = 3,
            /**
             * Feld der Klasse Ufer - Unterklasse von Wasser
             */
            UFER_PFUETZE = 30, UFER_SCHILFDREICK = 31, UFER_PFUETZE_SCHILFUNTEN = 33, UFER_PFUETZE_SCHILFOBEN = 32, UFER_SCHILFRECHTS = 34;
    /**
     * Klasse Wald
     */
    public static final int WALD = 4,
            /**
             * Feld der Klasse Wald
             */
            WALD_1 = 40, WALD_LICHTUNG = 41, WALD_DUNKEL = 42, WALD_VERTROCKNET = 43, WALD_2 = 44;
    /**
     * Gebüsch - Unterklasse von Wald
     */
    public static final int GEBUESCH = 5,
            /**
             * Feld der Klasse Gebüsch - Unterklasse von Wald
             */
            GEBUESCH_3BUESCHE = 50, GEBUESCH_BAUMSTAMM = 51, GEBUESCH_BUSCH = 52, GEBUESCH_2BUESCHE = 53, GEBUSCH_VERTROCKNET = 54;
    /**
     * Klasse Berg
     */
    public static final int BERG = 6,
            /**
             * Feld der Klasse Berg
             */
            BERG_1 = 60, BERG_2 = 61, BERG_3 = 62, BERG_4 = 63, BERG_GRUENESTAL = 64;
    /**
     * Geröll - Unterklasse von Berg
     */
    public static final int GEROELL = 7,
            /**
             * Feld der Klasse Geröll - Unterklasse von Berg
             */
            GEROELL_1 = 70, GEROELL_2 = 71, GEROELL_3 = 72, GEROELL_4 = 73, GEROELL_5 = 74;
    private int beschaffenheit;

    /**
     * erstellt eine Instanz mit der entsprechenden Beschaffenheit
     *
     * @param beschaffenheit
     */
    public Beschaffenheit(int beschaffenheit) {
        this.beschaffenheit = beschaffenheit;
    }

    /**
     * liefert die aktuelle Beschaffenheitskonsante zurück
     *
     * @return die Beschffenheitskonstante - Konstanten siehe diese Klasse
     */
    public int getBeschaffenheit() {
        return beschaffenheit;
    }

    /**
     * Liefert zurück, ob die aktuelle Beschaffenheit begehbar ist
     *
     * @return true wenn begehbar
     */
    public boolean getBegehbar() {
        int art = beschaffenheit / 10;
        if (art == WASSER || art == BERG) {
            return false;
        } else {
            return true;
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * gibt Variationen der aktuellen Beschaffenheitsklasse für den Rand zurück
     *
     * @return variation der aktuellen Beschaffenheitsklasse für den Rand
     */
    public int getIntRandRandom() {

        int art, unterart = 0;
        switch (beschaffenheit) {
            case BERG:
                art = GEROELL;
                break;
            case WALD:
                art = GEBUESCH;
                break;
            case WASSER:
                art = UFER;
                break;
            default:
                art = GRAS;
        }
        if (!(art == GRAS && Math.random() < 0.95)) {

            unterart = (int) (Math.random() * 5);
            if (unterart >= 5) {
                unterart = 4;
            }
        }

        return (art * 10) + unterart;
    }

    /**
     * gibt Variationen der aktuellen Beschaffenheitsklasse zurück
     *
     * @return variation der aktuellen Beschaffenheitsklasse
     */
    public int getIntBeschaffenheitRandom() {

        int art = beschaffenheit / 10;
        int unterart = 0;
        if (art == 0) {
            art = beschaffenheit;
        }
        if (!(art == GRAS && Math.random() < 0.4)) {

            unterart = (int) (Math.random() * 5);
            if (unterart >= 5) {
                unterart = 4;
            }
        }
        return (art * 10) + unterart;
    }

    /**
     * Liefert eine zufällige BeschaffenheitsKlasse zurück zu: 30% Gras, 25%
     * Wald, 25% Berg, 20% Wasser.
     *
     * @return eine zufällige BeschaffenheitsKlasse
     */
    public static Beschaffenheit randomBeschaffenheit() {

        double rand = Math.random();
        int besch;
        if (rand < 0.40f) {
            besch = GRAS;
        } else if (rand < 0.70f) {
            besch = WALD;
        } else if (rand < 0.85f) {
            besch = BERG;
        } else {
            besch = WASSER;
        }

        return new Beschaffenheit(besch);

    }
}
