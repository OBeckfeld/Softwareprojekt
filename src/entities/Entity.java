package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;

import java.awt.*;

public abstract class Entity {
    // Die Position und Größe des Spielers
    protected int height, width;
    protected double x, y;
    protected double speed = 5; // Pixel, mit denen sich der Spieler pro Frame bewegt
    protected MovementComponent movement;
    protected Rectangle hurtbox;

    public Entity(double x, double y, int height, int width, EntityRegistry registry) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent();
        hurtbox = new Rectangle((int)Math.round(x), (int) Math.round(y), width, height);
        registry.register(this);
    }

    public Rectangle getHurtbox() {return hurtbox;}

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

    public void update(){}//wird in jedem frame aufgerufen. die funktion wird in den unterklassen bestimmt

    public String getName(){return "";}//zum testen
}
