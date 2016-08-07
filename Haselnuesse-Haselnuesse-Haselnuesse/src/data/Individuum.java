package data;

import controller.toolkit.Koordinaten;
import java.io.Serializable;

/**
 * Die Klasse implementiert alle Grundeigenschaften eines Individuums für das
 * Spiel
 *
 * Ein Individuum kann sowohl ein Gegner (see <interface> Gegner) sein, mit dem
 * der Spieler kämpfen kann, als auch ein neutrales Individuum
 *
 * @version 0.9
 * @author Ole Richter
 */
public abstract class Individuum implements Serializable {

    /**
     *
     */
    public static final int FUCHS = 10, EICHHOERNCHEN_SCHWARZ = 20, STREIFENHOERNCHEN = 21, SPIELER = 1, MADER = 22, ELSTER = 30;
    /**
     * das Feld auf dem das Individuum sitzt
     */
    protected MapDataFeld position;
    /**
     * der Name des Individuums
     */
    protected String name;
    /**
     * die Art des Individuums - siehe Konstanten in dieser Klasse
     */
    protected int art;
    /**
     * Die Position bevor es den Spieler verfolgt hat
     */
    protected MapDataFeld letztePosition;
    /**
     * Ob der Spieler verfolgt wird
     */
    protected boolean spielerVerfolgen;

    /**
     * erstellt das Individuum referenziert die Position
     *
     * @param feld die Position
     */
    public Individuum(MapDataFeld feld) {
        position = feld;

    }

    /**
     * löst ein Abspeichern aus (ab 0.8)
     */
    public void save() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Setzt das Feld auf dem das Individuum sich gerade befindet
     *
     * @param aThis
     */
    public void setPosition(MapDataFeld aThis) {
        if (aThis != null) {
            position = aThis;
        }
    }

    /**
     * Sagt dem Feld in welche Richtung es sich bewegen möchte (see
     * MapDataFeld.moveIndividuum())
     *
     * @param c - (see MapDataFeld.moveIndividuum())
     */
    public void moveMe(char c) {
        position.moveIndividuum(this, c);
        //TODO perhaps this in hear is a problem because its a superclass
    }

    /**
     * gibt seine aktuelle Position als Koordinaten zurück
     *
     * @return seine aktuelle Position als Koordinaten
     */
    public Koordinaten getKoordinaten() {
        return position.getKoordinaten();
    }

    /**
     * Gibt die Art des Individuums zurück - siehe Konstanten in dieser Klasse
     *
     * @return eine Art-Konstante (dieser Klasse)
     */
    public int getArt() {
        return art;
    }

    /**
     * Gibt den Namen zurück
     *
     * @return den Namen
     */
    public String getName() {
        return name;
    }

    /**
     * Implemenitert simple randomierierte bewegungen
     */
    public void bewegen() {
        double rand = Math.random();
        if (rand < 0.05) {
            moveMe('u');
        } else if (rand < 0.1) {
            moveMe('r');
        } else if (rand < 0.15) {
            moveMe('l');
        } else if (rand < 0.2) {
            moveMe('o');
        }
    }

    /**
     * löscht alle Refernzen von sich
     */
    public void stirbt() {
        position.removeIndividuum(this);
        position.getIndividuumController().individuumDeaktivieren(this);
    }

    /**
     * überprüft ob der Spieler in Reicheite ist
     *
     * @param reichweite die Anzahl an Feldern
     * @return true wenn der Abstand kleiner als die Reichweite ist
     */
    protected boolean zielInRichweite(int reichweite) {
        if (position == null || Math.sqrt(Math.pow(position.getSpielerPosition().getKoordinaten().getX() - position.getKoordinaten().getX(), 2) + Math.pow(position.getSpielerPosition().getKoordinaten().getY() - position.getKoordinaten().getY(), 2)) >= reichweite) {

            return false;
        } else {
            return true;
        }
    }

    /**
     * bewegt das Individuum in Richtung dem übergebenen Ziel
     *
     * @param koordinaten die Koordinaten des Zieles
     */
    protected void bewegeRichtungZiel(Koordinaten koordinaten) {
        if (position.getKoordinaten().getX() <= koordinaten.getX() && position.getKoordinaten().getY() < koordinaten.getY()) {
            if (position.getUnten().getBeschaffenheit().getBegehbar()) {
                moveMe('u');
            } else {
                moveMe('r');
            }
        } else if (position.getKoordinaten().getX() > koordinaten.getX() && position.getKoordinaten().getY() < koordinaten.getY()) {
            if (position.getUnten().getBeschaffenheit().getBegehbar()) {
                moveMe('u');
            } else {
                moveMe('l');
            }
        } else if (position.getKoordinaten().getX() <= koordinaten.getX() && position.getKoordinaten().getY() > koordinaten.getY()) {
            if (position.getOben().getBeschaffenheit().getBegehbar()) {
                moveMe('o');
            } else {
                moveMe('r');
            }
        } else if (position.getKoordinaten().getX() > koordinaten.getX() && position.getKoordinaten().getY() > koordinaten.getY()) {
            if (position.getOben().getBeschaffenheit().getBegehbar()) {
                moveMe('o');
            } else {
                moveMe('l');
            }
        } else if (position.getKoordinaten().getY() == koordinaten.getY()) {
            if (position.getKoordinaten().getX() < koordinaten.getX()) {
                if (position.getRechts().getBeschaffenheit().getBegehbar()) {
                    moveMe('r');
                } else {
                    moveMe('u');
                }
            } else {
                if (position.getLinks().getBeschaffenheit().getBegehbar()) {
                    moveMe('l');
                } else {
                    moveMe('o');
                }
            }

        }

        if (position.getKoordinaten().equals(koordinaten)) {
            if (!spielerVerfolgen) {
                letztePosition = null;
            }

        }
    }
}
