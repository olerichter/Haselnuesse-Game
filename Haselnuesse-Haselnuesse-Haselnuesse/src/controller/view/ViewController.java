package controller.view;

import controller.game.HauptMenue;
import controller.game.InventarController;
import controller.game.Kampfsystem;
import controller.game.trigger.GameTrigger;
import controller.game.trigger.KeyInputController;
import controller.toolkit.Koordinaten;
import data.Gegenstand;
import data.Individuum;
import data.MapDataFeld;
import data.Spieler;
import view.GameMenue;
import view.KampfView;
import view.MainView;
import view.Spielfeld;

/**
 * Der ViewController ist die zentralle Anlaufstelle für Data- und
 * Controller-Objekte die mit dem View interagieren wollen es startet die
 * verscheidenen Modi
 *
 * @version 0.9
 * @author Ole Richter
 */
public class ViewController {

    //das HauptMenü /Spielstarter
    private HauptMenue hauptMenue;
    //der Sichtradius
    private static final int SICHTRADIUS = 4;
    //der Radius der aktiven Zone
    private int aktiveZone = 0;
    //das SpielFenster
    private MainView spielfenster;
    //der trigger
    private GameTrigger masterTrigger;
    private Spielfeld spielfeld;

    /**
     * Der ViewController ist die zentralle Anlaufstelle für Data- und
     * Controller-Objekte, die mit dem View interagieren wollen
     *
     * @param hauptMenue das Hauptmenü
     * @param masterTrigger der aktuelle Trigger des Spieles
     */
    public ViewController(HauptMenue hauptMenue, GameTrigger masterTrigger) {
        this.hauptMenue = hauptMenue;
        this.masterTrigger = masterTrigger;
        KeyInputController key = new KeyInputController(this);
        spielfenster = new MainView(key);
        masterTrigger.setKey(key);

    }

    /**
     * ab version 0.7 wird diese Methode eine Anlaufstelle für Veränderuengen im
     * Sichtfeld sein als repaint()
     *
     *
     * @param feld - das Feld das sich verändert hat
     *
     */
    public void feldChanged(MapDataFeld feld) {
        if (spielfeld == null) {
            return;
        }
        synchronized (spielfeld) {
            sichtfeldZeichnen('k', feld);
        }
    }

    /**
     * ab Version 0.8 wird die Funktion für Abspeicher- und Hibernatevorgänge
     * von Individuuen und Feldern verantwortlich sein
     *
     *
     * @param koordinaten - die zuprüfende Koordinaten
     * @return true wenn das Feld in der aktivien Zone liegt
     */
    public boolean inAktiverZone(Koordinaten koordinaten) {

        int x = 0;
        int y = 7;
        if (hauptMenue.getAktuellesSpiel().getSpieler().getPosition() != null) {
            x = hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten().getX();
            y = hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten().getY();
        }

        if (Math.sqrt(Math.pow(x - koordinaten.getX(), 2) + Math.pow(y - koordinaten.getY(), 2)) >= aktiveZone) {

            return false;
        } else {
            return true;
        }

    }

    /**
     * löst einen direkten repaint der Map aus sollte aus Synchronitätsgründen
     * nur über feldChanged aufgerufen werden
     *
     * ab version 0.7 geplant private
     */
    private void maprepaint() {
        if (spielfeld == null) {
            return;
        }
        synchronized (spielfeld) {
            if (hauptMenue.getAktuellesSpiel().getSpieler().getPosition() == null) {
                return;
            }
            spielfeld.kompletterRepaint();
            //TODO Implement Sichtradius mit awareness of BERGEN
            //malt den linken Teil des Sichtfeldes
            sichtfeldZeichnen('l', hauptMenue.getAktuellesSpiel().getSpieler().getPosition());
            //malt den rechten Teil des Sichtfeldes
            sichtfeldZeichnen('r', hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getRechts());
            //malt den Spieler ganz oben auf
            spielfeld.setIndividuum(Individuum.SPIELER, hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten(), hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten());
            //gibt an, dass alle Vorbereitungen für einen repaint abgeschlossen sind!
        }
        spielfeld.repaint();





    }

