package entities;

import entities.components.MovementComponent;
import entities.managers.AbilityManager;
import entities.managers.AttackManager;
import entities.managers.EntityRegistry;
import tools.Hitbox;
import tools.Vector;

import java.awt.*;
import java.util.ArrayList;

public abstract class Entity {
    public static final int NORTH = 3;//das muss noch geändert werden leute!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static final int EAST = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    protected AttackManager attackManager;
    protected int height, width;
    protected double x, y;
    protected double defaultSpeed = 5;
    protected MovementComponent movement;
    protected Hitbox hurtbox;
    public EntityRegistry registry;

    public Entity(double x, double y, int width, int height, EntityRegistry registry, AttackManager attackManager) {
        this.attackManager = attackManager;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent();
        hurtbox = new Hitbox(x, y, width, height);
        registry.register(this);
        this.registry = registry;

    }
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

    public void unregister(){
        registry.unregister(this);
    }
    public double [] getCenter(){//gibt das Center von Entities zurück
        double cX = x + width/2;
        double cY = y + height/2;
        double [] centerCoords = {cX,cY};

        return centerCoords;
    }

    public void move(Vector vector){
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
    public int [] getOffsetCoords(int dir){
        if (dir == NORTH){return new int[]{0, -1};}
        if (dir == EAST){return new int[]{1, 0};}
        if (dir == SOUTH){return new int[]{0, 1};}
        return new int[]{-1, 0};
    }
    public int getOppDirection(int dir){
        if(dir == NORTH){return SOUTH;}
        if(dir == SOUTH){return NORTH;}
        if(dir == EAST){return WEST;}
        return EAST;

    }
}
