package controller.game.trigger;

import controller.view.ViewController;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Der KeyInputController ist für alle vom Benutzer eingegebenen Befehle
 * (vornehmlich Tastaturbefehle - Tasten) zustänndig und leitet sie in Version
 * 0.5 über den ViewController an die zuständigen Stellen weiter arbeitet ab
 * v0.7 als SLAVE - die Daten werden vom Master ausgelesen (GameTrigger)
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class KeyInputController implements KeyListener {

    ViewController view;
    private KeyEvent lastKeyEvent;

    /**
     * Intialisiert den Keylistener
     *
     * @param view
     */
    public KeyInputController(ViewController view) {
        this.view = view;
    }

    /**
     * Does Nothing
     *
     * @param ke
     */
    @Override
    public void keyTyped(KeyEvent ke) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * hier werden die Tasten an den Trigger weitergeleitet
     *
     * W - Spieler nach Oben A - Spieler nach Links S - Spieler nach Unten D -
     * Spieler nach Rechts AB VERSION 0.6 alle Tasten
     *
     * @param ke
     */
    @Override
    public void keyPressed(KeyEvent ke) {
        lastKeyEvent = ke;
    }

    /**
     * Does Nothing
     *
     * @param ke
     */
    @Override
    public void keyReleased(KeyEvent ke) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Liefert die zuletzt gedrückte Taste setzt den Tastenspeicher zurück
     *
     * @return die zuletzt gedrückte Taste
     */
    public KeyEvent getLastKeyEvent() {
        KeyEvent tmp = lastKeyEvent;
        lastKeyEvent = null;
        return tmp;

    }
}
