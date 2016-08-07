package view;

import controller.game.Kampfsystem;
import controller.game.trigger.MeldungTrigger;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JComponent;
import view.bilder.GuiBildBeschaffer;

/**
 * Ist die GUI-Render-Komponente der Inventaransicht.
 *
 *
 * @version 0.9
 * @author Ole Richter
 */
public class InventarView extends JComponent {

    //Inhalt des Rucksackes
    private GuiKomponente gegenstandListe;
    //Level + Leben Status
    private String text;
    //bild Puffer
    private GuiBildBeschaffer bildBeschaffer;
    //Aktionsmenü
    private GuiKomponente menue;

    /**
     * Bereitet die GUI-RENDER-Komponente vor
     */
    public InventarView() {
        bildBeschaffer = new GuiBildBeschaffer();
    }

    /**
     * Setzt die Anzuzeigende GUI-Komponente (GegenstandListe)
     *
     * @param gegenstandListe die Anzuzeigende GUI-Komponente (GegenstandListe)
     */
    public void setGegenstandListe(GuiKomponente gegenstandListe) {
        this.gegenstandListe = gegenstandListe;
        if (gegenstandListe != null) {
            gegenstandListe.setVater(this);
        }
    }

    /**
     * Setzt das anzuzeigene Aktionsmenü (GUI-Komponente)
     *
     * @param menue das anzuzeigene Aktionsmenü (GUI-Komponente)
     */
    public void setMenue(GuiKomponente menue) {
        this.menue = menue;
        if (menue != null) {
            menue.setVater(this);
        }
    }

    /**
     * Malt den Inventar
     *
     * @param grphcs die Graphics
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        //Schriftart und Größenberechnug
        grphcs.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (45 * GuiBildBeschaffer.groessenVerhaeltnis())));
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x_part = d.width / 10;
        int y_part = d.height / 10;

        //Hintergrund
        grphcs.drawImage(bildBeschaffer.ladeBild(GuiBildBeschaffer.KAMPFHINTERGRUND, Kampfsystem.HINTERGRUND_FELD), 0, 0, d.width, d.height, this);
        //Status
        grphcs.drawChars(text.toCharArray(), 0, text.length(), x_part, y_part * 2);
        //Inhalt
        gegenstandListe.paintComponent(grphcs, x_part, y_part * 4);
        //Menü
        menue.paintComponent(grphcs, x_part * 7, y_part * 4);
        //Meldungen
        MeldungTrigger.getDefaultMeldungTrigger().getMeldungen().paintComponent(grphcs, d.width / 2 - (int) (600 * GuiBildBeschaffer.groessenVerhaeltnis()), d.height / 30);
    }

    /**
     * Setzt den Spieler/Level Infotext
     *
     * @param string der Spieler/Level Infotext
     */
    public void setText(String string) {
        text = string;
    }
}
