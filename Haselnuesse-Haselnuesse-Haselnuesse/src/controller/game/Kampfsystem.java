package controller.game;

import controller.game.trigger.GameTrigger;
import controller.game.trigger.MeldungTrigger;
import controller.view.MenuActionHandler;
import controller.view.ViewController;
import data.Gegenstand;
import data.Individuum;
import data.Spieler;
import data.faehigkeiten.Faehigkeit;
import data.individuen.Gegner;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import view.GegenstandListe;
import view.KampfView;
import view.Menue;
import view.MenueElement;

/**
 * Das Kampfsystem kümmert sich um den Kampf zischen einem Gegner und dem
 * Spieler, es verwaltet auch die dazugehörige GUI-Komponente
 *
 * Das Kampfsystem erst ab Version 0.6
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Kampfsystem implements MenuActionHandler {

    /**
     * Die Konstante für den Hintergrund
     */
    public static final int HINTERGRUND_FELD = 1, HINTERGRUND_WALD = 2;
    //Spieler
    private Spieler spieler;
    //der Gegner
    private Gegner gegner;
    //die GUI-Komponente
    private KampfView kview;
    //der GUI-Contoller
    private ViewController view;
    //der GameTrigger
    private GameTrigger masterTrigger;
    //art des Hintergund - Konstanten oben
    private int hintergrund;

    /**
     * bereitet den Kampf vor
     *
     * @param spieler den Spieler
     * @param gegner denGegner (Indíviduum das Gegner implementiert)
     * @param masterTrigger der Trigger des Spiels
     * @param hintergrund den Hintergrund des Kampfschaiplatzes - Konstanten
     * siehe diese Klasse
     */
    public Kampfsystem(Spieler spieler, Gegner gegner, GameTrigger masterTrigger, int hintergrund) {
        this.spieler = spieler;
        this.gegner = gegner;
        this.masterTrigger = masterTrigger;
        this.masterTrigger.setModus(GameTrigger.KAMPF_MODUS);
        this.hintergrund = hintergrund;
    }

    /**
     * Startet den Kampf - aktiviert das Angriffsmenü
     *
     * @param kview die Gui-Komponente
     * @param view der GUI-Controller
     */
    public void start(KampfView kview, ViewController view) {
        this.kview = kview;
        this.kview.setOrt(hintergrund);
        this.view = view;
        kview.updateStatusGegner(gegner.getStatus());
        kview.updateStatusSpieler(spieler.getStatus());
        aktiviereAuswahlMenue();
        kview.repaint();

    }

    /**
     * Fürt einen angriff des Spielers aus, sowie danach den Angriff des
     * Gegners, über prüft ob der Spieler oder der Gegner stribt
     *
     * @param fa die Fähigkeit des Spielers
     */
    public void faehigkeitAusfuehren(Faehigkeit fa) {


        //abfrage der Spezialfähigkeiten
        switch (fa.getID()) {
            case Faehigkeit.HEILEN_BUCHECKER:
                view.getSpieler().getRucksack().loescheGegenstand(new Gegenstand(Gegenstand.BUCHEKERN), 1);
                view.getSpieler().addLeben(fa.getDmg());
                kview.maleHeilen(fa.toString());
                break;
            case Faehigkeit.HEILEN_HASELNUSS:
                view.getSpieler().getRucksack().loescheGegenstand(new Gegenstand(Gegenstand.HASELNUSS), 1);
                view.getSpieler().addLeben(fa.getDmg());
                kview.maleHeilen(fa.toString());
                break;
            case Faehigkeit.HEILEN_WALLNUSS:
                view.getSpieler().getRucksack().loescheGegenstand(new Gegenstand(Gegenstand.WALLNUSS), 1);
                view.getSpieler().addLeben(fa.getDmg());
                kview.maleHeilen(fa.toString());
                break;
            case Faehigkeit.WERFEN:
                view.getSpieler().getRucksack().loescheGegenstand(new Gegenstand(Gegenstand.UNREIFE_KASTANIE), 1);
            //Normale Angriffe
            default:
                String gegner_text = gegner.verteidigen(fa);
                fa.benutzt();
                kview.updateStatusGegner(gegner.getStatus());
                kview.maleAngriff(true, fa.toString(), gegner_text);
        }




        kview.repaint();
        try {
            masterTrigger.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Kampfsystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Spielerangriff ende
        kview.angriffEnde();
        kview.repaint();
        try {
            masterTrigger.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Kampfsystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Angriff Gegner
        fa = gegner.getAngriff();
        String spieler_text = spieler.verteidigen(fa);
        if (fa != null) {
            fa.benutzt();
        }
        kview.updateStatusGegner(gegner.getStatus());
        kview.updateStatusSpieler(spieler.getStatus());
        if (fa != null) {
            kview.maleAngriff(false, spieler_text, fa.toString());
        }
        kview.repaint();
        try {
            masterTrigger.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Kampfsystem.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Angriff ende
        kview.angriffEnde();
        kview.repaint();
        try {
            masterTrigger.sleep(500);
        } catch (InterruptedException ex) {
            Logger.getLogger(Kampfsystem.class.getName()).log(Level.SEVERE, null, ex);
        }

        //überprüfen ob Spieler oder Gegner tot 
        if (gegner.getLeben() <= 0 || spieler.getLeben() <= 0) {
            // LOOT oder GAMEOVER


            if (spieler.getLeben() > 0) {
                //Loot
                GegenstandListe gegenstandListe = new GegenstandListe();
                int i = (int) (4 * controller.toolkit.Zubehoer.randomGauss(2)) + 1;
                for (int j = 0; j < i; j++) {
                    Gegenstand gegenstand = Gegenstand.randomGegenstand(spieler.getLevel());
                    spieler.getRucksack().addGegenstand(gegenstand, 1);
                    gegenstandListe.gegenstandHinzufuegen(gegenstand.getArt(), gegenstand.getName());
                }


                kview.setBelohnung(gegenstandListe);
                kview.repaint();

                try {
                    masterTrigger.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Kampfsystem.class.getName()).log(Level.SEVERE, null, ex);
                }
                view.kampfEnde(true, (Individuum) gegner);
            } else {
                //GameOver
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.NEGATIV, "Sie haben verloren");
                try {
                    masterTrigger.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Kampfsystem.class.getName()).log(Level.SEVERE, null, ex);
                }
                view.kampfEnde(false, null);
            }

        } else {
            //neuer Angriff zu wählen
            aktiviereAuswahlMenue();
            kview.repaint();
        }

    }

    /**
     * baut das Angriffsmenü
     */
    private void aktiviereAuswahlMenue() {

        Menue menue = new Menue("Nächste Attake", 5, masterTrigger, this);
        Iterator<Faehigkeit> it = spieler.getFaehigkeiten().iterator();
        while (it.hasNext()) {
            Faehigkeit faehigkeit = it.next();
            //überprüft Spezialfähigkeiten
            if (faehigkeit.getID() == Faehigkeit.HEILEN_BUCHECKER && spieler.getRucksack().getGegenstandAnzahl(new Gegenstand(Gegenstand.BUCHEKERN)) > 0) {
                menue.menueElementHinzufuegen(new MenueElement(faehigkeit.getBeschreibeung(), this, faehigkeit.getID()));
            } else if (faehigkeit.getID() == Faehigkeit.HEILEN_HASELNUSS && spieler.getRucksack().getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) > 0) {
                menue.menueElementHinzufuegen(new MenueElement(faehigkeit.getBeschreibeung(), this, faehigkeit.getID()));
            } else if (faehigkeit.getID() == Faehigkeit.HEILEN_WALLNUSS && spieler.getRucksack().getGegenstandAnzahl(new Gegenstand(Gegenstand.WALLNUSS)) > 0) {
                menue.menueElementHinzufuegen(new MenueElement(faehigkeit.getBeschreibeung(), this, faehigkeit.getID()));
            } else if (faehigkeit.getID() == Faehigkeit.WERFEN && spieler.getRucksack().getGegenstandAnzahl(new Gegenstand(Gegenstand.UNREIFE_KASTANIE)) > 0) {
                menue.menueElementHinzufuegen(new MenueElement(faehigkeit.getBeschreibeung(), this, faehigkeit.getID()));
            } //Normale Angriffe
            else if (faehigkeit.getID() != Faehigkeit.HEILEN_BUCHECKER && faehigkeit.getID() != Faehigkeit.HEILEN_HASELNUSS && faehigkeit.getID() != Faehigkeit.WERFEN && faehigkeit.getID() != Faehigkeit.HEILEN_WALLNUSS) {
                menue.menueElementHinzufuegen(new MenueElement(faehigkeit.getBeschreibeung(), this, faehigkeit.getID()));
            }
        }
        kview.setMenue(menue);
    }

    /**
     * fürt den ausgewählten Angriff aus
     *
     * @param AktionId die ID der Aktion - Konstanten siehe Faehigkeit
     */
    @Override
    public void aktionAusfueren(int AktionId) {
        kview.setMenue(null);
        Iterator<Faehigkeit> it = spieler.getFaehigkeiten().iterator();
        while (it.hasNext()) {
            Faehigkeit faehigkeit = it.next();
            if (faehigkeit.getID() == AktionId) {
                faehigkeitAusfuehren(faehigkeit);
                break;
            }

        }
    }

    /**
     * Tut nichts
     */
    @Override
    public void menuBeenden() {
//        kampfmodus kann nicht verlassen werden
    }

    /**
     * liefert die Art des Gegners
     *
     * @return die Kategorie
     * @see Individuum
     */
    public int getGegnerArt() {
        return ((Individuum) gegner).getArt();
    }
}
