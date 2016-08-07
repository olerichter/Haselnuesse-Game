package controller.game;

import controller.db.Speicher;
import controller.dungon.Dungon;
import controller.game.trigger.GameTrigger;
import controller.toolkit.Koordinaten;
import controller.toolkit.Zubehoer;
import controller.view.ViewController;
import data.MapDataFeld;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * Diese Klasse ist für das Speicher- und Lade-Manegement des Spielfeldes
 * zuständig. Da das Spielfeld aus einzelnen Feldobjekten besteht, die
 * untereinander rekursiv verknüpft sind, beschränkt sich der MapDataBeschaffer
 * auf das Laden ab 0.8 auch auf das Abspeichern von einzelnen Feldobjekten.
 * Sowie -falls noch nicht vorhanden- auf deren Gennerierung.
 *
 * Die eigentliche Spielfeldlogik ist in den Feldobjekten gespeichert und wird
 * dort zum Teil rekursiv ausgeführt (see MapDataFeld)
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class MapDataBeschaffer {

    //die Felder in der aktiven Zone
    private HashMap<Koordinaten, MapDataFeld> puffer;
    //VIewCOnroller
    private ViewController viewController;
    //der Mapgennerator für Quadranten gennerierung
    private Dungon dungongen;
    //der Individuuen Conroller
    private MonsterController monster_ctr;
    //der Gegenstand Gennerator
    private GegenstandGenerator item_gen;
    //der Speicher
    private Speicher dB_con;

    /**
     * Erzeugt eine Instanz des MapDataBeschaffers
     *
     * Es es werden alle Hilfsklassen instanziiert: Feld Generierung - Dungon,
     * Item Generierung - GegenstandGennerator, Individuum Generierung und
     * Verwaltung - MonsterController,
     *
     * Und die Datenbank und Buffer (ab 0.8 getrennt) - DBforAlfa
     *
     *
     * @param viewController seinen viewcontroller
     * @param masterTrigger den Trigger des Spiels
     * @param speicher des Speicher (das aktuelle Spiel)
     */
    public MapDataBeschaffer(ViewController viewController, GameTrigger masterTrigger, Speicher speicher) {

        if (viewController != null || masterTrigger != null || speicher != null) {
            this.viewController = viewController;
            dungongen = new Dungon();
            puffer = new HashMap<Koordinaten, MapDataFeld>();
            item_gen = new GegenstandGenerator(this, viewController.getSpieler());
            monster_ctr = new MonsterController(this, item_gen, masterTrigger, viewController.getSpieler());
            masterTrigger.setIndivduen_ctr(monster_ctr);
            dB_con = speicher;

        }
    }

    /**
     * Diese Methode läd ein Feld aus dem Buffer (um Redundanzen zu vermeiden),
     * wenn nicht im Buffer aus der Datenbank (bei erfolgreichem Ladevorgang
     * landet es dann im Buffer), wenn noch nicht in der Datenbank, wird dieses
     * Feld (und sein Quadrant) erzeugt.
     *
     * @param k Koordinaten des gewünschten Feldes
     * @return das Feldobjekt
     */
    public MapDataFeld getMapDataFeld(Koordinaten k) {

        MapDataFeld feld;
        // Das Feld wird aus dem Buffer geladen - TODO Datenbank abfrage

        feld = puffer.get(k);

        if (feld == null && dB_con.feldExistiert(k)) {
            feld = dB_con.feldLaden(k);
            neuesFeld(k, feld);
        }
        //falls das Laden nicht erfolgreich war wird es generiert (der entsprechende Quadrant)
        if (feld == null) {
            //Quadrant wird bestimmt
            Koordinaten quadrant = getQuadrant(k);
            //Quadrant wird generiert - größe 20x20 (Quadrant) und in die Datenbank gesichert
            dungongen.genDungonRek(20, 20, this, quadrant.getX(), quadrant.getY());
            //Individuuen werden erzeugt (Quadrant) und in die Datenbank gesichert
            monster_ctr.monsterAufQuadrantSetzen(quadrant);
            //Gegenstände werden erzeugt (Quadrant) und in die Datenbank gesichert
            item_gen.itemsAufQuadrantsetzen(quadrant);
            pufferAufraeumen();
            //das Feld wird aus der Datenbank geladen
            feld = puffer.get(k);
            if (feld == null) {
                feld = dB_con.feldLaden(k);
                neuesFeld(k, feld);
            }


        }
        //if (feld == null) throw new FeldnotLoadedExeption();
        monster_ctr.individuumAktivieren(feld.getIndividuum());

        // der Beschaffer wird gesetzt
        // der zuständige ViewController wird gesetzt

        return feld;


    }

    /**
     * ab 0.8 diese Methode ist für das Abspeichern in die Datenbank zuständig
     * es entfernt das Feld aus dem Buffer - und somit wird das Feld konserviert
     * mitsamt Individuuen und Gegenständen - welche auch aus ihren Buffern
     * entfernt werden
     *
     *
     * diese Methode wird nur vom Feld aufgerufen, wenn es sich ausserhalb der
     * aktiven Zone befindet
     *
     * @param k die Koordinaten des Feldes das deaktiviert werden soll
     */
    public void saveMe(Koordinaten k) {

        MapDataFeld feld = puffer.remove(k);

        feldspeichern(feld);


    }

    /**
     * der Quadrant wird durch schrittweises überprüfen der Ringe um den
     * Ursprung bestimmt - optionale TODO Effizienssteigerung durch Modulo
     * berechnung des Quadranten
     *
     * @param k die Kordinaten eines Feldes
     * @return Diese Hilfsmethode bestimmt, in welchem Quadranten sich die
     * angegebene Koordinate befindet. Sie liefert die linke-oberste-Koordinate
     * des Quadranten zurück
     *
     * Pro Quadrant in x Richtung wird ein Quadrant +4 felder in y-Richtung
     * versetzt
     *
     */
    public Koordinaten getQuadrant(Koordinaten k) {
        //temporäre variablen zur bewrechnung des Quadranten
        int ring = 1;
        int pos = 1;
        int richtung = 0;
        int quadrant_x = 0;
        int quadrant_y = 0;
        int maxpos;

        /* wird so lange aus geführt bis die Koordinate im Quadranten liegt -
         * 
         * dabei wird vom Innersten (Start Quadranten) spiralförmig nach außen gewandert, 
         * ist ein Ring abgeschlossen, wird durch einen Schritt nach rechts in den nächsten Ring geschritten
         */
        while (!(!(k.getX() < quadrant_x || k.getX() >= quadrant_x + 20) && !(k.getY() < quadrant_y || k.getY() >= quadrant_y + 20))) {
            //maximal positionen im ring
            maxpos = (int) (Math.pow(Zubehoer.ungeradeZahl(ring), 2) - Math.pow(Zubehoer.ungeradeZahl(ring - 1), 2));
            //Ring abgeschlossen - ein schritt nachrechts in den nächsten ring
            if (pos + 1 > maxpos) {
                ring++;
                pos = 1;
                richtung = 0;
                quadrant_x -= 20;
                quadrant_y -= 4;
            } //Ring noch nicht abgeschlossen 
            else {
                //es wird geprüft, ob wir an einer ecke im Ring angekommen sind - dann wird die Richtung um 90° gedreht
                if (pos % (Zubehoer.ungeradeZahl(ring) - 1) == 0) {
                    richtung++;
                }
                //es wird ein Schritt in die angegebene Richtung gegangen
                switch (richtung) {
                    case 0:
                        quadrant_y += 20;
                        pos++;
                        break;
                    case 1:
                        quadrant_x += 20;
                        quadrant_y += 4;
                        pos++;
                        break;
                    case 2:
                        quadrant_y -= 20;
                        pos++;
                        break;
                    case 3:
                        quadrant_x -= 20;
                        quadrant_y -= 4;
                        pos++;
                        break;
                }
            }
        }

        return new Koordinaten(quadrant_x, quadrant_y);
    }

    /**
     * Räumt den aktiven Puffer auf - entfernt alle Felder außerhalb der
     * aktivien Zone.
     */
    private void pufferAufraeumen() {


        Iterator<Koordinaten> it = puffer.keySet().iterator();
        while (it.hasNext()) {
            Koordinaten koordinaten = it.next();
            if (!viewController.inAktiverZone(koordinaten)) {
                feldspeichern(puffer.get(koordinaten));
                it.remove();
            }
        }

    }

    /**
     * nimmt ein inaktives Feld, aktiviert es und fügt es dem aktiven Puffer
     * hinzu
     *
     * @param k koordinaten des Feldes
     * @param mapDataFeld das Feld
     */
    public void neuesFeld(Koordinaten k, MapDataFeld mapDataFeld) {

        mapDataFeld.setLoader(this);
        mapDataFeld.setView(viewController);
        puffer.put(k, mapDataFeld);

    }

    /**
     * nimmt ein aktives Feld enfernt es aus dem Puffer und speichert es in die
     * Datenbank
     *
     * @param feld das zu speichernde Feld
     */
    private void feldspeichern(MapDataFeld feld) {

        if (feld.getIndividuum() != null) {
            monster_ctr.individuumDeaktivieren(feld.getIndividuum());
        }
        dB_con.feldSpeichern(feld);

    }

    /**
     * Deaktiviert (bei true) die aktive Zone, aktiviert sie bei false
     *
     * @param speichern true für Speichern, false für spielen
     */
    public void aktiviereSpeichern(boolean speichern) {
        if (speichern) {
            viewController.AktiveZoneAus(true);
            pufferAufraeumen();
        } else {
            viewController.AktiveZoneAus(false);
        }

    }

    /**
     * liefert den IndividuuenConroller
     *
     * @return den IndividuuenConroller
     */
    public MonsterController getIndividuenController() {
        return monster_ctr;
    }

    /**
     * Liefert die Position des Spielers
     *
     * @return die Position des Spielers
     */
    public Koordinaten getSpielerPosition() {
        return viewController.getSpieler().getPosition().getKoordinaten();
    }
}
