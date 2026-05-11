package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import entities.managers.AttackManager;

import java.awt.event.KeyEvent;

public class Player extends PlayerTypeEntity {

    protected KeyboardInputs inputs;

    public Player(int x, int y, int height, int width, EntityRegistry registry, KeyboardInputs keyboardInputs, AttackManager attackManager) {
        super(x, y, height, width, 180, registry, attackManager);
        movement = new MovementComponent(keyboardInputs);
    }

    public void update() {
        movement.move(this);
        if(inputs.getHeldKeys().contains(KeyEvent.VK_J)) {
            attackManager.newAttack(registry, this);
        };
    }
}

