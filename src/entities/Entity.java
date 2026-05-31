package entities;

import entities.components.MovementComponent;
import entities.managers.AbilityManager;
import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import main.GamePanel;
import skilltree.SkillTree;
import tools.Hitbox;
import tools.Vector;
import tools.TileManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public abstract class Entity {
    public static final int NORTH = 3;//das muss noch geändert werden leute!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public static final int EAST = 0;
    public static final int SOUTH = 1;
    public static final int WEST = 2;
    protected AttackRegistry attackRegistry;
    protected int height, width;
    protected double x, y;
    protected MovementComponent movement;
    protected Hitbox hurtbox;
    public EntityRegistry registry;
    protected TileManager tileManager;
    protected boolean dead;
    protected int z = 1;
    public Entity(double x, double y, int width, int height, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        this.attackRegistry = attackRegistry;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent(tileManager);
        hurtbox = new Hitbox(x, y, width, height);
        this.registry = registry;
        this.tileManager = tileManager;
        dead = false;

    }
    public Entity(double x, double y, int width, int height, EntityRegistry registry, TileManager tileManager) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent(tileManager);
        hurtbox = new Hitbox(x, y, width, height);

    }
    public Entity(double x, double y, int width, int height, EntityRegistry registry, TileManager tileManager, int hitBoxWidth, int hitBoxHeight) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent(tileManager);
        double hitboxX = x + (width - hitBoxWidth) / 2.0;
        double hitboxY = y + (height - hitBoxHeight);
        hurtbox = new Hitbox(hitboxX, hitboxY, hitBoxWidth, hitBoxHeight);
        registry.register(this);
        this.registry = registry;
    }


    public Hitbox getHurtbox() {return hurtbox;}
    public int getZ(){return z;}
    public void setZ(int z){this.z = z;}
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

    public EntityRegistry getRegistry() {
        return registry;
    }

    public void update(){

    }//wird in jedem frame aufgerufen. die funktion wird in den unterklassen bestimmt

    protected void unregister(){
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

    public int getPointsOnDeath(){
        return 1;
    }

    public boolean isDead() {
        return dead;
    }
}
