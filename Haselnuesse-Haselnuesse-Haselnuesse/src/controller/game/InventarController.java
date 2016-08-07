package controller.game;

import controller.game.trigger.GameTrigger;
import controller.game.trigger.MeldungTrigger;
import controller.view.MenuActionHandler;
import controller.view.ViewController;
import data.Gegenstand;
import data.Inventar;
import java.util.Iterator;
import view.GegenstandListe;
import view.InventarView;
import view.Menue;
import view.MenueElement;

/**
 * Der InventarController ist für die Dastelleung des Inventars verantwortlich,
 * und fürt die Aktionen des Menüs im Inventar aus. der InventarController
 * verwaltet nicht explizit den Inventar des Spielers, sondern nur was mit
 * diesem passiert wenn der Inventar im Spielfenster angezeigt wird.
 *
 * @version 0.9
 * @author Ole Richter
 */
public class InventarController implements MenuActionHandler {

    /**
     * Aktionen des Inventars
     */
    public static final int ESSEN_HASELNUSS = 10, ESSEN_BUCHECKER = 11, ESSEN_WALLNUSS = 12, ESSEN_MANDEL = 20;
    private ViewController view;
    private GameTrigger masterTrigger;
    //Die GUI-Komponente
    private final InventarView inventarView;

    /**
     * Bereitet das anzeigen des Inventares vor
     *
     * @param view den ViewController
     * @param masterTrigger den GameTrigger
     */
    public InventarController(ViewController view, GameTrigger masterTrigger) {

        this.view = view;
        this.masterTrigger = masterTrigger;


        inventarView = new InventarView();
        inventarAufbauen();

    }

    /**
     * Führt die ausgewählte Aktion des Menüs aus
     *
     * @param AktionId Konstanten siehe diese Klasse
     */
    @Override
    public void aktionAusfueren(int AktionId) {
        switch (AktionId) {
            case ESSEN_BUCHECKER:
                view.getSpieler().getRucksack().loescheGegenstand(new Gegenstand(Gegenstand.BUCHEKERN), 1);
                view.getSpieler().addLeben(20);
                break;
            case ESSEN_HASELNUSS:
                view.getSpieler().getRucksack().loescheGegenstand(new Gegenstand(Gegenstand.HASELNUSS), 1);
                view.getSpieler().addLeben(80);
                break;
            case ESSEN_WALLNUSS:
                view.getSpieler().getRucksack().loescheGegenstand(new Gegenstand(Gegenstand.WALLNUSS), 1);
                view.getSpieler().addLeben(100);
                break;
            case ESSEN_MANDEL:
                view.getSpieler().getRucksack().loescheGegenstand(new Gegenstand(Gegenstand.MANDEL), 1);
                view.getSpieler().addMaxLeben(5);
                break;
        }
        inventarAufbauen();
    }

    /**
     * baut den inventar in der GUI-Komponente auf + das menü
     */
    private void inventarAufbauen() {
        //Menü
        Menue menue = new Menue("Aktionen", 4, masterTrigger, this);
        //Liste
        GegenstandListe gegenstandListe = new GegenstandListe();
        //Info-Text
        inventarView.setText("Rucksack:     " + view.getSpieler().getStatus() + " - " + view.getSpieler().getLevelStatus());
        //Rucksackinhalt Darsellen
        Inventar inventar = view.getSpieler().getRucksack();
        inventarView.setGegenstandListe(gegenstandListe);

        Iterator<Gegenstand> it = inventar.getInventar().iterator();
        while (it.hasNext()) {
            Gegenstand gegenstand = it.next();
            gegenstandListe.gegenstandHinzufuegen(gegenstand.getArt(), inventar.getGegenstandAnzahl(gegenstand) + "x " + gegenstand.getName());

        }
        //aktionen dem Menü hinzufügen
        inventarView.setMenue(menue);
        if (inventar.getGegenstandAnzahl(new Gegenstand(Gegenstand.MANDEL)) > 0) {
            menue.menueElementHinzufuegen(new MenueElement("Mandel essen (+5 Gesundheit)", this, ESSEN_MANDEL));
        }
        if (inventar.getGegenstandAnzahl(new Gegenstand(Gegenstand.BUCHEKERN)) > 0) {
            menue.menueElementHinzufuegen(new MenueElement("Buchecker essen (+20 Leben)", this, ESSEN_BUCHECKER));
        }
        if (inventar.getGegenstandAnzahl(new Gegenstand(Gegenstand.WALLNUSS)) > 0) {
            menue.menueElementHinzufuegen(new MenueElement("Wallnuss essen (+100 Leben)", this, ESSEN_WALLNUSS));
        }
        if (inventar.getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) > 0) {
            menue.menueElementHinzufuegen(new MenueElement("Haselnuss essen (+80 Leben)", this, ESSEN_HASELNUSS));
        }
        //Hilfe ausgeben
        MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "ESC - zurück zum Spiel, Pfeiltasten - auswählen, Enter - Aktion ausführen");

        inventarView.repaint();

    }

    /**
     * Schließt den Inventar wenn ESC gedrückt wurde
     */
    @Override
    public void menuBeenden() {
        view.aktiviereSpielfeld();
    }

    /**
     * leifert die GUI-Komponente des Inventars
     *
     * @return die GUI-Komponente des Inventars
     */
    public InventarView getInventarView() {
        return inventarView;
    }
}
