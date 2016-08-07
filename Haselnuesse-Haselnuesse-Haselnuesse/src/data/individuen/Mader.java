package data.individuen;

import data.Individuum;
import data.MapDataFeld;
import data.Spieler;
import data.faehigkeiten.Beisen;
import data.faehigkeiten.Faehigkeit;
import data.faehigkeiten.Werfen;

/**
 * eine Implementation eines Maders - stärkster besiegbarer Gegner
 *
 * @version 0.9
 * @author Ole Richter
 */
public class Mader extends Individuum implements Gegner {

    private int leben;
    private Faehigkeit faehigkeit;
    private Faehigkeit spezialfaehigkeit;

    /**
     * Instanziert einen Mader
     *
     * @param feld refferrenziert die aktuelle Position
     * @param spieler spieler für schwierigkeits Abstimmung (wird nicht
     * refferenziert)
     */
    public Mader(MapDataFeld feld, Spieler spieler) {
        super(feld);
        name = "Mader";
        art = Individuum.MADER;
        leben = (int) (spieler.getMaxLeben() * 0.8);
        faehigkeit = new Beisen(spieler.getAngriffsLevel(), false);
        spezialfaehigkeit = new Werfen(spieler.getAngriffsLevel() + 1, false);
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public String verteidigen(Faehigkeit fa) {
        if (fa != null) {
            int schaden = fa.getDmg();
            leben -= schaden;
            return schaden + "";
        }

        return "";
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public String getStatus() {
        if (leben <= 0) {
            return name + " wurde besiegt";
        } else {
            return name + " Leben: " + leben + " Dmg: " + (spezialfaehigkeit != null ? spezialfaehigkeit.getDmgBeschreibung() : faehigkeit.getDmgBeschreibung());
        }
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public Faehigkeit getAngriff() {

        if (leben > 0) {
            if (spezialfaehigkeit != null) {
                Faehigkeit sp = spezialfaehigkeit;
                spezialfaehigkeit = null;
                return sp;
            }
            faehigkeit.benutzt();
            return faehigkeit;
        } else {
            return null;
        }
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public int getLeben() {
        return leben;
    }

    /**
     * Gibt dem Monster den Befehl sich zu bewegen, außerdem muss es beim
     * zusammenstoß mit dem Spieler einen kampf starten, Arbeitet mit
     * Sichtradien.
     *
     * Sichtradien: Verfolgen = 4 Endecken = 3
     *
     * (In Individuum ist diese Methode auch implementiert, aber diese muss
     * überschreiben werden, um Verfolgungsverhalten zu ermöglichen)
     */
    @Override
    public void bewegen() {
        //Prüft ob es den Spieler vefolgen soll
        if (spielerVerfolgen && zielInRichweite(4)) {
            bewegeRichtungZiel(position.getSpielerPosition().getKoordinaten());
        } else if (spielerVerfolgen) {
            spielerVerfolgen = false;
            bewegeRichtungZiel(letztePosition.getKoordinaten());
        }//Prüft ob es den Spieler vefolgen soll
        else if (zielInRichweite(3)) {
            spielerVerfolgen = true;
            letztePosition = position;
            bewegeRichtungZiel(position.getSpielerPosition().getKoordinaten());
        } else if (letztePosition != null) {
            bewegeRichtungZiel(letztePosition.getKoordinaten());
        } else {
            super.bewegen();
        }
    }
}
