package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;

import java.util.Set;

public class Player extends Entity {

    public Player(int x, int y, int height, int width, EntityRegistry registry, KeyboardInputs keyboardInputs) {
        super(x, y, height, width, registry);
        movement = new MovementComponent(keyboardInputs);
    }

    public void update() {
        movement.move(this);
    }
}

