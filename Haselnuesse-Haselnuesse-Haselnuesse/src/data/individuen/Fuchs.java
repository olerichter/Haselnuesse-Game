package data.individuen;

import data.Beschaffenheit;
import data.Individuum;
import data.MapDataFeld;
import data.Spieler;
import data.faehigkeiten.Beisen;
import data.faehigkeiten.Faehigkeit;

/**
 * eine Implementation eines Fuchs - sehr Stark, nicht besiegbar.
 *
 * @version 0.7
 * @author Ole Richter
 */
public class Fuchs extends Individuum implements Gegner {

    private int leben;
    private Faehigkeit faehigkeit;

    /**
     * Instanziert einen Fuchs
     *
     * @param feld refferrenziert die aktuelle position
     * @param spieler spieler für schwierigkeits Abstimmung (wird nicht
     * refferenziert)
     */
    public Fuchs(MapDataFeld feld, Spieler spieler) {
        super(feld);
        name = "Fuchs";
        art = Individuum.FUCHS;
        leben = 1000;
        faehigkeit = new Beisen(200, false);
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
            return name + " Leben: " + leben + " Dmg: " + faehigkeit.getDmgBeschreibung();
        }
    }

    //Erbt das JavaDoc des Interfaces - verhält sich konform
    @Override
    public Faehigkeit getAngriff() {
        if (leben > 0) {
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
     * Sichtradien: Verfolgen = 10 Endecken = 3
     *
     * (In Individuum ist diese Methode auch implementiert, aber diese muss
     * überschreiben werden, um Verfolgungsverhalten zu ermöglichen)
     */
    @Override
    public void bewegen() {
        //Prüft ob es den Spieler vefolgen soll
        if (spielerVerfolgen && zielInRichweite(10)) {
            bewegeRichtungZiel(position.getSpielerPosition().getKoordinaten());
            if (zielInRichweite(2) && position.getSpielerPosition().getBeschaffenheit().getBeschaffenheit() / 10 == Beschaffenheit.WALD) {
                spielerVerfolgen = false;
                bewegeRichtungZiel(letztePosition.getKoordinaten());
            }
        } else if (spielerVerfolgen) {
            spielerVerfolgen = false;
            bewegeRichtungZiel(letztePosition.getKoordinaten());
        }//Prüft ob es den Spieler vefolgen soll
        else if (zielInRichweite(3) && position.getSpielerPosition().getBeschaffenheit().getBeschaffenheit() / 10 != Beschaffenheit.WALD) {
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
