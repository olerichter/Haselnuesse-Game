package data;

import controller.game.MapDataBeschaffer;
import controller.game.MonsterController;
import controller.toolkit.Koordinaten;
import controller.view.ViewController;
import java.io.Serializable;

/**
 * Die Klasse MapDataFeld repräsentiert ein Feld des Spielfeldes, es Speichert
 * seine Beschaffenheit (see Beschaffenheit) sowie seine Position im Spielfeld,
 * Individuuen und Gegenstände die auf ihm liegen.
 *
 * (ab v0.8) dieses Objekt ist nur so lange Aktiv, wie es sich in der aktiven
 * Zone befindetet, ansonsten werden seine Daten in einer Datenbank
 * abgespeichert.
 *
 * Durch die rekusive Struktur des Spielfeldes implementiert es auch die
 * Spielfeldlogik. so kann das Feld seine umliegenden Felder zurückliefern(ab
 * 0.2), Individuuen auf umliegende Felder verschieben(ab 0.3 relevant ab 0.6),
 * und sich selbst abspeichern lassen (ab 0.8)
 *
 *
 * @version 0.8b
 * @author Ole Richter
 */
public class MapDataFeld implements Serializable {

    //die Absolute Position des Feldes
    private int koordinaten_x, koordinaten_y;
    //Nachbarn
    private MapDataFeld oben, unten, rechts, links;
    //Platz für Individuum
    private Individuum individuum;
    //Platz für Gegenstand
    private Gegenstand gegenstand;
    //Art des Feldes
    private Beschaffenheit beschaffenheit;
    //Der ViewController
    private ViewController view;
    //TOD
    private boolean saving = false;
    //Zuständiger Controller
    private MapDataBeschaffer loader;

    /**
     * Erstellt neues Feld
     *
     * @param koordinaten_x absolute x Koordinate
     * @param koordinaten_y absolute y Koordinate
     * @param beschaffenheit art des Feldes
     */
    public MapDataFeld(int koordinaten_x, int koordinaten_y, Beschaffenheit beschaffenheit) {
        this.koordinaten_x = koordinaten_x;
        this.koordinaten_y = koordinaten_y;
        this.beschaffenheit = beschaffenheit;
    }

    /**
     * Liefert die Beschaffenheit
     *
     * @return beschaffenheit
     */
    public Beschaffenheit getBeschaffenheit() {
        return beschaffenheit;
    }

    /**
     * Liefert den Gegenstand falls vorhanden sonst null
     *
     * @return den Gegenstand oder null
     */
    public Gegenstand getGegenstand() {
        return gegenstand;
    }

    /**
     * setzt einen neuen Gegenstand (überschreibt) oder null falls übergeben
     * TODO Begehbarkeit abfangen
     *
     * @param gegenstand der neue Gegenstand
     */
    public void setGegenstand(Gegenstand gegenstand) {
        this.gegenstand = gegenstand;
        gegenstand.setPosition(this);
    }

    /**
     * Liefert das Individuum auf dem Feld oder null wenn keins vorhanden ist
     *
     * @return das individuum oder null
     */
    public Individuum getIndividuum() {
        return individuum;
    }

    /**
     * Setzt eine neues Individuum (überschreibt nicht!) auf das Feld wenn es
     * leer ist. liefert zurück, ob es das Individuum plazieren konnte
     *
     * @param individuum das zusetzende Individuum
     * @return true bei erfolgreicher platzeirung
     */
    public boolean setIndividuum(Individuum individuum) {
        //prüft ob das Feld leer und begeehbar ist
        if (beschaffenheit.getBegehbar() && this.individuum == null && isAlive()) {
            this.individuum = individuum;
            individuum.setPosition(this);
            view.getSpieler().checkKampf(individuum, null);
            view.feldChanged(this);
            return true;
        } else {
            return false;
        }
    }

    /**
     * entfernt das individuum auf dem Feld, wenn es mit dem Übergebenen
     * übereinstimmt (selbes Objekt)
     *
     * @param individuum das gelöscht werden soll
     */
    public void removeIndividuum(Individuum individuum) {
        if (individuum == this.individuum) {
            this.individuum = null;
            if (isAlive()) {
                view.feldChanged(this);
            }

        }

    }

