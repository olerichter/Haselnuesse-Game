package view;

import controller.view.MenuActionHandler;

/**
 * Das Menüelement ist ein Auswahlpunkt in der GUI-Komponente Menue
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class MenueElement {

    private String element;
    private MenuActionHandler handler;
    private int iD;

    /**
     * Erstellt einen Unterpunkt für ein Menü
     *
     * @param element der Text
     * @param handler der Aktionshandler
     * @param iD die AktionsId sie AktionsHandler
     */
    public MenueElement(String element, MenuActionHandler handler, int iD) {
        this.element = element;
        this.iD = iD;
        this.handler = handler;

    }

    /**
     * fürt die Aktion aus
     */
    public void aktionAusfuehren() {
        handler.aktionAusfueren(iD);
    }

    /**
     * liefert den Text
     *
     * @return den Text
     */
    public String getElement() {
        return element;
    }
}
