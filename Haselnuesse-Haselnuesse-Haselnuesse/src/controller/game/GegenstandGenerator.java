package controller.game;

import controller.toolkit.Koordinaten;
import data.Gegenstand;
import data.MapDataFeld;
import data.Spieler;

/**
 * Die Klasse GegenstandGenerator ist für die Erstellung von Gegenständen auf
 * der Map zuständig.
 *
 * sie generiert items auf einen Quadranten(v0.4)
 *
 * @version 0.4
 * @author Ole Richter
 */
public class GegenstandGenerator {

    private MapDataBeschaffer loader;
    private Spieler spieler;

    /**
     * Bereitet den Gennerator vor
     * @param loader  Spielfeld-Controller
     * @param spieler der Spieler
     */
    public GegenstandGenerator(MapDataBeschaffer loader, Spieler spieler) {
        this.loader = loader;
        this.spieler = spieler;
    }

    /**
     * die Methode verteilt eine begrenzte zufällige Anzahl von Gegenständen
     * (max 8) auf den Quadranten
     *
     *
     * @param k die Koordinaten eines Feldes im Quadrenten
     */
    void itemsAufQuadrantsetzen(Koordinaten k) {
        k = loader.getQuadrant(k);

        MapDataFeld feld;
        //zufällige Anzahl der Gegenstände
        int anzahlItems = 1 + (int) (Math.random() * 8);
        //die Gegenstände auf der Karte verteilen
        for (int i = 0; i < anzahlItems; i++) {
            //so lange neue Koordinaten generieren bis sie noch nicht besetzt sind und begehbar sind
            do {
                feld = loader.getMapDataFeld(new Koordinaten(k.getX() + (int) (19 * Math.random()), k.getY() + (int) (19 * Math.random())));
            } while (!feld.getBeschaffenheit().getBegehbar() || feld.getGegenstand() != null);
            //genenstand generieren
            Gegenstand item = Gegenstand.randomGegenstand(spieler.getLevel());
            feld.setGegenstand(item);


        }



    }
}
