package tools;

import entities.Entity;

public class Vector {
    double dX;
    double dY;
    double length;
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
    public double getOffsetX(){
        return dX;
    }
    public double getOffsetY(){
        return dY;
    }

}
