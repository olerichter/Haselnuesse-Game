package view;

import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * definiert was eine GUI-Komponente der GUI-Render-Komponente (JComponent)
 * zurverfügungstellen muss.
 *
 *
 * sie Benötigt eine Render komponente (JComponent)
 *
 * @version 0.9
 * @author Ole Richter
 */
public interface GuiKomponente {

    /**
     * zeichnet die Komponente auf die übergebene Graphics an den angegebenen
     * Koordinaten
     *
     * @param g die Graphics auf die gezeichnet werden soll
     * @param x die X Koordinate der Linken-Oberen-Ecke
     * @param y die Y Koordinate der Linken-Oberen-Ecke
     */
    public void paintComponent(Graphics g, int x, int y);

    /**
     * setzt die GUI-Render-Komponente um ggf. einen Repaint auszulösen.
     *
     * @param vater die GUI-Render-Komponente
     */
    public void setVater(JComponent vater);
}
