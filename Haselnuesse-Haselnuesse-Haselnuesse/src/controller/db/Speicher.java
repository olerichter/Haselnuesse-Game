package controller.db;

import controller.game.MapDataBeschaffer;
import controller.game.trigger.GameTrigger;
import controller.game.trigger.MeldungTrigger;
import controller.toolkit.Koordinaten;
import controller.view.ViewController;
import data.MapDataFeld;
import data.Spieler;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Der Speicher dient als Datenbank für inaktive Felder (Felder außerhalb der
 * aktiven Zone). Sowie als Hauptspeicherobjekt für das Abspeichern des Spiels.
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Speicher implements Serializable {

    //der Speicherpfad
    private final static String pfad = "./";
    //die passive Feldliste
    private HashMap<Koordinaten, MapDataFeld> db;
    //der aktuelle Spieler
    private Spieler spieler;
    //bei Speicherung die Position des Spielers
    private Koordinaten spielerSavePosKoordinaten;
    //MapDataBeschaffer für das Speichern
    private MapDataBeschaffer mapDataBeschaffer;

    /**
     * Der Konstruktor inizialisiert die Map (DB)
     */
    public Speicher() {
        this.db = new HashMap<Koordinaten, MapDataFeld>();
    }

    /**
     * initSpiel erstellt alle Vorraussetzungen für ein neues Spiel: einen neuen
     * Spieler,einen MapDataBeschaffer (Spielfeldverwaltung) und er setzt den
     * Spieler auf die Startposition (0,7)
     *
     * @param spielerName - der Name des neuen Spielers
     * @param masterTrigger - Der spielweite Tacktgeber
     * @param view - der für die GUI zuständige Controller
     */
    public void initSpiel(String spielerName, GameTrigger masterTrigger, ViewController view) {
        //neuer spieler
        spieler = new Spieler(100, 100, spielerName, view, masterTrigger);
        //sichtfeld aktivieren
        view.AktiveZoneAus(false);
        //neuer Beschaffer
        mapDataBeschaffer = new MapDataBeschaffer(view, masterTrigger, this);
        //speichermodus wird deaktiviert
        mapDataBeschaffer.aktiviereSpeichern(false);
        //Spieler wird auf position gesetzt - Mapgennerierung gestartet
        spieler.setPosition(mapDataBeschaffer.getMapDataFeld(new Koordinaten(0, 7)));

    }

    /**
     * Überprüft ob ein inaktives Feld (abgespeichert) mit den Koordinaten
     * exsistiert
     *
     * @param koordinaten - die Koordinaten des Feldes
     * @return true wenn das Feld exisitert sonst false
     */
    public boolean feldExistiert(Koordinaten koordinaten) {

        if (feldLaden(koordinaten) != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Das übergebene feld wird deaktiviert (isioliert) und danach in die DB
     * abgespeichert
     *
     * @param feld - das abgespeichert werden soll
     */
    public synchronized void feldSpeichern(MapDataFeld feld) {
        if (feld == null) {
            return;
        }
        //nicht serialisierbare Objekte werden entfernt
        feld.setLoader(null);
        feld.setView(null);
        feld.loescheNachbarn();
        //feld wird in die passive Liste gespeichert
        //      --      speichern(feld, pfad+"f_"+feld.getKoordinaten().getX()+"_"+feld.getKoordinaten().getY()+".spielfeldobjekt");
        db.put(feld.getKoordinaten(), feld);



    }

    /**
     * Läd das feld der übergebenen Koordinaten aus der DB --ACHTUNG-- Feld ist
     * isoliert, muss duch MapDataBeschaffer aktiviert werden.
     *
     * @param koordinaten des zuladenden Feldes
     * @return das Feld
     */
    public synchronized MapDataFeld feldLaden(Koordinaten koordinaten) {

        return db.get(koordinaten);
        //  --  return (MapDataFeld)laden(pfad+"f_"+koordinaten.getX()+"_"+koordinaten.getY()+".spielfeldobjekt");
    }

    /**
     * liefert den aktuellen Spieler des Spiels
     *
     * @return den Spieler
     */
    public Spieler getSpieler() {
        return spieler;
        //  --  return (Spieler) laden(pfad+"aspieler.spielerobjekt");
    }

    /**
     * speichert das gesamte Spiel ab unter dem angegebenen Pfad
     */
    public void spielSpeichern() {
        speichernVorbereiten();
        speichern(this, pfad + "spiel.spiel");
    }

    /**
     * Läd ein gespeichertes Spiel --ACHTUNG-- Spiel ist noch isoliert, muss
     * noch aktiviert werden, dies passiert durch nachträgliches aufrufen von
     * ladenNachbereiten()
     *
     * @return eine Speicher-Instanz
     */
    public static Speicher spielLaden() {

        return (Speicher) laden(pfad + "spiel.spiel");

    }
    /*
     * Speichert ein übergebenenes Objekt, unter dem oben angegebenen pfad
     */

    private void speichern(Object obj, String dateiname) {

        try {
            FileOutputStream fos = new FileOutputStream(dateiname);
            ObjectOutputStream out = new ObjectOutputStream(fos);

            out.writeObject(obj);

            out.close();

        } catch (Exception ex) {
            //meldung
            if (MeldungTrigger.getDefaultMeldungTrigger() != null) {
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.NEGATIV, "Speichern fehlgeschlagen " + ex.getCause().getMessage());
            }
        }

    }
    /*
     * Läd ein angegebenes Objekt
     */

    private static Object laden(String dateiname) {


        try {
            FileInputStream fis = new FileInputStream(dateiname);
            ObjectInputStream oit = new ObjectInputStream(fis);

            return oit.readObject();
        } catch (Exception ex) {
            //meldung
            if (MeldungTrigger.getDefaultMeldungTrigger() != null) {
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.NEGATIV, "Laden fehlgeschlagen " + ex.getCause().getMessage());
            }
        }
        return null;



    }
    /*
     * bereitet das Speichern vor, entfernt alle nichtserialisierbaren Objekte
     */

    private void speichernVorbereiten() {
        //position wird gesichert
        spielerSavePosKoordinaten = spieler.getPosition().getKoordinaten();
        //speichen wird aktiviert - aktive liste wird geleert
        mapDataBeschaffer.aktiviereSpeichern(true);
        //alle nicht serialiserbaren objekte werden entfert
        spieler.setPosition(null);
        spieler.setMasterTrigger(null);
        spieler.setViewController(null);
        mapDataBeschaffer = null;
    }

    /**
     * Aktiviert den Speicher: Setzt den ViewController, GameTrigger und
     * MapDataBeschaffer
     *
     * @param masterTrigger - Der spielweite Tacktgeber
     * @param view - der für die GUI zuständige Controller
     */
    public void ladenNachbereiten(ViewController view, GameTrigger masterTrigger) {
        //neuer Beschaffer
        mapDataBeschaffer = new MapDataBeschaffer(view, masterTrigger, this);
        //speichern deaktivieren
        mapDataBeschaffer.aktiviereSpeichern(false);
        //objekte Setzten
        spieler.setMasterTrigger(masterTrigger);
        spieler.setViewController(view);
        spieler.setPosition(mapDataBeschaffer.getMapDataFeld(spielerSavePosKoordinaten));
        //Spiel akticvieren
        view.aktiviereSpielfeld();

    }

    /**
     * erstellt eine neue Map, und speichert
     *
     * @param masterTrigger - Der spielweite Tacktgeber
     * @param view - der für die GUI zuständige Controller
     */
    public void neuesLevel(ViewController view, GameTrigger masterTrigger) {

        masterTrigger.setModus(GameTrigger.ZWISCHENSEQUENZ);

        //speichern
        speichernVorbereiten();
        speichern(this, "autosicherung.spiel");
        //speicher zurücksetzten
        db.clear();
        spielerSavePosKoordinaten = new Koordinaten(0, 7);
        ladenNachbereiten(view, masterTrigger);
        view.aktiviereSpielfeld();
        //meldung
        //masterTrigger.getErrungenschaftenTrigger().neueMeldung(MeldungTrigger.POSITIV, "Spiel gespeichert - automatische Speicherung");
    }

    /**
     * --- nur für JUnit Tests ---
     *
     * @return nur für JUnit Tests
     */
    public MapDataBeschaffer beschafferFuerTEST() {
        return mapDataBeschaffer;
    }
}
