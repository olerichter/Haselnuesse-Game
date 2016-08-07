package controller.dungon;

/**
 * Diese Klasse Implementiert die BinBaumstruktur - ein Datenobjekt (
 *
 * @see DungonRoom) wird an den jeweiligen Knoten angehängt
 * @version 0.5a
 * @author Ole Richter
 *
 *
 *  *
 */
class DungonTree {

    DungonTree oben;
    DungonTree left;
    DungonTree right;
    DungonRoom room;

    /**
     * Initialisiert den Knoten
     *
     * @param room das Datenobjekt
     */
    DungonTree(DungonRoom room) {
        this.room = room;
    }

    /**
     * liefet den linken Knoten
     *
     * @return den linken Knoten
     */
    DungonTree getLeft() {
        return left;
    }

    /**
     * setzt den linken Knoten
     *
     * @param left linker Knoten
     */
    void setLeft(DungonTree left) {
        this.left = left;
        left.setOben(this);
    }

    /**
     * liefert den rechten Knoten
     *
     * @return
     */
    DungonTree getRight() {
        return right;
    }

    /**
     * setzt den rechten Knoten
     *
     * @param right rechter Knoten
     */
    void setRight(DungonTree right) {
        this.right = right;
        right.setOben(this);
    }

    /**
     * liefert das DatenObjekt
     *
     * @return der Raum
     */
    DungonRoom getRoom() {
        return room;
    }

    /**
     * liefert den nächst höheren Knoten
     *
     * @return vaterKnoten
     */
    DungonTree getOben() {
        return oben;
    }

    /**
     * setzt den obeen Knoten
     *
     * @param Vater Knoten
     */
    void setOben(DungonTree oben) {
        this.oben = oben;
    }

    /**
     * liefert die aktuelle Tiefe Rekursiv implementiert
     *
     * @return die Tiefe als int
     */
    int getTiefe() {
        if (oben == null) {
            return 1;
        } else {
            return (1 + oben.getTiefe());
        }
    }

    /**
     * Baut aus der Rückgabe des linken und rechten Teilbaumes ein array,dass
     * den aktuellen Raum als int[][] repräsentiert sehr Speicherineffizient
     * durch feldweieses kopieren und rekusion
     *
     * @return den Raum als int[breite][höhe] array
     */
    int[][] knotenToArray() {
        // prüft, ob er selber der unterste Knoten ist
        if (left == null) {
            // //DungonGenerator.printArray(room.roomToArray(), room);
            return room.roomToArray();

        }



        //Temporäre variablen werden inalisiert
        int[][] leftarray = left.knotenToArray();
        int[][] rightarray = right.knotenToArray();
        int[][] array = new int[room.getX()][room.getY()];
        //  //debug Ausgabe der Größe
        //  //System.out.println(">>NEW ROOM " + room.getX() + "x" + room.getY() + " >> " + (left.getRoom().getX() + right.getRoom().getX()) + "x" + (left.getRoom().getY() + right.getRoom().getY()));

        //feldweises Kopieren von vom linken und rechten Teilraum in den Gesamtraum
        //Vertikal
        if (left.getRoom().getX() == room.getX()) {
            for (int i = 0; i < room.getX(); i++) {
                //oberer Raum
                for (int j = 0; j < left.getRoom().getY(); j++) {
                    array[i][j] = leftarray[i][j];
                }
                //unterer Raum
                for (int jj = left.getRoom().getY(); jj < (right.getRoom().getY() + left.getRoom().getY()); jj++) {
                    array[i][jj] = rightarray[i][(jj - left.getRoom().getY())];
                }
            }
        }//Horizontal 
        else {
            //linker Raum
            for (int i = 0; i < left.getRoom().getX(); i++) {
                for (int j = 0; j < room.getY(); j++) {
                    array[i][j] = leftarray[i][j];
                }

            }
            //Rechter Raum
            for (int i = left.getRoom().getX(); i < (right.getRoom().getX() + left.getRoom().getX()); i++) {
                for (int j = 0; j < room.getY(); j++) {
                    array[i][j] = rightarray[(i - left.getRoom().getX())][j];
                }

            }
        }

        //debug ausgabe des Raumes -->  //DungonGenerator.printArray(array, room);
        return array;

    }
}
