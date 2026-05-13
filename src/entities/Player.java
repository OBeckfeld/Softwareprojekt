package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;

public class Player extends Entity {

    public Player(int x, int y, int height, int width, EntityRegistry registry, KeyboardInputs keyboardInputs) {
        super(x, y, height, width, registry);
        mass = 3;
        movement = new MovementComponent(keyboardInputs);
    }

    public void update() {
        super.update();
        movement.move(this);
    }
}

