package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import tools.Hitbox;
import tools.Vector;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity {
    protected static final int NORTH = 3;
    protected static final int EAST = 0;
    protected static final int SOUTH = 1;
    protected static final int WEST = 2;

    protected int height, width;
    protected double x, y;
    protected double speed = 5;
    protected MovementComponent movement;
    protected Hitbox hurtbox;
    protected EntityRegistry registry;
    protected int viewRange;
    protected int mass;
    public Entity(double x, double y, int width, int height, EntityRegistry registry) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent();
        hurtbox = new Hitbox(x, y, width, height);
        registry.register(this);
        this.registry = registry;
        viewRange = 300;
        mass = 2;
    }

    public Hitbox getHurtbox() {return hurtbox;}
    public int getViewRange(){return viewRange;}
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

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }


    public void update(){
        ArrayList<Entity> inView = getInView();
        for (Entity entity : inView) {
            if (entity instanceof Attack || entity instanceof ViewBox){continue;}
            if (registry.collidesWith(this, entity)) {
                Vector vector = new Vector(entity.getCenter()[0], entity.getCenter()[1], getCenter()[0], getCenter()[1]);
                vector.setLength(2/mass);
                move(vector);
            }
        }
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
    protected ArrayList<Entity> getInView(){ return registry.getInRange(this, getViewRange()); }
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
            return yDir;
        }
        return xDir;
    }
}
