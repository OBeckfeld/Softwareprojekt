package entities;

import entities.Entity;
import entities.components.MovementComponent;
import entities.entityRegistry;
import tools.TileManager;
import tools.Vector;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Set;

public class Enemy extends Entity {
    Player player;//nur zum testen
    public Enemy(int x, int y, int height, int width, entityRegistry registry, Player player, TileManager tileManager) {
        super(x, y, height, width, registry, tileManager);
        setSpeed(2);
        this.player = player;//nur zum testen
    }
    public void act(){
        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());
        vector.setLength(getSpeed());
        vector.apply(this);//get getSpeed() schritte in richtung vom player
    }

}
