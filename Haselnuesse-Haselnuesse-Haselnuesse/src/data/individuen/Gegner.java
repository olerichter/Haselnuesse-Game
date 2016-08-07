package data.individuen;

import data.faehigkeiten.Faehigkeit;

/**
 * Das interface Gegner müssen alle kämpfenden Individuuen implementieren, es
 * Gewährleistet das alle Methoden für den Kampf (ab 0.6/0.7) ordnungsgemäß
 * implementeirt sind.
 *
 * Das Interface ist nur in verbindung mit der abstraken Klasse Individuum zu
 * benutzen!
 *
 * @version 0.8b
 * @author Ole Richter
 */
public interface Gegner {

//    /**
//     * @deprecated bereits in Individuum implementiert ab (0.8) speichert TODO
//     * wenn Möglich entfernen
//     */
//    public void save();
//
//    /**
//     * @deprecated bereits in Individuum implementiert TODO wenn Möglich
//     * entfernen
//     * @param aThis
//     */
//    public void setPosition(MapDataFeld aThis);
//
//    /**
//     * @deprecated bereits in Individuum implementiert TODO wenn Möglich
//     * entfernen
//     */
//    public Koordinaten getKoordinaten();
//
    /**
     * Gibt dem Monster den Befehl sich zu bewegen, außerdem muss es beim
     * zusammenstoß mit dem Spieler einen kampf starten, Arbeitet mit
     * Sichtradien.
     *
     * In Individuum ist diese Methode auch implementiert, aber diese muss
     * überschreiben werden, um Verfolgungsverhalten zu ermöglichen
     */
    public void bewegen();

    /**
     * Berechnet die Auswirkungen der übergebenen Fähigkeit und wendet sie an.
     *
     * @param fa die angrifende Fähigkeit
     * @return Eine Statusmeldung über die Auswirkungen des Angriffs
     */
    public String verteidigen(Faehigkeit fa);

    /**
     * Liefert den Status des Individuums - Name + Lebenspunkte + aktueller
     * Schaden
     *
     * @return Name + Lebenspunkte + aktueller Schaden
     */
    public String getStatus();

    /**
     * Liefert die Fähigkeit mit dem das Individuum angreift.
     *
     * @return den Angriff
     */
    public Faehigkeit getAngriff();

    /**
     * Liefert die aktuellen Lebenspunkte
     *
     * @return die aktuellen Lebenspunkte
     */
    public int getLeben();
    //public int getArt();
}
