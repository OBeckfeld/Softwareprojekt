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

    public Entity(double x, double y, int height, int width, EntityRegistry registry) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent();
        hurtbox = new Hitbox(x, y, width, height);
        registry.register(this);
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

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public void update(){}

    public void unregister(EntityRegistry registry){
        registry.unregister(this);
    }
}
