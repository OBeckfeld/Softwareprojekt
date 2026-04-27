package entities;

import entities.components.MovementComponent;

import java.awt.*;
import java.util.Set;

public abstract class Entity {
    // Die Position und Größe des Spielers
    private int x, y, height, width;
    private double speed = 2; // Pixel, mit denen sich der Spieler pro Frame bewegt
    private MovementComponent movement;
    private Rectangle hurtbox;

    public Entity(int x, int y, int height, int width, entityRegistry registry) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        movement = new MovementComponent();
        hurtbox = new Rectangle(x, y, width, height);
        registry.register(this);
    }

    public Rectangle getHurtbox() {return hurtbox;}

    public int getX() {
        return x;
    }
    public void setX(int x) {this.x = x;}

    public int getY() {
        return y;
    }
    public void setY(int y) { this.y = y; }

    public double getSpeed() { return speed; }
    public void setSpeed(double speed) { this.speed = speed; }//noch keinen Nutzen, aber in der Zukunft

    public void update(Set<Integer> keyboardInputs) {
        movement.move(keyboardInputs, this);
    }


}
