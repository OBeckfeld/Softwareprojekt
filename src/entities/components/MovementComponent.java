package entities.components;

import entities.Entity;

import java.awt.event.KeyEvent;
import java.util.Set;

public class MovementComponent {

    public MovementComponent(){}

//player muss noch zu entity geändert werden
    public void move(Set<Integer> keyboardInputs, Entity entity) {
        double dx = 0, dy = 0; //Veränderung der X und Y Koordinaten genau
        int vx, vy ; //Veränderung der X und Y Koordinaten auf int gerundet

        //prüfen, welche Inputs gegeben werden
        if(keyboardInputs.contains(KeyEvent.VK_W)){ dy -= entity.getSpeed(); }
        if(keyboardInputs.contains(KeyEvent.VK_S)){ dy += entity.getSpeed(); }
        if(keyboardInputs.contains(KeyEvent.VK_A)){ dx -= entity.getSpeed(); }
        if(keyboardInputs.contains(KeyEvent.VK_D)){ dx += entity.getSpeed(); }

        //für diagonale Bewegung
        if(dx != 0 && dy != 0){
            dx = dx * 0.70710678118;
            dy = dy * 0.70710678118;
        }

        entity.getHurtbox().setLocation((int) (entity.getX() + dx), (int) (entity.getY() + dy));

        entity.setX(entity.getX() + dx);
        entity.setY(entity.getY() + dy);
    }

}
