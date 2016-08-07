package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JComponent;
import view.bilder.GuiBildBeschaffer;

/**
 * Die GegenstandsListe ist eine GUI-Komponente die verschiedene Bildchen
 * (Gegenstände) mit Satz(Beschreibung) daneben, anzeugen kann.
 *
 * sie Benötigt eine Render komponente
 *
 * @version 0.9
 * @author Ole Richter
 */
public class GegenstandListe implements GuiKomponente {

    //Alle Gegenstände + Text
    private final HashMap<BufferedImage, String> inventarInhalt;
    //Bild-Puffer
    private GuiBildBeschaffer bildBeschaffer;
    //Render-Komponente
    private JComponent vater;
    //Höhe der Unterelemente
    private int groesse;

    /**
     * bereitet die GUI-Komponente vor
     */
    public GegenstandListe() {
        this.inventarInhalt = new HashMap<BufferedImage, String>();
        bildBeschaffer = new GuiBildBeschaffer();
        groesse = ((Toolkit.getDefaultToolkit().getScreenSize()).height) / 7;
    }

    /**
     * zeichnet die Liste auf die übergebene Graphics an den angegebenen
     * Koordinaten
     *
     * @param g die Graphics auf die gezeicnet werden soll
     * @param x die X Koordinate der Linken-Oberen-Ecke
     * @param y die Y Koordinate der Linken-Oberen-Ecke
     */
    @Override
    public synchronized void paintComponent(Graphics g, int x, int y) {

        //Schriftart + Farbe
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (35 * GuiBildBeschaffer.groessenVerhaeltnis())));
        g.setColor(Color.black);
        //Inhalt + Text
        Iterator<BufferedImage> it = inventarInhalt.keySet().iterator();
        while (it.hasNext()) {
            BufferedImage bild = it.next();
            g.drawImage(bild, x, y, (int) (groesse * 0.8), (int) (groesse * 0.8), null);
            y += (int) (groesse * 0.5);
            g.drawChars(inventarInhalt.get(bild).toCharArray(), 0, inventarInhalt.get(bild).length(), x + (int) (groesse * 0.8), y);
            y += (int) (groesse * 0.2);

        }
    }

    /**
     * fügt ein Gegenstand mit Beschreibung der Liste hinzu.
     *
     * @param artID Id -Konstanten siehe Gegenstand
     * @param text der zugehörige Text (zB "1x Nuss")
     */
    public synchronized void gegenstandHinzufuegen(int artID, String text) {
        BufferedImage bild = bildBeschaffer.ladeBild(GuiBildBeschaffer.GEGENSTAND, artID);

        inventarInhalt.put(bild, text);
        if (vater != null) {
            vater.repaint();
        }


    }

    /**
     * leert die Liste
     */
    public synchronized void leeren() {
        inventarInhalt.clear();
        if (vater != null) {
            vater.repaint();
        }
    }

    //ERbt JavaDoc von Interface
    @Override
    public void setVater(JComponent vater) {
        this.vater = vater;
    }
}
