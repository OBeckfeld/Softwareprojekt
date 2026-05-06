package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;

public class Player extends PlayerTypeEntity {

    public Player(int x, int y, int height, int width, EntityRegistry registry, KeyboardInputs keyboardInputs) {
        super(x, y, height, width, registry);
        movement = new MovementComponent(keyboardInputs);
    }

    public void update() {
        movement.move(this);
    }
}

