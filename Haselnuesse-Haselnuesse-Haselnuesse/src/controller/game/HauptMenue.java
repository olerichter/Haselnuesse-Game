package controller.game;

import controller.db.Speicher;
import controller.game.trigger.GameTrigger;
import controller.game.trigger.MeldungTrigger;
import controller.view.MenuActionHandler;
import controller.view.ViewController;
import view.GameMenue;
import view.Menue;
import view.MenueElement;

/**
 * Das HauptMenu dieht als Startklasse, zugleich kümmert es sich um das
 * Hauptmenü es erstellt den programmweiten Tacktgeber (GameTrigger), und den
 * ViewCOntroller der für die GUI ständig ist.
 *
 * MenuActionHandler wird implementiert um die Nutzereingaben im Menü
 * auszuführen.
 *
 * @version 0.9
 * @author Ole Richter
 */
public class HauptMenue implements MenuActionHandler {

    /**
     * menü punkte - Spiel beenden
     */
    public static final int SPIEL_BEENDEN = 1;
    /**
     * menü punkte - Speichern
     */
    public static final int SPIEL_SPEICHERN = 2;
    /**
     * menü punkte - Laden
     */
    public static final int SPIEL_LADEN = 3;
    /**
     * menü punkte - Neues Spiel
     */
    public static final int SPIEL_NEU = 4;
    /**
     * menü punkte - Spiel fortsetzten
     */
    public static final int SPIEL_FORTSETZTEN = 5;
    private GameTrigger masterTrigger;
    private ViewController view;
    private Speicher aktuellesSpiel;

    /**
     * liefert den aktuellen Speicher
     *
     * @return die aktuelle Speicherinstanz (aktuelle Spiel)
     */
    public Speicher getAktuellesSpiel() {
        return aktuellesSpiel;
    }

    /**
     * Startet das Spiel - im test modus ist der taktgeber deaktiviert.
     *
     * @param testModus - deaktiviert den tacktgeber
     */
    public HauptMenue(boolean testModus) {

        //Tacktgeber wird erstellt
        masterTrigger = new GameTrigger(this);
        //die GUI wird vorbereitet
        view = new ViewController(this, masterTrigger);
        //die GUI wird dem Tacktgeber übergeben
        masterTrigger.setView(view);
        //das HauptMenü wird gesartet
        hauptMenueAnzeigen();
        //der Tacktgeber wird gestartet
        if (!testModus) {
            masterTrigger.start();
        }
    }

    /**
     * Startet ein neues Spiel TODO Spielernamen abfragen
     */
    public void neuesSpiel() {

        //ein neuer Speicher
        aktuellesSpiel = new Speicher();
        //neues Spiel / Speiler
        aktuellesSpiel.initSpiel("Spieler", masterTrigger, view);
        //Spiel aktivieren
        view.aktiviereSpielfeld();

    }

    /**
     * Zeigt das Hauptmenü im Spielfenster , deaktiviert Individuuenbewegungen
     */
    public void hauptMenueAnzeigen() {
        //Neue Menü GUI
        GameMenue gameMenue = new GameMenue();
        //neues Menü
        Menue menue = new Menue("Hauptmenü", 4, masterTrigger, this);
        gameMenue.setMenue(menue);
        //Menüelemente werden hinzugefügt
        menue.menueElementHinzufuegen(new MenueElement("Ein neues Spiel starten", this, SPIEL_NEU));
        menue.menueElementHinzufuegen(new MenueElement("Ein gespeichertes Spiel laden", this, SPIEL_LADEN));
        if (aktuellesSpiel != null) {
            menue.menueElementHinzufuegen(new MenueElement("Das aktuelle Spiel speichern", this, SPIEL_SPEICHERN));
            menue.menueElementHinzufuegen(new MenueElement("zurück zum aktuellen Spiel", this, SPIEL_FORTSETZTEN));
        }
        menue.menueElementHinzufuegen(new MenueElement("Das Spiel beenden", this, SPIEL_BEENDEN));
        //menü wird aktiviert
        view.aktiviereHauptMenue(gameMenue);
        masterTrigger.setModus(GameTrigger.HAUPTMENU);
        //hilfe meldung
        MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "Pfeiltasten zum auswählen der Aktion, Enter zum bestätigen");

    }

    /**
     * Nimmt die ausgewählte Aktion des Menüs entgegen
     *
     * @param AktionId - eine der Definierten Konsanten
     */
    @Override
    public void aktionAusfueren(int AktionId) {
        switch (AktionId) {
            case SPIEL_NEU:
                neuesSpiel();
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "R - Rucksack, ESC - Menü, WASD - bewegen. Sammle viele Haselnüsse!");
                break;
            case SPIEL_LADEN:
                aktuellesSpiel = Speicher.spielLaden();
                aktuellesSpiel.ladenNachbereiten(view, masterTrigger);
                break;
            case SPIEL_SPEICHERN:
                if (aktuellesSpiel != null) {
                    aktuellesSpiel.spielSpeichern();
                    aktuellesSpiel.ladenNachbereiten(view, masterTrigger);
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.POSITIV, "Spiel wurde gespeichert");
                }

                break;
            case SPIEL_FORTSETZTEN:
                if (aktuellesSpiel != null) {
                    view.aktiviereSpielfeld();
                }
                break;
            case SPIEL_BEENDEN:
                System.exit(0);
                break;

        }
    }

    /**
     * löscht das aktuelle Spiel mit Spieler - nach GAMEOVER
     */
    public void loescheAktuellesSpiel() {
        aktuellesSpiel = null;
    }

    /**
     * beendet das Menü wenn ESC gedrückt wurde
     */
    @Override
    public void menuBeenden() {
        if (aktuellesSpiel != null) {
            view.aktiviereSpielfeld();
        }
    }

    /**
     * -- nur für JUNIT Tests --
     *
     * @return -- nur für JUNIT Tests --
     */
    public MapDataBeschaffer getFeldFuerTEST() {
        return aktuellesSpiel.beschafferFuerTEST();

    }
}
