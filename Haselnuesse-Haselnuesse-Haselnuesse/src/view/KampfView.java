package view;

import controller.game.trigger.MeldungTrigger;
import data.Individuum;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import view.bilder.GuiBildBeschaffer;

/**
 * Der KampfView Ist die GUI-Render-Komponente des Kampfmodus
 *
 * es zeichnet Spieler, Gegner sowie den jeweiligen Status und ggf das
 * AngriffsMenü und die LOOT-Liste
 *
 * @version 0.9
 * @author Ole Richter
 */
public class KampfView extends JComponent {

    //Bild Buffer
    private GuiBildBeschaffer imageBuffer;
    //Spieler Bild
    private BufferedImage spieler;
    //Spieler Status (Leben)
    private String statusSpieler;
    //Spieler Angriff (Schaden)
    private String aktionSpieler;
    //Gegner Bild
    private BufferedImage gegner;
    //Gegner Status (Leben + Schaden)
    private String statusGegner;
    //gegner Angriff (Schaden)
    private String aktionGegner;
    //Angriffsmenü
    private GuiKomponente menue;
    //Loot-Liste
    private GuiKomponente gegenstandListe;
    //relative Position Gegner
    private int gegnerangriff;
    //relative Position Spieler
    private int spielerangriff;
    //Hintergrund
    private int ort;

    /**
     * Bereitet die GUI-RENDER-Komponente vor
     *
     * @param gegnerart die Art des Gegners - Konstanten siehe Individuum
     */
    public KampfView(int gegnerart) {
        imageBuffer = new GuiBildBeschaffer();
        this.spieler = imageBuffer.ladeBild(GuiBildBeschaffer.KAMPFINDIVIDUUM, Individuum.SPIELER);
        this.gegner = imageBuffer.ladeBild(GuiBildBeschaffer.KAMPFINDIVIDUUM, gegnerart);
        statusGegner = "";
        statusSpieler = "";
        gegnerangriff = 0;
        spielerangriff = 0;
        aktionGegner = "";
        aktionSpieler = "";




    }

    /**
     * Setzt den Status des Spielers
     *
     * @param statusSpieler der Status des Spielers
     */
    public void updateStatusSpieler(String statusSpieler) {
        this.statusSpieler = statusSpieler;
    }

    /**
     * Setzt den Status des Gegners
     *
     * @param statusGegner der Status des Gegners
     */
    public void updateStatusGegner(String statusGegner) {
        this.statusGegner = statusGegner;
    }

    /**
     * Setzt das Angriffsauswahlmenü (GUI-Komponente)
     *
     * @param menue das Angriffsauswahlmenü
     */
    public void setMenue(GuiKomponente menue) {
        this.menue = menue;
        if (menue != null) {
            menue.setVater(this);
        }
    }

    /**
     * Malt den KampfView
     *
     * @param grphcs die Graphics
     */
    @Override
    protected void paintComponent(Graphics grphcs) {
        //Schriftfarbe
        grphcs.setColor(Color.BLACK);
        super.paintComponent(grphcs);
        //größenberechnung
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x_part = d.width / 10, y_part = d.height / 10;
        //Hintergrund
        grphcs.drawImage(imageBuffer.ladeBild(GuiBildBeschaffer.KAMPFHINTERGRUND, ort), 0, 0, d.width, d.height, this);
        //Spieler Bild
        grphcs.drawImage(spieler, x_part + spielerangriff, d.height - y_part - 2 * x_part, x_part * 2, x_part * 2, this);
        //Gegner Bild
        if (gegenstandListe == null) {
            grphcs.drawImage(gegner, x_part * 7 - gegnerangriff, d.height - y_part - 2 * x_part, x_part * 2, x_part * 2, this);
        }
        //Schriftart für Status / Status
        grphcs.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (35 * GuiBildBeschaffer.groessenVerhaeltnis())));
        grphcs.drawChars(statusSpieler.toCharArray(), 0, statusSpieler.length(), x_part, y_part);
        grphcs.drawChars(statusGegner.toCharArray(), 0, statusGegner.length(), x_part * 5, y_part);
        //Schriftart für Angrifsmeldung / Angrifsmeldung
        grphcs.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (55 * GuiBildBeschaffer.groessenVerhaeltnis())));
        if (spielerangriff == 0) {
            grphcs.setColor(Color.RED);
        } else {
            grphcs.setColor(Color.GREEN);
        }
        grphcs.drawChars(aktionSpieler.toCharArray(), 0, aktionSpieler.length(), 2 * x_part + spielerangriff, d.height - y_part - x_part);
        if (gegnerangriff == 0) {
            grphcs.setColor(Color.RED);
        } else {
            grphcs.setColor(Color.GREEN);
        }
        grphcs.drawChars(aktionGegner.toCharArray(), 0, aktionGegner.length(), (int) (7.5 * x_part - gegnerangriff), d.height - y_part - x_part);

        //Angriffsmenü
        if (menue != null) {
            menue.paintComponent(grphcs, x_part, 2 * y_part);
        }
        //Loot-Liste
        if (gegenstandListe != null) {

            gegenstandListe.paintComponent(grphcs, x_part * 8, d.height - 6 * y_part);
            grphcs.drawChars("Belohnung:".toCharArray(), 0, 10, x_part * 8, d.height - 6 * y_part - (int) (25 * GuiBildBeschaffer.groessenVerhaeltnis()));
        }

        //Meldungen
        MeldungTrigger.getDefaultMeldungTrigger().getMeldungen().paintComponent(grphcs, d.width / 2 - (int) (600 * GuiBildBeschaffer.groessenVerhaeltnis()), d.height / 30);

    }

    /**
     * setzt am Kampfende die angezeigte Belohnungsliste (GUI-Komponente)
     *
     * @param gegenstandListe die Belohnungsliste (GUI-Komponente)
     */
    public void setBelohnung(GuiKomponente gegenstandListe) {
        this.gegenstandListe = gegenstandListe;
        if (menue != null) {
            gegenstandListe.setVater(this);
        }
    }

    /**
     * Aktiviert die verschiebung des Spielers oder Gegners, sowie die Anzeige
     * der Angiffsinformationen
     *
     * @param b ob der Spieler angreift
     * @param spieler_text Angiffsinformationen des Spielers
     * @param gegner_text Angiffsinformationen des Gegners
     */
    public void maleAngriff(boolean b, String spieler_text, String gegner_text) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x_part = d.width / 10, y_part = d.height / 10;
        if (b) {
            spielerangriff = 2 * x_part;

        } else {
            gegnerangriff = 2 * x_part;
        }
        aktionGegner = gegner_text;
        aktionSpieler = spieler_text;
    }

    /**
     * beendet die von maleAngriff oder maleHeilen hervorgerufenen Änderungen
     */
    public void angriffEnde() {
        aktionGegner = "";
        aktionSpieler = "";
        gegnerangriff = 0;
        spielerangriff = 0;
    }

    /**
     * setzt den Hintergrund
     *
     * @param hintergrund Hintergrund - Konstanten siehe Kampfsystem
     */
    public void setOrt(int hintergrund) {
        ort = hintergrund;
    }

    /**
     * aktiviert die Anzeige von der Heilungsinformation
     *
     * @param toString die Heilungsinformation
     */
    public void maleHeilen(String toString) {
        aktionSpieler = toString;
        spielerangriff = 1;
    }
}