    /**
     * bewegt das Individuum, das auf dem Feld sitzt, wenn es mit dem
     * Übergebenen übereinstimmt (selbes Objekt) in die übergebene Richtung,
     * wenn das neue Feld das Individuum nicht annimmt, bleibt es auf dem
     * aktuellen Feld sitzen.
     *
     * o = oben, u = unten, l = links, r = rechts
     *
     * Relevant ab 0.6
     *
     * @param Individuum das bewegt werden soll
     * @param richtung in die das Individuum bewegt werden soll: o = oben, u =
     * unten, l = links, r = rechts.
     */
    public void moveIndividuum(Individuum Individuum, char richtung) {
        updateAktiveZone();
        if (individuum == this.individuum && isAlive()) {
            switch (richtung) {
                case 'o':
                    if (getOben().setIndividuum(Individuum)) {
                        removeIndividuum(Individuum);
                    }
                    break;
                case 'u':
                    if (getUnten().setIndividuum(Individuum)) {
                        removeIndividuum(Individuum);
                    }
                    break;
                case 'r':
                    if (getRechts().setIndividuum(Individuum)) {
                        removeIndividuum(Individuum);
                    }
                    break;
                case 'l':
                    if (getLinks().setIndividuum(Individuum)) {
                        removeIndividuum(Individuum);
                    }
                    break;
            }
        }
    }

    /**
     * Liefert das linke Nachbarfeld zurück, falls es keine Refferenz hat lässt
     * es das Feld laden (see MapDataBeschaffer.getMapDataFeld())
     *
     * @return das linke Nachbarfeld
     */
    public MapDataFeld getLinks() {
        if (links == null || (links != null && !links.isAlive())) {
            links = loader.getMapDataFeld(new Koordinaten(koordinaten_x - 1, koordinaten_y));
        }
//         if(!links.isAlive())
//            System.out.println("NOT ALLiVE");
        return links;
    }

    /**
     *
     * Setzt das linke Nachbarfeld, Ist das übergebene Feld null und ist es
     * selber nicht in der aktiven Zone , löst es ein Speichern aus Sagt dem
     * Nachbarfeld Bescheid, dass es der neue Nachbar ist
     *
     * @param links linker Nachbar
     */
    private void setLinks(MapDataFeld links) {
        if (links == null) {
            updateAktiveZone();
        }
        this.links = links;
        if (this.links != links) {
            links.setRechts(this);
        }

    }

    /**
     * Liefert das obere Nachbarfeld zurück, falls es keine Referenz hat, lässt
     * es das Feld laden (see MapDataBeschaffer.getMapDataFeld())
     *
     * @return das obere Nachbarfeld
     */
    public MapDataFeld getOben() {
        if (oben == null || (oben != null && !oben.isAlive())) {
            oben = loader.getMapDataFeld(new Koordinaten(koordinaten_x, koordinaten_y - 1));
        }
//        if(!oben.isAlive())
//            System.out.println("NOT ALLiVE");
        return oben;
    }

    /**
     * Setzt das obere Nachbarfeld, Ist das übergebene Feld null und es selber
     * nicht in der aktiven Zone , löst es ein Speichern aus Sagt dem
     * Nachbarfeld Bescheid, dass es der neue Nachbar ist
     *
     * @param oben oberer Nachbar
     */
    private void setOben(MapDataFeld oben) {
        if (oben == null) {
            updateAktiveZone();
        }
        this.oben = oben;
        if (this.oben != oben) {
            oben.setUnten(this);
        }

    }

    /**
     * Liefert das rechte Nachbarfeld zurück, falls es keine Referenz hat, lässt
     * es das Feld laden (see MapDataBeschaffer.getMapDataFeld())
     *
     * @return das rechte Nachbarfeld
     */
    public MapDataFeld getRechts() {
        if (rechts == null || (rechts != null && !rechts.isAlive())) {
            rechts = loader.getMapDataFeld(new Koordinaten(koordinaten_x + 1, koordinaten_y));
        }
//         if(!rechts.isAlive())
//            System.out.println("NOT ALLiVE");
        return rechts;
    }

