package controller.toolkit;

import java.io.Serializable;

/**
 * Dies ist eine Hilfsklasse zur Speicherung von 2-int Koordinaten (x,y) in
 * einem Objekt - verhällt sich ähnlich wie java.awt.Point
 *
 * @version 0.5a
 * @author Ole Richter
 */
public class Koordinaten implements Serializable {

    private int x;
    private int y;

    /**
     * Baut ein Koordinatenobjekt
     *
     * @param x
     * @param y
     */
    public Koordinaten(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * lifert nur true wenn das Objekt eine Koordinate ist und die Koordinaten
     * übereinstimmen
     *
     * @param obj
     * @return true wenn das Objekt eine Koordinate ist und die Koordinaten
     * übereinstimmen, sonst false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Koordinaten other = (Koordinaten) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    /**
     * liefert den hashcode
     *
     * @return den hashcode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        return hash;
    }

    /**
     * liefert y
     *
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * setz y
     *
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the value of x
     *
     * @return the value of x
     */
    public int getX() {
        return x;
    }

    /**
     * Set the value of x
     *
     * @param x new value of x
     */
    public void setX(int x) {
        this.x = x;
    }
}
