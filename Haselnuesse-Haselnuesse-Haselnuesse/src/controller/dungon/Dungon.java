package controller.dungon;

import controller.game.MapDataBeschaffer;
import controller.toolkit.Zubehoer;
import data.Beschaffenheit;

/**
 * der dungongenerator erzeugt einen Quadranten für die Map - dieser ist in
 * Version 0.5 20x20 Felder groß (see GameController.MapDataBeschaffer)
 *
 * Er teilt rekursiv Räume (see DungonRoom), die er über eine BinBaum-Struktur
 * zwischenspeichert (see DungonTree) die Räume werden dann als primitives
 * 2D-INT Array zusammengefasst und mit Hilfe des Toolkits (see
 * Zubehoer.Zubehoer) objektorientiert an die Datenbank weiter gegeben.
 *
 * @version 0.6
 * @author Ole Richter
 *
 *
 *
 *
 */
public class Dungon {

    /**
     * Die Methode startet die Generierung des Quadranten
     *
     * @param x gibt die Breite an
     * @param y gibt die Höhe an
     * @param db ist die Datenbank (oder Datenbankverbindung), in die der
     * Quadrant gespeichert werden soll
     * @param x_offset ist die absolute X-Koordinate, an den der Quadrant nach
     * der Generierung platziert werden soll
     * @param y_offset ist die absolute Y-Koordinate, an den der Quadrant nach
     * der Generierung platziert werden soll
     */
    public void genDungonRek(int x, int y, MapDataBeschaffer db, int x_offset, int y_offset) {
        // der oberste Knoten wird erzeugt
        Beschaffenheit start = Beschaffenheit.randomBeschaffenheit();
        DungonTree top = new DungonTree(new DungonRoom(x, y, start));
        //der Baum wird rekusiv aufgebeut
        gentree(top);
        //der Baum wird in ein array kolabiert
        int[][] array = top.knotenToArray();
        //das jedes Feld des arrays wird in ein Objekt verwandelt und in die Datenbank gespeichert
        Zubehoer.arrayToDB(array, db, x_offset, y_offset, x, y);


    }

    /**
     * dies ist eine Hilfsmetode die zur Fehlerkorrektur ein Dungeonroom mit
     * zugehörigem int[][] array auf die Konsole schreibt
     *
     * @param array
     * @param room
     */
    static void printArray(int[][] array, DungonRoom room) {

        for (int i = 0; i < room.getX(); i++) {
            for (int j = 0; j < room.getY(); j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    /**
     * dies ist die rekursive Methode, die den BinBaum aufbaut
     */
    private void gentree(DungonTree top) {
        //bei einer Tiefe von 7 wird abgeborchen
        if (top.getTiefe() < 7) {

            int x = top.getRoom().getX();
            int y = top.getRoom().getY();

            double rand = Math.random();
            // es wird zufällig horizontal (60%) oder vertikal geteilt(40%) 
            if (rand < 0.6f && y >= 4) {
                //es wird die Teilungsstelle bestimmt
                int dif = (int) ((y - 4) * Math.random());
                //es wird ein Knoten mit Raum für links und rechts erzeugt
                DungonTree left = new DungonTree(new DungonRoom(x, dif + 2, Beschaffenheit.randomBeschaffenheit()));
                DungonTree right = new DungonTree(new DungonRoom(x, y - (dif + 2), Beschaffenheit.randomBeschaffenheit()));
                //sie werden an den Knoten gehängt
                top.setLeft(left);
                top.setRight(right);
                //der rekusive Aufruf wird bei ihnen gestartet
                gentree(left);
                gentree(right);

            } else if (x >= 4) {
                //es wird die Teilungsstelle bestimmt
                int dif = (int) ((x - 4) * Math.random());
                //es wird ein Knoten mit Raum für links und rechts erzeugt
                DungonTree left = new DungonTree(new DungonRoom(dif + 2, y, Beschaffenheit.randomBeschaffenheit()));
                DungonTree right = new DungonTree(new DungonRoom(x - (dif + 2), y, Beschaffenheit.randomBeschaffenheit()));
                //sie werden an den Knoten gehängt
                top.setLeft(left);
                top.setRight(right);
                //der rekusive Aufruf wird bei ihnen gestartet
                gentree(left);
                gentree(right);

            }
        }
    }
}