    /**
     * Setzt das rechte Nachbarfeld, Ist das übergebene Feld null und es selber
     * nicht in der aktiven Zone , löst es ein Speichern aus Sagt dem
     * Nachbarfeld Bescheid, dass es der neue Nachbar ist
     *
     * @param rechts rechter Nachbar
     */
    private void setRechts(MapDataFeld rechts) {
        if (rechts == null) {
            updateAktiveZone();
        }
        this.rechts = rechts;
        if (this.rechts != rechts) {
            rechts.setLinks(this);
        }

    }

    /**
     * Liefert das untere Nachbarfeld zurück, falls es keine Referenz hat, lässt
     * es das Feld laden (see MapDataBeschaffer.getMapDataFeld())
     *
     * @return das untere Nachbarfeld
     */
    public MapDataFeld getUnten() {
        if (unten == null || (unten != null && !unten.isAlive())) {
            unten = loader.getMapDataFeld(new Koordinaten(koordinaten_x, koordinaten_y + 1));
        }
//         if(!unten.isAlive())
//            System.out.println("NOT ALLiVE");
        return unten;
    }

    /**
     * Setzt das untere Nachbarfeld, Ist das übergebene Feld null und es selber
     * nicht in der aktiven Zone , löst es ein Speichern aus Sagt dem
     * Nachbarfeld Bescheid, dass es der neue Nachbar ist
     *
     * @param unten unterer Nachbar
     */
    private void setUnten(MapDataFeld unten) {
        if (unten == null) {
            updateAktiveZone();
        }
        this.unten = unten;
        if (this.unten != unten) {
            unten.setOben(this);
        }

    }

    /**
     * überprift ob das Feld abgespeichert werden muss.
     */
    public void updateAktiveZone() {
        if (!saving && view != null && loader != null) {
            saving = true;
            if (!view.inAktiverZone(getKoordinaten())) {
                loader.saveMe(getKoordinaten());
            }
//            if (rechts != null) rechts.updateAktiveZone();
//            if (links != null) links.updateAktiveZone();
//            if (oben != null) oben.updateAktiveZone();
//            if (unten != null) unten.updateAktiveZone(); 
            saving = false;
        }

    }

    /**
     * Setzt den MapDataBeschaffer
     *
     * @param loader
     */
    public void setLoader(MapDataBeschaffer loader) {
        this.loader = loader;
    }

    /**
     * Setzt den zuständigen View
     *
     * @param view ViewController
     */
    public void setView(ViewController view) {
        this.view = view;
    }

    /**
     * Liefert seine absolute Position zurück
     *
     * @return seine Position als Koordinaten
     */
    public Koordinaten getKoordinaten() {
        return new Koordinaten(koordinaten_x, koordinaten_y);
    }

    /**
     * löscht den auf ihm leigenden Gegenstand
     *
     * @param gegenstand zulöschnden Gegenstand
     */
    public void removeGegenstand(Gegenstand gegenstand) {
        if (gegenstand == this.gegenstand) {
            this.gegenstand = null;
            view.feldChanged(this);
        }



    }

    /*  @Override
     public Object clone(){
     try {
     return super.clone();
     } catch (CloneNotSupportedException ex) {
     Logger.getLogger(MapDataFeld.class.getName()).log(Level.SEVERE, null, ex);
     }
     return null;
     }
     */
    /**
     * Isoliert sich von seinen Nachbarn
     */
    public void loescheNachbarn() {
        if (rechts != null) {
            rechts.links = null;
        }
        if (links != null) {
            links.rechts = null;
        }
        if (oben != null) {
            oben.unten = null;
        }
        if (unten != null) {
            unten.oben = null;
        }
        oben = null;
        unten = null;
        links = null;
        rechts = null;
    }

    /**
     * Überprüft ob das feld Aktiv ist
     *
     * @return true wenn das Feld nicht abgespeichtert ist.
     */
    public boolean isAlive() {
        if (view != null && loader != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * liefert den IndividuuenController
     *
     * @return IndividuuenController
     */
    MonsterController getIndividuumController() {
        return loader.getIndividuenController();
    }

    /**
     * Liefert das Feld auf dem der Spieler sitzt
     *
     * @return die Position des Spielers
     */
    public MapDataFeld getSpielerPosition() {
        return loader.getMapDataFeld(loader.getSpielerPosition());
    }
}
