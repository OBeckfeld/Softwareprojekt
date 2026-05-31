package tools;

import entities.Entity;

public class Vector {
    private double dX;
    private double dY;
    private double length;
    public Vector(double fromX, double fromY, double toX, double toY){
        dX = toX - fromX;
        dY = toY - fromY;
        length = Math.sqrt(dX * dX + dY * dY);
    }
    //setzt die Länge vom Vektor auf die neue und berechnet die neuen Offset-Koordinaten
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
    public void combineVector(Vector vector){
        dX = dX + vector.getOffsetX();
        dY = dY + vector.getOffsetY();
        length = Math.sqrt(dX * dX + dY * dY);
    }
    public double getOffsetX(){
        return dX;
    }
    public double getOffsetY(){
        return dY;
    }

    public double getLength() {
        return length;
    }
}
