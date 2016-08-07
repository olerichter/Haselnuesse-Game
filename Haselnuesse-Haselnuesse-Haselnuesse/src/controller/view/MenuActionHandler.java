package controller.view;

/**
 * Diese Interface sellt sicher das die ausgewählten Aktionen eines Menüs auch
 * umgesetzt werden.
 *
 * @version 0.8b
 * @author Ole Richter
 */
public interface MenuActionHandler {

    /**
     * wird aufgerufen wenn ENTER gedrückt wird
     *
     * @param AktionId
     */
    public void aktionAusfueren(int AktionId);

    /**
     * wird aufgerufen wenn ESC gedrückt wird
     */
    public void menuBeenden();
}
