package view;

import controller.game.trigger.MeldungTrigger;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import view.bilder.GuiBildBeschaffer;

/**
 * Die MeldungenListe Ist die GUI-Komponente , sie ist für spielweite Meldungen
 * zuständig - Es wird eine Render-Komponente benötigt
 *
 * @version 0.9
 * @author Ole Richter
 */
public class MeldungenListe implements GuiKomponente {

    private ArrayList<Element> liste;
    private JComponent vater;

    /**
     * Bereitet die Liste vor.
     */
    public MeldungenListe() {
        liste = new ArrayList<Element>();

    }

    /**
     * Setzt die GUI-Render-Komponente in der es gezeichnet werden soll
     *
     * @param vater GUI-Render-Komponente (JComponent)
     */
    @Override
    public void setVater(JComponent vater) {
        this.vater = vater;
    }

    /**
     * Fügt eine Meldung der GUI hinzu
     *
     * @param art die Hintergrungfarbe - siehe MeldungTrigger für Konstanten
     * @param nachricht die Meldung.
     */
    public synchronized void neueMeldung(int art, String nachricht) {
        if (!liste.contains(new Element(nachricht, art))) {
            liste.add(new Element(nachricht, art));
            if (vater != null) {
                vater.repaint();
            }
        }
    }

    /**
     * Entfernt eine Meldung von der GUI
     *
     * @param string die zuentfernende Meldung
     */
    public synchronized void entferne(String string) {
        Iterator<Element> it = liste.iterator();
        while (it.hasNext()) {
            Element element = it.next();
            if (element.getNachricht().equals(string)) {
                it.remove();
                break;
            }

        }
        if (vater != null) {
            vater.repaint();
        }
    }

    /**
     * Zeichnet die Meldungsliste auf eine übergebene Graphics an die übergebene
     * Stelle
     *
     * @param g die Graphics auf die gezeicnet werden soll
     * @param x die X Koordinate der Linken-Oberen-Ecke
     * @param y die Y Koordinate der Linken-Oberen-Ecke
     */
    @Override
    public synchronized void paintComponent(Graphics g, int x, int y) {

        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (30 * GuiBildBeschaffer.groessenVerhaeltnis())));
        g.setColor(Color.BLACK);
        Iterator<Element> it = liste.iterator();
        while (it.hasNext()) {
            Element element = it.next();

            switch (element.getArt()) {
                case MeldungTrigger.POSITIV:
                    g.setColor(Color.GREEN);
                    break;
                case MeldungTrigger.NEGATIV:
                    g.setColor(Color.RED);
                    break;
                default:
                    g.setColor(Color.YELLOW);
            }


            g.fillRect(x, y, (int) (1200 * GuiBildBeschaffer.groessenVerhaeltnis()), (int) (60 * GuiBildBeschaffer.groessenVerhaeltnis()));
            //g.drawImage(bild, x, y,(int)(50*GuiBildBeschaffer.groessenVerhaeltnis()),(int)(50*GuiBildBeschaffer.groessenVerhaeltnis()), null);
            y += (int) (40 * GuiBildBeschaffer.groessenVerhaeltnis());
            g.setColor(Color.BLACK);
            g.drawString(element.getNachricht(), x + (int) (45 * GuiBildBeschaffer.groessenVerhaeltnis()), y);
            y += (int) (40 * GuiBildBeschaffer.groessenVerhaeltnis());


        }

    }

    /**
     * eine Klasse für speicherung der Meldungen + Farbe
     */
    private class Element {

        private String nachricht;
        private int positiv;

        public Element(String nachricht, int positiv) {
            this.nachricht = nachricht;
            this.positiv = positiv;
        }

        public String getNachricht() {
            return nachricht;
        }

        public int getArt() {
            return positiv;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Element other = (Element) obj;
            if ((this.nachricht == null) ? (other.nachricht != null) : !this.nachricht.equals(other.nachricht)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 19 * hash + (this.nachricht != null ? this.nachricht.hashCode() : 0);
            return hash;
        }
    }
}
