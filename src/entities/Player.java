package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import tools.TileManager;

public class Player extends Entity {

    public Player(int x, int y, int height, int width, EntityRegistry registry, KeyboardInputs keyboardInputs, TileManager tile) {
        super(x, y, height, width, registry, tile);
        movement = new MovementComponent(keyboardInputs, tile);
    }

    public void update() {
        movement.move(this);
    }
}

