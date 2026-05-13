package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;

public class Player extends PlayerTypeEntity {

    public Player(int x, int y, int height, int width, EntityRegistry registry, KeyboardInputs keyboardInputs) {
        super(x, y, height, width, 180,registry);
        movement = new MovementComponent(keyboardInputs);
        this.verticalRange = 120; // größe der Hitbox von Attack bei einem Enemy
        this.horizontalRange = 60;
    }

    public void update() {
        movement.move(this);
    }
}

