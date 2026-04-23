package entities.components;

import entities.Entity;

import java.awt.event.KeyEvent;
import java.util.Set;

public class MovementComponent {

    public MovementComponent(){}
    public void move(Set<Integer> keyboardInputs, Entity player) {
        double dx = 0, dy = 0; //Veränderung der X und Y Koordinaten genau
        int vx, vy ; //Veränderung der X und Y Koordinaten auf int gerundet

        //prüfen, welche Inputs gegeben werden
        if(keyboardInputs.contains(KeyEvent.VK_W)){ dy -= player.getSpeed(); }
        if(keyboardInputs.contains(KeyEvent.VK_S)){ dy += player.getSpeed(); }
        if(keyboardInputs.contains(KeyEvent.VK_A)){ dx -= player.getSpeed(); }
        if(keyboardInputs.contains(KeyEvent.VK_D)){ dx += player.getSpeed(); }

        //für diagonale Bewegung
        if(dx != 0 && dy != 0){
            vx = (int) Math.round(dx / 1.4142135623730951);
            vy = (int) Math.round(dy / 1.4142135623730951);
        }
        else{
            vx = (int )Math.round(dx);
            vy = (int )Math.round(dy);
        }

        player.setX(player.getX() + vx);
        player.setY(player.getY() + vy);
    }
}
