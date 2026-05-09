package tools;

import entities.Entity;

import java.lang.Math;

//Hilfsklasse für die Vektorrechnung
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
    public void setLength(double l){
        dX = dX / length *l;
        dY = dY / length *l;
        length = l;
    }
    //Verechnet die Offset-Koordinaten mit denen einer Entity (könnte die Berechtigungen vom Vector überschreiten, müssen wir dann entscheiden)
    public void apply(Entity entity){
        entity.setX(entity.getX() + dX);
        entity.setY(entity.getY() + dY);
        entity.getHurtbox().setLocation((int) (entity.getX() + dX), (int) (entity.getY() + dY));
    }
    public double getOffsetX(){
        return dX;
    }
    public double getOffsetY(){
        return dY;
    }
    public double getLength() { return length; }
}
