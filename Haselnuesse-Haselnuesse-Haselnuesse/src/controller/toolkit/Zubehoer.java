package controller.toolkit;

import controller.game.MapDataBeschaffer;
import data.Beschaffenheit;
import data.MapDataFeld;

/**
 * Das Zubehoer beinhaltet statische Methoden, die aus allen Klassen des Spiels
 * aufgerufen werden können, meist einen Servicecharakter haben und somit
 * vielfälltig verwendbar sind.
 *
 * @version 0.7
 * @author Ole Richter
 *
 */
public class Zubehoer {

    /**
     * Wandelt ein 2D-INT-Array in MapDataFeld objekte um und speichert sie in
     * die angegebene Datenbankverbindung
     *
     * @param array das int[width][hight]
     * @param db die datenbank verbindung
     * @param xoffset die absoluten Koordinaten des Quadranten
     * @param yoffset die absoluten Koordinaten des Quadranten
     * @param width die breite des arrays
     * @param hight die höhe des arrays
     */
    public static void arrayToDB(int[][] array, MapDataBeschaffer db, int xoffset, int yoffset, int width, int hight) {
        MapDataFeld feld;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < hight; j++) {
                Koordinaten k = new Koordinaten(i + xoffset, j + yoffset);
                db.neuesFeld(k, new MapDataFeld(i + xoffset, j + yoffset, new Beschaffenheit(array[i][j])));
            }
        }

    }

    /**
     * Ist eine Randommethode, die wahrscheinlich einen Wert um 0.5 zurückgibt
     * und sehr selten einen um 0 oder 1 diese Methode liefert in Version 0.5
     * nur normale Randomwerte, die Breite wird ignoriert
     *
     * @param breite die Breite der Gaussfunktion
     * @return einen Randomwert
     */
    public static double randomGauss(int breite) {
        //TODO Iplmement Gaus random
        return Math.random();
    }

    /**
     * bildet die Summe von 1 bis z d.h. sie summiert die aktuelle Zahl i
     * (element 1,...,z) zur Summe hinzu bis sie bei z angekommen ist
     *
     * @param z
     * @return die Summe von 1 bis z
     */
    public static int summmeVon1Bis(int z) {
        int summe = 0;
        for (int i = 1; i <= z; i++) {
            summe += i;
        }
        return summe;
    }

    /**
     * liefert die a-te ungerade Zahl. für 1 => 1, für 2 => 3, für 3 => 5 usw.
     *
     * @param n eine positive zahl
     * @return die n-te ungerade Zahl
     */
    public static double ungeradeZahl(int n) {
        if (n <= 0) {
            return 0;
        }
        return (2 * n) - 1;
    }
}
