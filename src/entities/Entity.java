package entities;

import entities.components.MovementComponent;

import java.awt.*;
import java.util.Set;

public abstract class Entity {
    // Die Position und Größe des Spielers
    private int height, width;
    private double x, y;
    private double speed = 3; // Pixel, mit denen sich der Spieler pro Frame bewegt
    private MovementComponent movement;
    private Rectangle hurtbox;

    public Entity(double x, double y, int height, int width, entityRegistry registry) {
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

    public void update(Set<Integer> keyboardInputs) {
        movement.move(keyboardInputs, this);
    }

    public void act(){}//wird in jedem frame aufgerufen. die funktion wird in den unterklassen bestimmt

}
