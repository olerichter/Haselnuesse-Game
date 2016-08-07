package controller.game.trigger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.MeldungenListe;

/**
 * Der MeldungenTrigger ist der Tacktgeber und Anlaufstelle für programmweite
 * Meldungen Er arbeitet als Master
 *
 * @version 0.9
 * @author Ole Richter
 */
public class MeldungTrigger implements Runnable {

    private static MeldungTrigger meldungTrigger;
    private static Thread errtrigger;
    /**
     * Hintergrund gelb
     */
    public static final int TIP = 2,
            /**
             * Hintergrund rot
             */
            NEGATIV = 3,
            /**
             * Hintergrund grün
             */
            POSITIV = 1;
    //meldungs Standzeit
    private int timer;
    //End of Life
    private boolean eol;
    //der Thread
    private Thread vater;
    //neue Meldungen
    private HashSet<String> neue;
    //alte Meldungen
    private HashSet<String> alte;
    //GUI-Komponente
    private MeldungenListe liste;

    /**
     * Erzeugt alle Sets und GUI-Komponente, setzt sie intevall Zeit auf 4 sec.
     */
    public MeldungTrigger() {
        neue = new HashSet<String>();
        alte = new HashSet<String>();
        liste = new MeldungenListe();
        timer = 4000;
        eol = false;
    }

    /**
     * fügt eine Errungenschaft / Nachricht dem Meldungsfeet hinzu.
     *
     * @param art die Art - Konstanten siehe diese Klasse
     * @param nachricht die anzuzeigende Nachricht
     */
    public void neueMeldung(int art, String nachricht) {
        neue.add(nachricht);
        liste.neueMeldung(art, nachricht);
    }

    /**
     * Die Überwachung der Anzeigedauer
     */
    @Override
    public void run() {
        while (!eol) {

            if (vater != null && !vater.isInterrupted()) {
                Iterator<String> it = alte.iterator();
                while (it.hasNext()) {
                    String string = it.next();
                    liste.entferne(string);
                }
                HashSet<String> tmp = alte;
                alte = neue;
                neue = tmp;
                neue.clear();

            }

            if (vater != null && vater.isAlive()) {
                try {
                    vater.sleep(timer);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MeldungTrigger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    /**
     * setzt den ausführenden Thread
     *
     * @param vater den Thread
     */
    public void setVater(Thread vater) {
        this.vater = vater;
    }

    /**
     * beendet die ausführung
     */
    public void loeschen() {
        vater = null;
        eol = true;
    }

    /**
     * Liefert das GUI-Element zurück
     *
     * @return das GUI-Element
     */
    public MeldungenListe getMeldungen() {
        return liste;
    }

    /**
     * Liefert den zuletzt aktiven MeldungTrigger
     *
     * @return den zuletzt aktiven MeldungTrigger
     */
    public static MeldungTrigger getDefaultMeldungTrigger() {

        if (meldungTrigger == null) {
            meldungTrigger = new MeldungTrigger();
            errtrigger = new Thread(meldungTrigger);
            meldungTrigger.setVater(errtrigger);
            errtrigger.start();
        }
        return meldungTrigger;
    }
}
