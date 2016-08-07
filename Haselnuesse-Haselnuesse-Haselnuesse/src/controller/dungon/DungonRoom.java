package controller.dungon;

import data.Beschaffenheit;

/**
 * Dies ist das Datenobjekt, dass an jedem Knoten hängt (see DungonTree) es
 * enthält seine Größe und seine Beschaffenheit (see Data.Beschaffenheit)
 *
 * @version 0.5a
 * @author Ole Richter
 *
 *
 *
 */
class DungonRoom {

    private int x;
    private int y;
    private Beschaffenheit beschaffenheit;

    /**
     * Initialisiert das Datenobjekt
     *
     * @param x ist die Breite
     * @param y ist die Höhe
     * @param beschaffenheit ist die Bodenbeschaffenheit
     */
    DungonRoom(int x, int y, Beschaffenheit beschaffenheit) {
        this.x = x;
        this.y = y;
        this.beschaffenheit = beschaffenheit;
    }

    /**
     * Gibt die Breite zurück
     *
     * @return the value of x
     */
    int getX() {
        return x;
    }

    /**
     * Gibt die Höhe zurück
     *
     * @return the value of y
     */
    int getY() {
        return y;
    }

    /**
     * liefert den Raum als int[x][y]- Array zurück dieser enthält einen Rand
     * von einem Feld ab v0.7 wird auch der Innenraum zufälliger innerhalb der
     * Beschaffenheit gestaltet sein (
     *
     * @see Data.Beschaffenheit)
     * @return raum als int[x][y]
     */
    int[][] roomToArray() {
        int[][] raumarray = new int[x][y];

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                //der Rand
                if (i == 0 || i == (x - 1) || j == 0 || j == (y - 1)) {
                    raumarray[i][j] = beschaffenheit.getIntRandRandom();
                }//der Rest
                else {
                    raumarray[i][j] = beschaffenheit.getIntBeschaffenheitRandom();
                }

            }
        }

        return raumarray;
    }
}
