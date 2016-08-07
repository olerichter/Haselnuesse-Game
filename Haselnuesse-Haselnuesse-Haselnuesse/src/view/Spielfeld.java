package view;

import controller.game.trigger.MeldungTrigger;
import controller.toolkit.Koordinaten;
import data.Beschaffenheit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import view.bilder.GuiBildBeschaffer;

/**
 * diese Klasse beinhaltet die RenderQues /PrePaintBuffer für das Spielfeld sie
 * ist gleichzeitig das JComponent auf dessen graphic das Spielfeld gemalt wird
 *
 * ab v0.7 wird auch parzielles Neuzeichen unterstützt, dh wenn sich nur ein
 * Feld ändert wird auch nur dieses neugezeichnet - Effizienz
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Spielfeld extends JComponent {

    //feld pixelgröße
    private Dimension fenstergroesse;
    private int quadrat;
    //grafik Buffer
    private GuiBildBeschaffer imageBuffer;
    //Renderlisten
    private ArrayList<GSpielFeldElement> gspielfeld_1;
    private ArrayList<GSpielFeldElement> gspielfeld_2;
    private ArrayList<GSpielFeldElement> gspielfeld_3;
    private ArrayList<GSpielFeldElement> itemlist;
    private ArrayList<GSpielFeldElement> indlist;

    /**
     * Initialisiert die Buffer und Renderlisten
     */
    public Spielfeld() {
        fenstergroesse = Toolkit.getDefaultToolkit().getScreenSize();
        gspielfeld_3 = new ArrayList<GSpielFeldElement>();
        gspielfeld_1 = new ArrayList<GSpielFeldElement>();
        gspielfeld_2 = new ArrayList<GSpielFeldElement>();
        imageBuffer = new GuiBildBeschaffer();
        itemlist = new ArrayList<GSpielFeldElement>();
        indlist = new ArrayList<GSpielFeldElement>();
        quadrat = fenstergroesse.height / 7;

    }

    /**
     * Fügt ein Feld (Hintergrund/Spielfeld) der Renderliste hinzu
     *
     * --Aufruf muss mit diseer Instanz syncronisiert sein!--
     *
     * Die Bilder werden Jar-Save geladen
     *
     * @param art int id-schlüssel fürs bild
     * @param position absolute Position des bildes
     * @param center absolute Position des Spielers (Sichtfeldmitte)
     */
    public void setBild(int art, Koordinaten position, Koordinaten center) {
        int x, y;
        BufferedImage image;


        //  debug    //System.out.println(bilddatei.toString());
        //die relative Position wird ermittelt
        x = position.getX() - (center.getX() - this.getWidth() / (2 * quadrat));
        y = position.getY() - (center.getY() - this.getHeight() / (2 * quadrat));

        //das Bild wird aus dem Buffer geladen
        image = imageBuffer.ladeBild(GuiBildBeschaffer.FELD, art);

        if (image != null) {
            //das Bild wird der Renderliste hinzugefügt
            int lvltmp = art / 10;
            switch (lvltmp) {
                case Beschaffenheit.BERG:
                case Beschaffenheit.WALD:
                case Beschaffenheit.WASSER:
                    gspielfeld_3.add(new GSpielFeldElement(x, y, image));
                    break;
                //es wird  zusätzlich ein grüner Hintergrund gemalt 
                case Beschaffenheit.UFER:
                case Beschaffenheit.GEBUESCH:
                case Beschaffenheit.GEROELL:
                default:
                    gspielfeld_2.add(new GSpielFeldElement(x, y, image));
                    gspielfeld_1.add(new GSpielFeldElement(x, y, imageBuffer.ladeBild(GuiBildBeschaffer.FELD, Beschaffenheit.GRAS_0)));
                    break;



            }

            //   debug   //System.out.println("Bild eingefügt");
        }

    }

    /**
     * hier werden alle Elemente aus den Renderlisten auf die graphic gemalt,
     * danach werden die Renderlisten geleert
     *
     * wird durch repaint() aufgerufen
     *
     * @param g die Graphics
     */
    @Override
    protected synchronized void paintComponent(Graphics g) {

        //schwarzer Hintergrund wird gemalt
        g.setColor(Color.BLACK);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        g.fillRect(0, 0, d.width, d.height);
        super.paintComponent(g);

        //Felder werden gemalt
        GSpielFeldElement tmp;
        Iterator<GSpielFeldElement> it = gspielfeld_1.iterator();
        //Erst werden alle Elemente der Feldliste (level 1) gezeichnet
        while (it.hasNext()) {
            tmp = it.next();
            g.drawImage(tmp.getImage(), (tmp.getX() * quadrat) - (int) (quadrat * 0.25), (tmp.getY() * quadrat) - (int) (quadrat * 0.25), (int) (quadrat * 1.5), (int) (quadrat * 1.5), this);

            //  debug   //System.out.println("bild ausgelesen >> "+ tmp.getX()*quadrat+" - "+ tmp.getY()*quadrat+" - "+ this);
        }
        it = gspielfeld_2.iterator();
        //Erst werden alle Elemente der Feldliste (level 2) gezeichnet
        while (it.hasNext()) {
            tmp = it.next();
            g.drawImage(tmp.getImage(), (tmp.getX() * quadrat) - (int) (quadrat * 0.25), (tmp.getY() * quadrat) - (int) (quadrat * 0.25), (int) (quadrat * 1.5), (int) (quadrat * 1.5), this);
            //  debug   //System.out.println("bild ausgelesen >> "+ tmp.getX()*quadrat+" - "+ tmp.getY()*quadrat+" - "+ this);
        }
        it = gspielfeld_3.iterator();
        //Erst werden alle Elemente der Feldliste (level 3) gezeichnet
        while (it.hasNext()) {
            tmp = it.next();
            g.drawImage(tmp.getImage(), (tmp.getX() * quadrat) - (int) (quadrat * 0.25), (tmp.getY() * quadrat) - (int) (quadrat * 0.25), (int) (quadrat * 1.5), (int) (quadrat * 1.5), this);
            //  debug   //System.out.println("bild ausgelesen >> "+ tmp.getX()*quadrat+" - "+ tmp.getY()*quadrat+" - "+ this);
        }
        //dann werden die Items gezeichnet
        it = itemlist.iterator();
        while (it.hasNext()) {
            tmp = it.next();
            g.drawImage(tmp.getImage(), (tmp.getX() * quadrat) + (int) (quadrat * 0.1), (tmp.getY() * quadrat) + (int) (quadrat * 0.1), (int) (quadrat * 0.8), (int) (quadrat * 0.8), this);
            //  debug   //System.out.println("bild ausgelesen >> "+ tmp.getX()*quadrat+" - "+ tmp.getY()*quadrat+" - "+ this);
        }
        //und zum Schluss die Monster und Individuuen
        it = indlist.iterator();
        while (it.hasNext()) {
            tmp = it.next();
            g.drawImage(tmp.getImage(), (tmp.getX() * quadrat) + (int) (quadrat * 0.1), (tmp.getY() * quadrat) + (int) (quadrat * 0.1), (int) (quadrat * 0.8), (int) (quadrat * 0.8), this);
            //  debug   //System.out.println("bild ausgelesen >> "+ tmp.getX()*quadrat+" - "+ tmp.getY()*quadrat+" - "+ this);
        }
        //  debug   g.drawString("TEST", 10, 10);
        // am schluss erden die Listen geleert und vorbereitet
        //initPaintLists();

        //Meldungen werden gemalt
        MeldungTrigger.getDefaultMeldungTrigger().getMeldungen().paintComponent(g, d.width / 2 - (int) (600 * GuiBildBeschaffer.groessenVerhaeltnis()) - ((int) (0.125 * quadrat)), d.height / 30);
    }

    /**
     * Fügt ein Individuum (Monster) der Renderliste hinzu
     *
     * --Aufruf muss mit dieser Instanz syncronisiert sein!--
     *
     * @param art int id-schlüssel fürs Bild
     * @param position absolute Position des Bildes
     * @param center absolute Position des Spielers (Sichtfeldmitte)
     */
    public void setIndividuum(int art, Koordinaten position, Koordinaten center) {
        int x, y;
        BufferedImage image;


        //die relative Position wird ermittelt
        x = position.getX() - (center.getX() - this.getWidth() / (2 * quadrat));
        y = position.getY() - (center.getY() - this.getHeight() / (2 * quadrat));
        image = imageBuffer.ladeBild(GuiBildBeschaffer.INDIVIUUM, art);
        //das Bild wird aus dem Buffer geladen
        if (image != null) {
            //das Bild wird der Renderliste hinzugefügt
            indlist.add(new GSpielFeldElement(x, y, image));

        }
    }

    /**
     * Fügt ein Gegenstand (Item) der Renderliste hinzu
     *
     * --Aufruf muss mit diseer Instanz syncronisiert sein!--
     *
     * @param art int id-schlüssel fürs Bild
     * @param position absolute Position des Bildes
     * @param center absolute Position des Spielers (Sichtfeldmitte)
     */
    public void setGegenstand(int art, Koordinaten position, Koordinaten center) {
        int x, y;
        BufferedImage image;

        //die relative Position wird ermittelt
        x = position.getX() - (center.getX() - this.getWidth() / (2 * quadrat));
        y = position.getY() - (center.getY() - this.getHeight() / (2 * quadrat));
        image = imageBuffer.ladeBild(GuiBildBeschaffer.GEGENSTAND, art);
//das Bild wird aus dem Buffer geladen
        if (image != null) {
            //das Bild wird der Renderliste hinzugefügt
            itemlist.add(new GSpielFeldElement(x, y, image));
        }
    }

    /**
     * Leert die Renderlisten --Aufruf muss mit diseer Instanz syncronisiert
     * sein!--
     */
    public void kompletterRepaint() {
        //Leert die Listen

        gspielfeld_1.clear();
        gspielfeld_2.clear();
        gspielfeld_3.clear();
        itemlist.clear();
        indlist.clear();
        //maleEcken();





    }

    /**
     * baut die schwarzen Blöcke auserhalb des Sichtfeldes
     */
    private void maleEcken() {
        //Malt die schwarzen Ecken
        BufferedImage image = imageBuffer.ladeBild(GuiBildBeschaffer.FELD, -1);
        gspielfeld_1.add(new GSpielFeldElement(0, 0, image));
        gspielfeld_1.add(new GSpielFeldElement(1, 0, image));
        gspielfeld_1.add(new GSpielFeldElement(0, 1, image));

        gspielfeld_1.add(new GSpielFeldElement(9, 0, image));
        gspielfeld_1.add(new GSpielFeldElement(10, 0, image));
        gspielfeld_1.add(new GSpielFeldElement(10, 1, image));

        gspielfeld_1.add(new GSpielFeldElement(10, 9, image));
        gspielfeld_1.add(new GSpielFeldElement(10, 10, image));
        gspielfeld_1.add(new GSpielFeldElement(9, 10, image));

        gspielfeld_1.add(new GSpielFeldElement(0, 9, image));
        gspielfeld_1.add(new GSpielFeldElement(0, 10, image));
        gspielfeld_1.add(new GSpielFeldElement(1, 10, image));
    }

    /**
     * zeichnet ein einzelnes Feld neu
     *
     * @param position Position des Feldes
     * @param center Position des Spielers
     */
    public void feldneuzeichnen(Koordinaten position, Koordinaten center) {
        int x = position.getX() - (center.getX() - this.getWidth() / (2 * quadrat));
        int y = position.getY() - (center.getY() - this.getHeight() / (2 * quadrat));
        repaint(x * quadrat, y * quadrat, quadrat, quadrat);
    }

    /**
     * Entfernt ein Individuum (zB wenn es gelaufen ist)
     *
     * @param position Position des Feldes
     * @param center Position des Spielers
     */
    public void removeIndividuum(Koordinaten position, Koordinaten center) {
        int x = position.getX() - (center.getX() - this.getWidth() / (2 * quadrat));
        int y = position.getY() - (center.getY() - this.getHeight() / (2 * quadrat));
        indlist.remove(new GSpielFeldElement(x, y, null));
    }

    /**
     * Entfernt ein Gegenstand (zB wenn er aufgenommen wurde)
     *
     * @param position Position des Feldes
     * @param center Position des Spielers
     */
    public void removeGegenstand(Koordinaten position, Koordinaten center) {
        int x = position.getX() - (center.getX() - this.getWidth() / (2 * quadrat));
        int y = position.getY() - (center.getY() - this.getHeight() / (2 * quadrat));
        itemlist.remove(new GSpielFeldElement(x, y, null));
    }

    /**
     * Die intere Klasse zur Speicherung der Bilder und deren Position in den
     * Renderlisten
     */
    private class GSpielFeldElement {

        int x, y;
        BufferedImage image;

        public GSpielFeldElement(int x, int y, BufferedImage image) {
            this.x = x;
            this.y = y;
            this.image = image;

        }

        public BufferedImage getImage() {
            return image;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GSpielFeldElement elem = (GSpielFeldElement) obj;
            if (x == elem.getX() && y == elem.getY()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 97 * hash + this.x;
            hash = 97 * hash + this.y;
            return hash;
        }
    }
}
