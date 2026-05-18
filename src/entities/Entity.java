package entities;

import entities.components.MovementComponent;
import entities.managers.AbilityManager;
import entities.managers.EntityRegistry;
import tools.Hitbox;
import tools.Vector;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity {
    public static final int NORTH = 3;
    public static final int EAST = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;

    protected int height, width;
    protected double x, y;

    protected MovementComponent movement;
    protected Hitbox hurtbox;
    protected EntityRegistry registry;

    public Entity(double x, double y, int width, int height, EntityRegistry registry) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent();
        hurtbox = new Hitbox(x, y, width, height);
        registry.register(this);
        this.registry = registry;

    }

    public Hitbox getHurtbox() {return hurtbox;}

    public double getX() {
        return x;
    }
    public void setX(double x) {this.x = x;}

    public double getY() {
        return y;
    }
    public void setY(double y) { this.y = y; }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }




    public void update(){

    }//wird in jedem frame aufgerufen. die funktion wird in den unterklassen bestimmt

    public void unregister(EntityRegistry registry){
        registry.unregister(this);
    }
    public double [] getCenter(){//gibt das Center von Entities zurück
        double cX = x + width/2;
        double cY = y + height/2;
        double [] centerCoords = {cX,cY};

        return centerCoords;
    }


    protected void applyVector(Vector vector){movement.applyVector(this, vector);}
    
    protected void move(Vector vector){
        movement.applyVector(this, vector);
    }
    protected int getDirectionTo(double targetX, double targetY){
        double xDis = Math.abs(x - targetX);
        double yDis = Math.abs(y - targetY);
        int xDir;
        int yDir;

        if (x - targetX > 0){
            xDir = WEST;
        }
        else{
            xDir = EAST;
        }
        if (y - targetY > 0){
            yDir = NORTH;
        }
        else{
            yDir = SOUTH;
        }
        if (xDis > yDis){
            return xDir;
        }
        return yDir;
    }
}
