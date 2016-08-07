package controller.game.trigger;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Der IndividuenTrigger ist der Tacktgeber für die Monster bewegungen Er
 * arbeitet als Slave - Master ist der GameTrigger
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class IndividuenTrigger implements Runnable {

    private int timer;
    private boolean eol;
    private Thread vater;
    private boolean ausgeloest;

    /**
     * bereitet das runneble vor intervall 0.4 sec
     */
    public IndividuenTrigger() {
        timer = 400;
        eol = false;


    }

    /**
     * bewegt die Individuuen
     */
    @Override
    public void run() {
        while (!eol) {

            if (vater != null && !vater.isInterrupted()) {
                ausgeloest = true;
            }

            if (vater != null && vater.isAlive()) {
                try {
                    vater.sleep(timer);
                } catch (InterruptedException ex) {
                    Logger.getLogger(IndividuenTrigger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }

    /**
     * setzt den ausführenden Thread
     *
     * @param vater den ausführenden Thread
     */
    public void setVater(Thread vater) {
        this.vater = vater;
    }

    /**
     * beendet den Thread
     */
    public void loeschen() {
        vater = null;
        eol = true;
    }

    /**
     * Liefert zurück ob die Individuuen sich bewegen sollen setzt den Auslöser
     * zurück
     *
     * @return true wenn die Individuuen sich bewegen sollen
     */
    public boolean isAusgeloest() {
        boolean ausgeloest = this.ausgeloest;
        this.ausgeloest = false;
        return ausgeloest;
    }
}
