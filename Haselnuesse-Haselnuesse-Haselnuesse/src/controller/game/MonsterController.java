package controller.game;

import controller.game.trigger.GameTrigger;
import controller.game.trigger.IndividuenTrigger;
import controller.toolkit.Koordinaten;
import data.Individuum;
import data.MapDataFeld;
import data.Spieler;
import data.individuen.Elster;
import data.individuen.Fuchs;
import data.individuen.Mader;
import data.individuen.SchwarzesEichhoernchen;
import data.individuen.Streifenhoernchen;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Der MonsterController ist für die aktiven Monster innerhalb der aktiven Zone
 * zuständig. Ausserdem generiert er für neue Quadranten Monster. ab Version 0.7
 * wird er für die Bewegung der Monster zuständig sein.
 *
 * (Neutrale Individuen werden ab v.0.10 auch durch diese Klasse verwaltet -
 * TODO rename IndividuumController.)
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class MonsterController {

    //Der SpielfeldController
    private MapDataBeschaffer loader;
    //Gegenstand Generator für Quadrantenbestückung
    private GegenstandGenerator item_gen;
    //Alle Gegner in der aktiven Zone
    private HashSet<Individuum> aktive_gegner_db;
    //der Thread
    private Thread trigger;
    //Der bewegungs Tacktgeber
    private IndividuenTrigger triggerrunneble;
    //Game Trigger
    private GameTrigger masterTrigger;
    //der Spieler
    private Spieler spieler;

    /**
     * Liefert eine Instanz vom MonsterController
     *
     * @param loader benötigt sein Vaterobjekt als referen
     * @param item_gen benötigt einen Itemgenerator für Loot.
     * @param masterTrigger den Trigger des Spiels
     * @param spieler den Spieler
     */
    public MonsterController(MapDataBeschaffer loader, GegenstandGenerator item_gen, GameTrigger masterTrigger, Spieler spieler) {
        this.loader = loader;
        this.item_gen = item_gen;
        this.masterTrigger = masterTrigger;
        this.spieler = spieler;
        aktive_gegner_db = new HashSet<Individuum>();
        triggerrunneble = new IndividuenTrigger();
        trigger = new Thread(triggerrunneble, "IndividuenTrigger von " + this.toString());
        triggerrunneble.setVater(trigger);
        masterTrigger.setInd(triggerrunneble);
        trigger.start();


    }

    /**
     * entfernt ein Individuum aus der aktiven Liste, damit es stehenbleibt
     * (außerhalb der aktiven Zone)
     *
     * @param ind das zu entfernende Individuum
     */
    public void individuumDeaktivieren(Individuum ind) {
        aktive_gegner_db.remove(ind);
    }

    /**
     * fügt ein Individuum der aktiven Liste hinzu, damit es sich bewegt
     *
     * @param ind das zu aktivierende Individuum
     */
    public void individuumAktivieren(Individuum ind) {
        if (ind != null) {
            aktive_gegner_db.add(ind);
        }
    }

    /**
     * Diese Methode erzeugt für einen neuen Quadranten die zugehörigen Gegner.
     *
     * @param k eine Koordinate im Quadranten
     */
    public void monsterAufQuadrantSetzen(Koordinaten k) {
        //richtigen Quadrant herraussuchen
        k = loader.getQuadrant(k);

        MapDataFeld feld;
        //zufällige Anzahl an Gegnern erzeugen
        int anzahlMonster = (int) (Math.random() * 5) + 1;
        //die Monster auf der Karte verteilen

        for (int i = 0; i < anzahlMonster; i++) {

            //so lange neue Koordinaten generieren bis sie noch nicht besetzt sind und begehbar sind
            do {
                feld = loader.getMapDataFeld(new Koordinaten(k.getX() + (int) (19 * Math.random()), k.getY() + (int) (19 * Math.random())));
            } while (!feld.getBeschaffenheit().getBegehbar() || feld.getIndividuum() != null || feldinstartzone(feld));

            //Gegner generieren
            Individuum monster = getIndividuum(spieler.getLevel(), feld);
            //der Monsterliste hinzufügen
            feld.setIndividuum(monster);
            aktive_gegner_db.add(monster);

        }
    }

    /**
     * überprüft ob das feld in der Startzone liegt
     *
     * @param feld das zu überprüfende Feld
     * @return true wenn in der start Zone
     */
    private boolean feldinstartzone(MapDataFeld feld) {
        if ((feld.getKoordinaten().getX() < 5 && feld.getKoordinaten().getX() > -5) && (feld.getKoordinaten().getY() < 12 && feld.getKoordinaten().getY() > 2)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Bewegt alle aktiven Individuen, die zum Start der methode aktiv waren,
     * alle wärend der ausführung aktiv werdende Individuen werden nicht
     * behandelt.
     */
    public void individuenBewegen() {

        // if (true) return;
        //System.out.println("IT Start");
        HashSet<Individuum> eingefroren = (HashSet<Individuum>) aktive_gegner_db.clone();
        Iterator<Individuum> it = eingefroren.iterator();
        while (it.hasNext()) {
            Individuum gegner = it.next();
            if (isIndividuumAktiv(gegner)) {
                gegner.bewegen();
            }

        }

        //System.out.println("IT End");



    }

    /**
     * liefert ob das Individuum bewegt wird
     *
     * @param individuum das zuüberprüfende Individuum
     * @return true wenn das individuum aktiv ist
     */
    public boolean isIndividuumAktiv(Individuum individuum) {
        return aktive_gegner_db.contains(individuum);
    }

    /**
     * Genneriert ein zufälliges Individuum des Spiel-Levels
     *
     * @param spielLevel das aktuelle Level
     * @param feld die Position
     * @return das neue Individuum
     */
    private Individuum getIndividuum(int spielLevel, MapDataFeld feld) {
        double fuchs = 0, s_eich = 0, mader = 0, elster = 0;
        double rand = Math.random();
        switch (spielLevel) {
            case 1:
                break;
            case 2:
                s_eich = 0.5;
                break;
            case 3:
                s_eich = 0.5;
                fuchs = 0.2;
                break;
            case 4:
                s_eich = 0.4;
                fuchs = 0.2;
                mader = 0.2;

                break;
            case 5:
                s_eich = 0.2;
                fuchs = 0.2;
                mader = 0.2;
                elster = 0.2;

                break;
            default:
                s_eich = 0.1;
                fuchs = 0.4;
                mader = 0.2;
                elster = 0.2;

                break;
        }
        if (rand < s_eich) {
            return new SchwarzesEichhoernchen(feld, spieler);
        } else if (rand - s_eich < fuchs) {
            return new Fuchs(feld, spieler);
        } else if (rand - s_eich - fuchs < mader) {
            return new Mader(feld, spieler);
        } else if (rand - s_eich - mader - fuchs < elster) {
            return new Elster(feld, spieler);
        } else {
            return new Streifenhoernchen(feld, spieler);
        }
    }
}
