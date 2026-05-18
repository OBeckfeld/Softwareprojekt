package entities;

import entities.components.MovementComponent;
import tools.TileManager;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Set;

public class Player extends Entity {

    public Player(int x, int y, int height, int width, entityRegistry registry, TileManager tileManager) {
        super(x, y, height, width, registry, tileManager);
    }

}

