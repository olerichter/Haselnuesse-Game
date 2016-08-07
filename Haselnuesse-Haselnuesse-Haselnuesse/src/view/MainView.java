package view;

import controller.game.trigger.MeldungTrigger;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * der MainView ist für das Spielfenster sowie für die Komponenten, die darin
 * angezeigt werden, verantwortlich - zB Spielfeld. Er verwalltet diese
 * Komponenten und reicht sie je nach Gebrauch an den ViewController weiter
 *
 * @version 0.9
 * @author Ole Richter
 */
public class MainView {

    private JFrame fenster;
    //private Spielfeld spielfeld;
    private JComponent letzterModus;
    private GraphicsDevice graphicsDevice;

    /**
     * baut das Spielfenster
     *
     * @param k der KeyInputController
     */
    public MainView(KeyListener k) {

        //Fenster
        fenster = new JFrame("Haselnüsse, Haselnüsse, Haselnüsse!");
        fenster.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


        //Fenster keylisener
        fenster.addKeyListener(k);
        //aktiviert
        fenster.setResizable(false);
        // Vollbild
        fenster.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        fenster.setUndecorated(true);

        //wird Vollbild auf primären Bildschirm gesetzt
        graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
        graphicsDevice.setFullScreenWindow(fenster);

        fenster.setVisible(true);

    }

    /**
     * beendet das Fenster (Spiel)
     */
    public void schliesen() {
        fenster.setVisible(false);
        fenster.removeAll();

    }

    /**
     * Setzt die GUI-Render-Komponente aktiv, löscht den letzteg aktive
     * GUI-Render-Komponente (JComponent)
     *
     * @param component die aktiv zu setzende GUI-Render-Komponente
     */
    public void aktiviereGuiRenderKomponente(JComponent component) {
        if (letzterModus != null) {
            fenster.remove(letzterModus);
        }
        //neue REnder-Komponente
        fenster.add(component);
        letzterModus = component;
        //Meldungen werden angepasst
        MeldungTrigger.getDefaultMeldungTrigger().getMeldungen().setVater(component);
        fenster.setVisible(true);
    }
}
