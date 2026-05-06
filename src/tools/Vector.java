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
    public void setLength(double l){
        if (length == 0) {
            dX = 0;
            dY = 0;
            return;
        }
        dX = dX / length *l;
        dY = dY / length *l;
        length = l;
    }
    public void apply(Entity entity){
        entity.setX(entity.getX() + dX);
        entity.setY(entity.getY() + dY);
        entity.getHurtbox().setLocation(entity.getX(), entity.getY());
    }
    public double getLength(){
        return length;
    }
    public double getOffsetX(){
        return dX;
    }
    public double getOffsetY(){
        return dY;
    }
}
