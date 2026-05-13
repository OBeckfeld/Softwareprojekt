package entities.components;

import entities.Entity;
import inputs.KeyboardInputs;
import tools.Vector;

import java.awt.event.KeyEvent;

public class MovementComponent {

    KeyboardInputs inputs;

    public MovementComponent(){}
    public MovementComponent(KeyboardInputs inputs){this.inputs = inputs;}

    public void move(Entity entity) {
        if (inputs == null) {
            return;
        }

        double dx = 0, dy = 0;

        if(inputs.getHeldKeys().contains(KeyEvent.VK_W)){ dy -= entity.getSpeed(); }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_S)){ dy += entity.getSpeed(); }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_A)){ dx -= entity.getSpeed(); }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_D)){ dx += entity.getSpeed(); }

        if(dx != 0 && dy != 0){
            dx = dx * 0.70710678118;
            dy = dy * 0.70710678118;
        }

        entity.getHurtbox().setLocation(entity.getX() + dx,entity.getY() + dy);

        entity.setX(entity.getX() + dx);
        entity.setY(entity.getY() + dy);
    }

    public void move(Entity entity, double dx, double dy) {

        entity.getHurtbox().setLocation(entity.getX() + dx,entity.getY() + dy);

        entity.setX(entity.getX() + dx);
        entity.setY(entity.getY() + dy);

    }
    public void applyVector(Entity entity, Vector vector) {
        move(entity, vector.getOffsetX(), vector.getOffsetY());
    }
}