    /**
     * zeichnet das Sichtfeld rekursiv nach links oder rechts
     *
     * fügt alle Elemente mit ihrer Bild iD und Position zum Spielfeld hinzu (
     *
     * @see View.Spielfeld)
     *
     * @param richtung l oder r für links oder rechts,o oder u für oben oder
     * unten, k für einzelnes Feld
     * @param feld das aktuelle Feld
     */
    private void sichtfeldZeichnen(char richtung, MapDataFeld feld) {
        if (spielfeld == null || masterTrigger.getModus() != GameTrigger.SPIELFELD_MODUS || aktiveZone == 0) {
            return;
        }
        //System.out.println("-->"+Math.sqrt(Math.pow(hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten().getX()-feld.getKoordinaten().getX(), 2)+Math.pow(hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten().getY()-feld.getKoordinaten().getY(), 2)));

        //prüft, ob das Feld noch im Sichtfeld liegt.  Satz des pythagoras für rundes Sichtfeld
        if (feld == null || Math.sqrt(Math.pow(hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten().getX() - feld.getKoordinaten().getX(), 2) + Math.pow(hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten().getY() - feld.getKoordinaten().getY(), 2)) >= SICHTRADIUS) {

            return;
        }
        //holt Individuum + gegenstand
        Individuum ind = feld.getIndividuum();
        Gegenstand item = feld.getGegenstand();
        //mahlt nuir das feld neu
        if (richtung == 'k') {
        }
        //behandelt das Individuum auf dem Feld

        if (ind != null) {
            //malt das Individuum
            spielfeld.setIndividuum(ind.getArt(), feld.getKoordinaten(), hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten());
        }
        //behandelt den Gegenstand auf dem Feld

        if (item != null) {
            //malt den Gegenstand
            spielfeld.setGegenstand(item.getArt(), feld.getKoordinaten(), hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten());
        }
        // behandeld das Feld selber und malt es
        spielfeld.setBild(feld.getBeschaffenheit().getBeschaffenheit(), feld.getKoordinaten(), hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten());
        //ruft rekursiv bestimmte benachbarte Felder auf


        switch (richtung) {
            case 'l':
                sichtfeldZeichnen('o', feld.getOben());
                sichtfeldZeichnen('u', feld.getUnten());
                sichtfeldZeichnen('l', feld.getLinks());
                break;
            case 'r':
                sichtfeldZeichnen('o', feld.getOben());
                sichtfeldZeichnen('u', feld.getUnten());
                sichtfeldZeichnen('r', feld.getRechts());
                break;
            case 'o':
                sichtfeldZeichnen('o', feld.getOben());
                break;
            case 'u':
                sichtfeldZeichnen('u', feld.getUnten());
                break;
            case 'k':
                Koordinaten pos = feld.getKoordinaten();
                Koordinaten cen = hauptMenue.getAktuellesSpiel().getSpieler().getPosition().getKoordinaten();

                if (ind == null) {
                    spielfeld.removeIndividuum(pos, cen);
                }
                if (item == null) {
                    spielfeld.removeGegenstand(pos, cen);
                }
                spielfeld.feldneuzeichnen(pos, cen);
                break;

        }
    }

    /**
     * bewegt den Spieler
     *
     * @param c die Richtung: o = oben, u = unten, l = links, r = rechts
     */
    public void move(char c) {

        if (hauptMenue.getAktuellesSpiel().getSpieler().move(c)) {
            maprepaint();
        }



    }

    /**
     * Aktiviert das Spielfeld löst ein repaint aus
     *
     */
    public void aktiviereSpielfeld() {

        spielfeld = new Spielfeld();
        spielfenster.aktiviereGuiRenderKomponente(spielfeld);

        masterTrigger.setModus(GameTrigger.SPIELFELD_MODUS);
        maprepaint();


    }

    /**
     * Aktiviert den KampfModus, setzt den KampfBildschirm in das Spielfenster
     *
     * @param kampf
     */
    public void aktiviereKampf(Kampfsystem kampf) {
        KampfView kview = new KampfView(kampf.getGegnerArt());
        spielfenster.aktiviereGuiRenderKomponente(kview);
        kampf.start(kview, this);
        //throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * liefert den Spieler aus dem aktuellen Speicher
     *
     * @return den Spieler
     */
    public Spieler getSpieler() {
        return hauptMenue.getAktuellesSpiel().getSpieler();
    }

    /**
     * Beenedet den Kampf ruft beim Gewinnen das Spielfeld auf, sonst das
     * Hauptmenü
     *
     * @param b
     * @param individuum
     */
    public void kampfEnde(boolean b, Individuum individuum) {
        if (b) {
            individuum.stirbt();
            aktiviereSpielfeld();
        } else {
            hauptMenue.loescheAktuellesSpiel();
            hauptMenue.hauptMenueAnzeigen();
        }
    }

    /**
     * setzt das Hauptmenü in das Spielfenser
     *
     * @param gameMenue
     */
    public void aktiviereHauptMenue(GameMenue gameMenue) {
        spielfenster.aktiviereGuiRenderKomponente(gameMenue);
    }

    /**
     * deaktiviert die aktive Zone - alle Felder werden damit inaktiv
     *
     * @param b
     */
    public void AktiveZoneAus(boolean b) {
        if (b) {
            aktiveZone = 0;
        } else {
            aktiveZone = SICHTRADIUS + 4;
        }
    }

    /**
     * setzt die InventarAnsicht in das Speilfenster
     */
    public void aktiviereInventar() {
        InventarController inventarController = new InventarController(this, masterTrigger);
        spielfenster.aktiviereGuiRenderKomponente(inventarController.getInventarView());
        masterTrigger.setModus(GameTrigger.INVENTAR_MODUS);
    }
}
