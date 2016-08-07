package controller.game.trigger;

import controller.game.HauptMenue;
import controller.game.MonsterController;
import controller.view.ViewController;
import data.Gegenstand;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.Menue;

/**
 * Der GameTrigger ist der Taktgeber für das Spiel, er verhindert datenkoruption
 * die durch Multitheding entsteht ausserdem aktiviert und deaktiviert er den
 * IndivduenTrigger (Individuenbewegungen) sowie den Keylisener(jedenfalls
 * partiell) in besimmten Spielzuständen
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class GameTrigger extends Thread {

    /**
     * Konstante dür den enspechenden Modus
     */
    public static final int SPIELFELD_MODUS = 1, KAMPF_MODUS = 2, HAUPTMENU = 3, INVENTAR_MODUS = 4, ZWISCHENSEQUENZ = 5;
    //KeyLisener
    private KeyInputController key;
    //IndividuuenTrigger
    private IndividuenTrigger ind;
    //IndividuuenController
    private MonsterController indivduen_ctr;
    //ViewController
    private ViewController view;
    //der aktuelle Modus - Konstanten oben
    private int modus;
    //Startklasse
    private HauptMenue hauptMenue;
    //Wenn ein menü aktiv, dann hier die Refferenz
    private Menue aktuellesMenue;
    //wenn ein neues Level gestartet werden soll
    private boolean neuesLevel;
    //nur für tests
    private int cheat;
    //wenn das Spiel abgeschlossen wurde
    private boolean spielEnde;

    /**
     * bereitet den Taktgeber vor
     *
     * @param hauptMenue
     */
    public GameTrigger(HauptMenue hauptMenue) {
        super();
        this.hauptMenue = hauptMenue;
        neuesLevel = false;
        spielEnde = false;

    }

    /**
     * Liefert den aktuellen Modus des Taktgebers
     *
     * @return den aktuellen Spielmodus - Konsanten siehe diese Klasse
     */
    public int getModus() {
        return modus;
    }

    /**
     * diese Methode wird einmal pro Takt audgefürt sie überprüft ob der #Nutzer
     * eine Taste gedrückt hat oder ob sich die Individuuen bewegen wollen
     */
    @Override
    public void run() {

        while (!isInterrupted()) {

            //Trigger für Spiel
            checkTheadsNachAktionen();
            //Neues Level
            if (neuesLevel && hauptMenue.getAktuellesSpiel() != null) {
                hauptMenue.getAktuellesSpiel().neuesLevel(view, this);
                neuesLevel = false;
            }
            //Spiel Ende - Sieg
            if (spielEnde && hauptMenue.getAktuellesSpiel() != null) {
                spielEnde = false;
                view.kampfEnde(false, null);

            }
            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(GameTrigger.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * setzt den aktuellen KeyLisener
     *
     * @param key
     */
    public void setKey(KeyInputController key) {
        this.key = key;
    }

    /**
     * Setzt den aktuellen IndividuenTrigger (Individuenbewegungen)
     *
     * @param ind den neuen IndividuenTrigger
     */
    public void setInd(IndividuenTrigger ind) {
        this.ind = ind;
    }

    /**
     * setzt den aktuellen IndividuenController (Verwalter der aktiven
     * Individuen)
     *
     * @param indivduen_ctr den neuen IndividuenController
     */
    public void setIndivduen_ctr(MonsterController indivduen_ctr) {
        this.indivduen_ctr = indivduen_ctr;
    }

    /**
     * setzt den aktuellen VIewCOntroller
     *
     * @param view den neuen ViewController
     */
    public void setView(ViewController view) {
        this.view = view;
    }

    /**
     * Diese Methode überprüft ob der #Nutzer eine Taste gedrückt hat oder ob
     * sich die Individuuen bewegen wollen
     */
    private void checkTheadsNachAktionen() {
        if (modus == SPIELFELD_MODUS && ind.isAusgeloest()) {
            indivduen_ctr.individuenBewegen();
        }

        KeyEvent ke = null;
        if (key != null && view != null) {
            ke = key.getLastKeyEvent();
        }
        if (ke != null) {
            switch (ke.getKeyCode()) {
                case KeyEvent.VK_W:
                    if (modus == SPIELFELD_MODUS) {
                        view.move('o');
                    }
                    break;
                case KeyEvent.VK_A:
                    if (modus == SPIELFELD_MODUS) {
                        view.move('l');
                    }
                    break;
                case KeyEvent.VK_S:
                    if (modus == SPIELFELD_MODUS) {
                        view.move('u');
                    }
                    break;
                case KeyEvent.VK_D:
                    if (modus == SPIELFELD_MODUS) {
                        view.move('r');
                    }
                    break;
                case KeyEvent.VK_R:
                    if (modus == SPIELFELD_MODUS) {
                        view.aktiviereInventar();
                    }
                    break;
                // -- TEST ONLY --
                case KeyEvent.VK_NUMPAD0:
                    if (modus == ZWISCHENSEQUENZ || modus != HAUPTMENU) {
                        if (cheat > 5) {
                            view.getSpieler().getRucksack().addGegenstand(new Gegenstand(Gegenstand.HASELNUSS), 2);
                        } else {
                            cheat++;
                        }
                    }
                    break;

                case KeyEvent.VK_ENTER:
                    if (aktuellesMenue != null) {
                        aktuellesMenue.aktionAusfueren();
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (aktuellesMenue != null) {
                        aktuellesMenue.runter();
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (aktuellesMenue != null) {
                        aktuellesMenue.hoch();
                    }
                    break;
                case KeyEvent.VK_ESCAPE:
                    if (modus == SPIELFELD_MODUS) {
                        hauptMenue.hauptMenueAnzeigen();
                    } else {
                        aktuellesMenue.verlassen();
                    }
                    break;
            }
        }
    }

    /**
     * Ändert den Modus bei SPIELFELD_MODUS werden alle Menüs entfernt
     *
     * @param modus konstante siehe diese Klasse
     */
    public void setModus(int modus) {
        this.modus = modus;
        if (modus == SPIELFELD_MODUS) {
            aktuellesMenue = null;


        }
    }

    /**
     * setzt ein Menü
     *
     * @param aThis neues Menü
     */
    public void setMenue(Menue aThis) {

        aktuellesMenue = aThis;

    }

    /**
     * Start ein neues Level
     */
    public void neuesLevel() {
        neuesLevel = true;
    }

    /**
     * beendet das aktuelle Spiel - (Sieg)
     */
    public void spielEnde() {
        spielEnde = true;
    }
}
