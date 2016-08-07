package view;

import controller.game.Kampfsystem;
import controller.game.trigger.MeldungTrigger;
import data.Individuum;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JComponent;
import view.bilder.GuiBildBeschaffer;

/**
 * Das GameMenue ist die GUI-RENDER-Komponente des Hauptmenüs
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class GameMenue extends JComponent {

    private GuiKomponente menue;
    private GuiBildBeschaffer bildBeschaffer;

    /**
     * Bereitet die GUI-RENDER-Komponente vor
     */
    public GameMenue() {
        bildBeschaffer = new GuiBildBeschaffer();
    }

    /**
     * setzt das Hauptmenü
     *
     * @param menue die GUI-Komponente des Menüs
     */
    public void setMenue(GuiKomponente menue) {
        this.menue = menue;
        if (menue != null) {
            menue.setVater(this);
        }

    }

    /**
     * Malt das Hauptmenü
     *
     * @param grphcs die Graphics
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        //hintergrund
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        grphcs.drawImage(bildBeschaffer.ladeBild(GuiBildBeschaffer.KAMPFHINTERGRUND, Kampfsystem.HINTERGRUND_WALD), 0, 0, d.width, d.height, this);
        
        grphcs.drawImage(bildBeschaffer.ladeBild(GuiBildBeschaffer.INDIVIUUM, Individuum.SPIELER), d.width/10, d.height/2, (int)(d.height/ 7 * 0.8), (int)(d.height/ 7 * 0.8), this);
        grphcs.drawImage(bildBeschaffer.ladeBild(GuiBildBeschaffer.KAMPFINDIVIDUUM, Individuum.FUCHS), (int)(d.width/5 * 3.7), d.height/10*5, d.width/5, d.width/ 5, this);
        //Menü
        menue.paintComponent(grphcs, d.width / 2 - (int) (275 * GuiBildBeschaffer.groessenVerhaeltnis()), d.height / 3);
        //Titel
        grphcs.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (80 * GuiBildBeschaffer.groessenVerhaeltnis())));
        grphcs.drawString("Haselnüsse, Haselnüsse, Haselnüsse!", d.width / 2 - (int) (700 * GuiBildBeschaffer.groessenVerhaeltnis()), d.height- (int) (140 * GuiBildBeschaffer.groessenVerhaeltnis()));
        //Meldungen
        MeldungTrigger.getDefaultMeldungTrigger().getMeldungen().paintComponent(grphcs, d.width / 2 - (int) (600 * GuiBildBeschaffer.groessenVerhaeltnis()), d.height / 10);


    }
}
