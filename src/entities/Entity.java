package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import tools.Hitbox;
import tools.Vector;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity {
    // Die Position und Größe des Spielers
    protected int height, width;
    protected double x, y;
    protected double speed = 5; // Pixel, mit denen sich der Spieler pro Frame bewegt
    protected MovementComponent movement;
    protected Hitbox hurtbox;
    protected EntityRegistry registry;
    protected int viewRange;
    protected int mass;
    public Entity(double x, double y, int height, int width, EntityRegistry registry) {
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

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }//noch keinen Nutzen, aber in der Zukunft

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void update(){
        ArrayList<Entity> inView = getInView();
        for (Entity entity : inView) {
            if (registry.collidesWith(this, entity)) {
                Vector vector = new Vector(entity.getCenter()[0], entity.getCenter()[1], getCenter()[0], getCenter()[1]);
                vector.setLength(2);
                applyVector(vector);
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
}
