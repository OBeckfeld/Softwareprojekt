package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import tools.Hitbox;

public abstract class Entity {
    protected int height, width;
    protected double x, y;
    protected double speed = 5;
    protected MovementComponent movement;
    protected Hitbox hurtbox;
    protected EntityRegistry registry;
    protected int viewRange;
    public Entity(double x, double y, int height, int width, EntityRegistry registry) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent();
        hurtbox = new Hitbox(x, y, width, height);
        registry.register(this);
        this.registry = registry;
        viewRange = 100;
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

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void update(){}

    public void unregister(EntityRegistry registry){
        registry.unregister(this);
    }
    public double [] getCenter(){//gibt das Center von Entities zurück
        double cX = x + width/2;
        double cY = y + height/2;
        double [] centerCoords = {cX,cY};

        return centerCoords;
    }
}
