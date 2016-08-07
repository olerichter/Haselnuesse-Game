package view;

import controller.game.trigger.GameTrigger;
import controller.view.MenuActionHandler;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JComponent;
import view.bilder.GuiBildBeschaffer;

/**
 * Das Menu ist eine GUI-Komponente, die ein Menü an einer bestimmten Stelle
 * malen kann benötigt eine Render-Komponente
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Menue implements GuiKomponente {

    // Die Liste der Unterpunkte
    private ArrayList<MenueElement> liste;
    //der ausgewälte Unterpunkt
    private int aktuellePosition;
    //der oberste Punkt im Menü
    private int startPosition;
    //Text oberhalb des Menüs
    private String oben;
    //Text unterhalb des Menüs
    private String unten;
    //die Höhe der Unterpunkte
    private int groesse;
    //die Render-Komponente
    private JComponent vater;
    // die maximale Zahl der angezeigten Elemente
    private int maxAngezeigteElemente;
    //der Trigger
    private final GameTrigger masterTrigger;
    //der Handler der die Aktionen ausführt
    private MenuActionHandler handler;

    /**
     * Bereitet die GUI-Komponente vor
     *
     * @param oben Test der oben im Menü stehen soll
     * @param maxAngezeigteElemente maximale länge des Menüs
     * @param masterTrigger der GameTrigger
     * @param handler der Handler der die Aktionen ausführt
     */
    public Menue(String oben, int maxAngezeigteElemente, GameTrigger masterTrigger, MenuActionHandler handler) {
        this.handler = handler;
        this.groesse = (int) (45 * GuiBildBeschaffer.groessenVerhaeltnis());
        this.masterTrigger = masterTrigger;
        this.oben = "\u21D1  " + oben + ":";
        this.maxAngezeigteElemente = maxAngezeigteElemente;
        liste = new ArrayList<MenueElement>();
        aktuellePosition = 0;
        startPosition = 0;
        masterTrigger.setMenue(this);

    }

    //Erbt JavaDoc vom interface
    @Override
    public void setVater(JComponent vater) {
        this.vater = vater;
    }

    /**
     * Liefert die Position die gerade Aktiv ist.
     *
     * @return die Position die gerade Aktiv ist
     */
    public int getAktuellePosition() {
        return aktuellePosition;
    }

    /**
     * fügt dem Menü einenen Unterpunkt hinzu
     *
     * @param elem der unterpunkt
     */
    public void menueElementHinzufuegen(MenueElement elem) {
        liste.add(elem);
        if (vater != null) {
            vater.repaint();
        }
    }

    //ERbt JavaDoc von Interface
    @Override
    public synchronized void paintComponent(Graphics g, int x, int y) {
        //setzt die Schriftgröße
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, (int) (37 * GuiBildBeschaffer.groessenVerhaeltnis())));
        //malt den Hintergrund
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(x, y, (int) (550 * GuiBildBeschaffer.groessenVerhaeltnis()), (int) (25 * GuiBildBeschaffer.groessenVerhaeltnis()) + groesse * ((maxAngezeigteElemente < liste.size() ? maxAngezeigteElemente : liste.size()) + 2));
        //setzt die Schriftfarbe + malt oberen Text
        g.setColor(Color.DARK_GRAY);
        y += groesse;
        x += (int) (25 * GuiBildBeschaffer.groessenVerhaeltnis());
        g.drawChars(oben.toCharArray(), 0, oben.length(), x, y);

        //Unterpunkte werden gemalt
        int i = 0;
        int gezeichnent = 0;
        Iterator<MenueElement> it = liste.iterator();
        boolean zeichnen = false;
        while (it.hasNext()) {
            MenueElement elem = it.next();
            if (i == startPosition) {
//                if (zeichnen) {
//                    break;
//                }
                zeichnen = true;
            }
            if (zeichnen && gezeichnent < maxAngezeigteElemente) {
                gezeichnent++;
                if (i == aktuellePosition) {
                    g.setColor(Color.RED);
                }
                g.drawChars(elem.getElement().toCharArray(), 0, elem.getElement().length(), x, y + (gezeichnent) * groesse);
                if (i == aktuellePosition) {
                    g.setColor(Color.DARK_GRAY);
                }
            }

//            if (!it.hasNext() && gezeichnent <= maxAngezeigteElemente) {
//                it = liste.iterator();
//                i = 0;
//            } else {
//                i++;
//            }
            i++;

        }
        //Unterer Text wird gemalt
        if (liste.size() > maxAngezeigteElemente) {
            if (liste.size() - gezeichnent == 1) {
                unten = "\u21D3 (1 verstektes Element)";
            } else {
                unten = "\u21D3 (" + (liste.size() - gezeichnent) + " verstekte Elemente)";
            }

        } else {
            unten = "\u21D3 ";
        }
        //System.out.println(gezeichnent+" - "+i+" - "+liste.isEmpty());
        g.drawChars(unten.toCharArray(), 0, unten.length(), x, y + (gezeichnent + 1) * groesse);



    }

    /**
     * fürt die aktuelle Aktion aus
     */
    public void aktionAusfueren() {
        liste.get(aktuellePosition).aktionAusfuehren();
    }

    /**
     * bewegt die aktuelle Position ein schritt nach unten
     */
    public void runter() {
        aktuellePosition++;
        //überprüft ob von das menü einen Punkt nach unten verschoben werden muss
        if (aktuellePosition == (maxAngezeigteElemente + startPosition) && liste.size() > maxAngezeigteElemente) {
            if (aktuellePosition < liste.size()) {
                startPosition++;
            }
        }
        //überprüft ob nach oben gesprungen werden muss
        if (aktuellePosition >= liste.size()) {
            aktuellePosition = 0;
            startPosition = 0;
        }
        //neumalen
        if (vater != null) {
            vater.repaint();
        }
    }

    /**
     * bewegt die aktuelle Position ein Schritt nach oben
     */
    public void hoch() {
        aktuellePosition--;
        //überprüft ob von das menü einen Punkt nach oben verschoben werden muss
        if (aktuellePosition < startPosition && liste.size() > maxAngezeigteElemente) {
            startPosition--;
            if (startPosition < 0) {
                startPosition = 0;
            }
        }
        //überprüft ob nach unten gesprungen werden muss
        if (aktuellePosition < 0) {
            aktuellePosition = liste.size() - 1;
            if (liste.size() > maxAngezeigteElemente) {
                startPosition = aktuellePosition - maxAngezeigteElemente + 1;
            }
        }
        //neumalen
        if (vater != null) {
            vater.repaint();
        }
    }

    /**
     * beendet das Menü
     */
    public void verlassen() {
        handler.menuBeenden();
    }
}
