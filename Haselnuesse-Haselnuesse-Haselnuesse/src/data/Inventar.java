package data;

import controller.game.trigger.MeldungTrigger;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * Das Inventar repäsentiert den Rucksack des Spielers ab (0.6) Hier drin
 * sammelt der Spieler Gegenstände.
 *
 * es wird hier der Levelstatuscheck aufgerufen
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class Inventar implements Serializable {

    //Inhalt
    private HashMap<Gegenstand, Integer> inventar;
    //Besitzer
    private Spieler spieler;

    /**
     * erstellt einen Leeren Rucksack
     *
     * @param spieler den Besitzer
     */
    public Inventar(Spieler spieler) {
        this.inventar = new HashMap<Gegenstand, Integer>();
        this.spieler = spieler;

    }

    /**
     * lifert den inhalt des Rucksackes, nicht die Menge sondern nur ein
     * Gegenstand pro Art.
     *
     * @return den inhalt des Rucksackes
     */
    public HashSet<Gegenstand> getInventar() {
        return new HashSet<Gegenstand>(inventar.keySet());
    }

    /**
     * fügt dem Rucksack eine anzahl von einem Gegenstand hinzu
     *
     * @param gegenstand den Gegenstand
     * @param anzahl die Anzahl
     */
    public void addGegenstand(Gegenstand gegenstand, int anzahl) {
        if (gegenstand == null) {
            return;
        } else {
            gegenstand.wurdeAufgenommen();
        }
        if (inventar.containsKey(gegenstand)) {
            inventar.put(gegenstand, anzahl + inventar.get(gegenstand));
        } else {
            inventar.put(gegenstand, anzahl);
        }
        if (gegenstand.getArt() == Gegenstand.HASELNUSS) {
            if (!spieler.checkLevelStatus()) {
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.POSITIV, spieler.getLevelStatus());
            }
        }

    }

    /**
     * löscht eine anzahl eines Gegenstandes
     *
     * @param gegenstand zu löschenden Gegenstand
     * @param anzahl zu löschende Anzahl
     */
    public void loescheGegenstand(Gegenstand gegenstand, int anzahl) {
        if (gegenstand == null || !inventar.containsKey(gegenstand)) {
            return;
        }
        if (inventar.get(gegenstand) <= anzahl) {
            inventar.remove(gegenstand);
        } else {
            inventar.put(gegenstand, inventar.get(gegenstand) - anzahl);
        }
    }

    /**
     * liefert die anzahl des übergebenen Gegenstandes im Rucksack zurück
     *
     * @param gegenstand den Gegenstand
     * @return liefert die Anzahl
     */
    public int getGegenstandAnzahl(Gegenstand gegenstand) {
        if (inventar.get(gegenstand) == null) {
            return 0;
        } else {
            return inventar.get(gegenstand).intValue();
        }
    }
}
