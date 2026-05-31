package tools;

import entities.Entity;

public class Vector {
    private double dX;
    private double dY;
    private double length;

    /**
     * Erstellt einen neuen Vektor zwischen zwei Punkten und berechnet
     * die X- und Y-Verschiebung sowie die Länge des Vektors.
     */
    public Vector(double fromX, double fromY, double toX, double toY){
        dX = toX - fromX;
        dY = toY - fromY;
        length = Math.sqrt(dX * dX + dY * dY);
    }

    //setzt die Länge vom Vektor auf die neue und berechnet die neuen Offset-Koordinaten
    /**
     * Setzt die Länge des Vektors auf den angegebenen Wert und passt
     * die Offset-Koordinaten entsprechend an. Bei einer Länge von 0
     * wird der Vektor auf den Nullvektor gesetzt.
     */
    public void setLength(double l){//damit man nicht durch 0 teilt
        if (length != 0 && l != 0){
            dX = dX / length *l;
            dY = dY / length *l;
            length = l;
        }
        else{
            dX = 0;
            dY = 0;
            length = 0;
        }

    }

    /**
     * Addiert einen anderen Vektor zu diesem Vektor und berechnet
     * anschließend die neue Länge des kombinierten Vektors.
     */
    public void combineVector(Vector vector){
        dX = dX + vector.getOffsetX();
        dY = dY + vector.getOffsetY();
        length = Math.sqrt(dX * dX + dY * dY);
    }

    /**
     * Gibt die X-Verschiebung des Vektors zurück.
     */
    public double getOffsetX(){
        return dX;
    }

    /**
     * Gibt die Y-Verschiebung des Vektors zurück.
     */
    public double getOffsetY(){
        return dY;
    }

    /**
     * Gibt die aktuelle Länge des Vektors zurück.
     */
    public double getLength() {
        return length;
    }
}
