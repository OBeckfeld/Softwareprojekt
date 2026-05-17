package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import entities.managers.AttackManager;

import java.awt.event.KeyEvent;

public class Player extends PlayerTypeEntity {

    protected KeyboardInputs inputs;

    public Player(int x, int y, int width, int height, EntityRegistry registry, KeyboardInputs keyboardInputs, AttackManager attackManager) {
        super(x, y, width, height, 180, registry, attackManager);
        this.inputs = keyboardInputs;
        movement = new MovementComponent(keyboardInputs);
        mass = 3;
    }

    public void update() {
        super.update();
        movement.move(this);
        if(inputs.getHeldKeys().contains(KeyEvent.VK_J)) {
            attackManager.newAttack(registry, this);
            attackManager.attack(attack);
        };
    }
}

