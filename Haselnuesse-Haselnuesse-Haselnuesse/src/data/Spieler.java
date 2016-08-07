package data;

import controller.game.Kampfsystem;
import controller.game.trigger.GameTrigger;
import controller.game.trigger.MeldungTrigger;
import controller.toolkit.Koordinaten;
import controller.view.ViewController;
import data.faehigkeiten.Beisen;
import data.faehigkeiten.Faehigkeit;
import data.faehigkeiten.HeilenBuchecker;
import data.faehigkeiten.HeilenHaselnuss;
import data.faehigkeiten.HeilenWallnuss;
import data.faehigkeiten.Kratzen;
import data.faehigkeiten.Werfen;
import data.individuen.Gegner;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Die Klasse repräsentiert das Model des Spielers
 *
 * Ein Spieler hat ein Feld auf dem er sitzt, Einen Rucksack in dem er
 * Gegenstände mitnehmen kann (ab v0.6) Eine Anzahl an Fähigkeiten die er im
 * Kampf einsetzen kann (ab v0.6)
 *
 * sowie Leben. (ab v0.6)
 *
 * Natürlich auch einen Namen.
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Spieler implements Serializable {

    //Lebenspunkte
    private int leben;
    //Gesundheit
    private int maxleben;
    //Position
    MapDataFeld position;
    //Rucksack
    private Inventar rucksack;
    //Kampffähigkeiten
    private HashSet<Faehigkeit> faehigkeiten;
    //Name
    private String name;
    //Trigger + View
    private GameTrigger mastertrigger;
    private ViewController view;
    //Das Level des Spiels
    private int level;

    /**
     * Instanziert den Spieler
     *
     * @param leben das Startleben des Spielers
     * @param maxleben das StartMaxLeben des Spielers
     * @param name den Name des Spielers
     * @param view - den aktuellen View
     * @param trigger - den aktuellen SpieleTrigger
     */
    public Spieler(int leben, int maxleben, String name, ViewController view, GameTrigger trigger) {
        this.leben = leben;
        this.maxleben = maxleben;
        this.rucksack = new Inventar(this);
        this.faehigkeiten = new HashSet<Faehigkeit>();
        this.name = name;

        this.view = view;
        mastertrigger = trigger;
        level = 1;

        //Startfähigkeiten
        faehigkeiten.add(new Beisen(1, true));
        faehigkeiten.add(new HeilenBuchecker());
        faehigkeiten.add(new HeilenHaselnuss());
        faehigkeiten.add(new HeilenWallnuss());

    }

    /**
     * liefert den Namen des Spielers
     *
     * @return den Namen
     */
    public String getName() {
        return name;
    }

    /**
     * Liefert das aktuelle Feld auf dem der Spieler sitzt
     *
     * @return das Feld
     */
    public MapDataFeld getPosition() {
        return position;
    }

    /**
     * setzt das Feld auf dem der Spieler sitzt
     *
     * @param position die neue Position
     */
    public void setPosition(MapDataFeld position) {
        this.position = position;
    }

    /**
     * Bewegt den Spieler in die angegebene Richtung o = oben, u = unten, l =
     * links, r = rechts
     *
     * Startet den Kampf wenn der Spieler auf ein Individuum trifft Sammelt
     * einen Gegenstand ein wenn der Spieler sich auf ihm befindet
     *
     * @param richtung o = oben, u = unten, l = links, r = rechts.
     * @return true wenn das Bewegen erfolgreich war
     */
    public boolean move(char richtung) {
        MapDataFeld feld;
        switch (richtung) {
            case 'o':
                feld = position.getOben();
                break;
            case 'u':
                feld = position.getUnten();
                break;
            case 'l':
                feld = position.getLinks();
                break;
            case 'r':
                feld = position.getRechts();
                break;
            default:
                return false;
        }
        //Prüfe ob Feld begehbar
        if (!feld.getBeschaffenheit().getBegehbar()) {
            return false;
        }
        if (feld.getIndividuum() != null) {
            //Kampf innalisieren
            checkKampf(feld.getIndividuum(), feld.getKoordinaten());

        }
        if (feld.getGegenstand() != null) {
            //gegenstand behandeln
            rucksack.addGegenstand(feld.getGegenstand(), 1);
        }
        position = feld;
        position.updateAktiveZone();
        return true;

    }

    /**
     * setzt den ViewController
     *
     * @param view den ViewController
     */
    public void setViewController(ViewController view) {
        this.view = view;
    }

    /**
     * Überprüft ob ein Kampf gestartet werden muss
     *
     * @param individuum der Gegner
     * @param spieler die Position des Spielers
     */
    void checkKampf(Individuum individuum, Koordinaten spieler) {
        if (position == null || individuum == null) {
            return;
        }
        if (spieler == null) {
            spieler = position.getKoordinaten();
        }

        if (individuum.getKoordinaten().equals(spieler)) {



            if (individuum instanceof Gegner) {
                Kampfsystem kampf;
                if (position.getBeschaffenheit().getBeschaffenheit() / 10 == Beschaffenheit.WALD || position.getBeschaffenheit().getBeschaffenheit() / 10 == Beschaffenheit.GEBUESCH) {
                    kampf = new Kampfsystem(this, (Gegner) individuum, mastertrigger, Kampfsystem.HINTERGRUND_WALD);
                } else {
                    kampf = new Kampfsystem(this, (Gegner) individuum, mastertrigger, Kampfsystem.HINTERGRUND_FELD);
                }

                view.aktiviereKampf(kampf);
            }

        }
    }

    /**
     * Liefert Name + Lebenspunke zurück
     *
     * @return String mit Namen und Lebenspunken
     */
    public String getStatus() {
        return name + " Leben: " + leben + "/" + maxleben;
    }

    /**
     * berechnet welche auswirkungen der Angriff hat und wendet sie an
     *
     * @param angriff die Fähigkeit mit der der Spieler angegriffen wird
     * @return die Statusmeldung der Verteidigung
     */
    public String verteidigen(Faehigkeit angriff) {

        //TODO more + diffterent stuff
        if (angriff != null) {
            if (angriff.getID() == Faehigkeit.KLAUEN) {
                Gegenstand gegenstand = null;
                int rand = (int) (rucksack.getInventar().size() * Math.random());
                Iterator<Gegenstand> it = rucksack.getInventar().iterator();
                if (it.hasNext()) {
                    for (int i = 0; i <= rand; i++) {
                        gegenstand = it.next();
                        if (!it.hasNext()) {
                            break;
                        }
                    }
                    rucksack.loescheGegenstand(gegenstand, 1);
                    return "-1 " + gegenstand.getName();
                } else {
                    return "";
                }
            }
            int schaden = angriff.getDmg();
            leben -= schaden;

            if (((double) leben / (double) maxleben) < 0.25) {
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.NEGATIV, "Dein Leben ist unter 25% - Heile dich!");
            }
            return schaden + "";
        }

        return "";
    }

    /**
     * liefert alle Kampffähigkeiten zurück
     *
     * @return die Fähigkeiten als Set.
     */
    public HashSet<Faehigkeit> getFaehigkeiten() {
        return faehigkeiten;
    }

    /**
     * leifet die Lebenspunkte zurück
     *
     * @return die aktuellen Lebenspunkte
     */
    public int getLeben() {
        return leben;
    }

    /**
     * setzt den Gametrigger
     *
     * @param mastertrigger GameTRigger
     */
    public void setMasterTrigger(GameTrigger mastertrigger) {
        this.mastertrigger = mastertrigger;
    }

    /**
     * liefert den Rucksack
     *
     * @return den Rucksack
     */
    public Inventar getRucksack() {
        return rucksack;
    }

    /**
     * fügt lebenspunkte hinzu
     *
     * @param i +Lebenspunkte
     */
    public void addLeben(int i) {
        if (i > 0) {
            leben += i;
            if (leben > maxleben) {
                leben = maxleben;
            }
        }
    }

    /**
     * leifert den aktuellen Status des Spiel-Levels zurück
     *
     * @return Beschreibung wie viele Nüsse noch benötigt werden.
     */
    public String getLevelStatus() {

        switch (getLevel()) {

            case 1:
                return rucksack.getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) + "/5 Haselnüsse gesammelt";
            case 2:
                return rucksack.getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) + "/7 Haselnüsse gesammelt";
            case 3:
                return rucksack.getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) + "/9 Haselnüsse gesammelt";
            case 4:
                return rucksack.getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) + "/12 Haselnüsse gesammelt";
            case 5:
                return rucksack.getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) + "/15 Haselnüsse gesammelt";
            case 6:
                return rucksack.getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) + "/27 Haselnüsse gesammelt";
            default:
                return "Unbekantes Level";
        }
    }

    /**
     * liefert das Level des Spiels, des Spieler hat kein Level. die "Level" des
     * Spielers sind in den Fähigkeitsleveln repräsentiert.
     *
     * @return das aktuelle Level des Spiels
     */
    public int getLevel() {
        return level;
    }

    /**
     * überprüft ob die Vorauseszungen für das nächste Level gegeben sind und
     * startet ggf das nächste level
     *
     * @return true wenn der Spieler ins nächste level aufgesiegen ist
     */
    public boolean checkLevelStatus() {
        int haselnuesse = 0;
        switch (getLevel()) {
            case 1:
                haselnuesse = 5;
                break;
            case 2:
                haselnuesse = 7;
                break;
            case 3:
                haselnuesse = 9;
                break;
            case 4:
                haselnuesse = 12;
                break;
            case 5:
                haselnuesse = 15;
                break;
            case 6:
                haselnuesse = 27;
                break;

        }
        //Überprüft vorraussetzung
        if (rucksack.getGegenstandAnzahl(new Gegenstand(Gegenstand.HASELNUSS)) >= haselnuesse) {
            //neues Levelvorbereiten
            rucksack.loescheGegenstand(new Gegenstand(Gegenstand.HASELNUSS), haselnuesse);
            level++;
            if (level >= 7) {
                //Spiel Ende
                MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.POSITIV, "Du hast 75 Haselnüsse verbuddelt, der Winter kann kommen - Gewonnen!");
                mastertrigger.spielEnde();
                return true;
            }
            maxleben += 20;
            //Meldungen + Fähigkeiten
            MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.POSITIV, "Level " + level + " - Der Winter naht, sammle Haselnüsse!");
            switch (level) {

                case 2:
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "Neue Fähigkeit: Kratzen - Neuer Gegner: Schwarzes Eichhörnchen");
                    faehigkeiten.add(new Kratzen(1, true));
                    break;
                case 3:
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "Neuer Gegenstand: Mandel (Maximale Lebenspunkte +5) - Neuer Gegner: Fuchs ");
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "Flüchte vor Füchsen in den dichten Wald! - Sie sind zu stark");
                    break;
                case 4:
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "Neue Fähigkeit: Kastanien werfen - Neuer Gegenstand: unreife Kastanie");
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "Neuer Gegner: Mader");
                    faehigkeiten.add(new Werfen(1, true));
                    break;
                case 5:
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "Neuer Gegenstand: Wallnuss (heilt +100 Lebenspunkte");
                    MeldungTrigger.getDefaultMeldungTrigger().neueMeldung(MeldungTrigger.TIP, "Neuer Gegner: Elster - Klaut Gegenstände aus deinem Rucksack");
                    break;

            }
            mastertrigger.neuesLevel();
            return true;
        } else {
            return false;
        }

    }

    /**
     * berechnet das durschschnittliche Level der Kampffähigkeiten
     *
     * @return das durschschnittliche Level der Kampffähigkeiten
     */
    public int getAngriffsLevel() {
        Iterator<Faehigkeit> it = faehigkeiten.iterator();
        int i = -2, lvl = 0;
        while (it.hasNext()) {
            Faehigkeit faehigkeit = it.next();
            lvl += faehigkeit.getLevel();
            i++;
        }
        return Math.round((float) lvl / (float) i);
    }

    /**
     * liefert die maximale Anzahl der Lebenspunkte
     *
     * @return die maximale Anzahl der Lebenspunkte
     */
    public int getMaxLeben() {
        return maxleben;
    }

    /**
     * Erhöht die Gesundheit
     *
     * @param i +Gesundheit
     */
    public void addMaxLeben(int i) {
        if (i > 0) {
            maxleben += i;
        }
    }
}
