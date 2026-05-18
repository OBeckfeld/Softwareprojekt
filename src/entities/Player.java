package entities;

import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import entities.managers.AttackManager;
import skilltree.Dash;

import java.awt.event.KeyEvent;

public class Player extends PlayerTypeEntity {

    protected KeyboardInputs inputs;

    public Player(int x, int y, int width, int height, EntityRegistry registry, KeyboardInputs keyboardInputs, AttackManager attackManager) {
        super(x, y, width, height, 180, registry, attackManager);
        this.inputs = keyboardInputs;
        movement = new MovementComponent(keyboardInputs);
        mass = 3;
        //nur zum TESTENNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
        Dash dash = new Dash(this);
        abilityManger.unlock(dash);
        abilityManger.equip(dash, 1);
        //---------------------------------------------------------------
    }

    public void update() {
        super.update();
        movement.move(this);
        if(inputs.getHeldKeys().contains(KeyEvent.VK_J)) {
            attackManager.newAttack(registry, this);
            attackManager.attack(attack);
        }
        //1 ability slot
        if(inputs.getHeldKeys().contains(KeyEvent.VK_1)) {
            abilityManger.use(1);
        }
        //2 ability slot
        if(inputs.getHeldKeys().contains(KeyEvent.VK_2)) {
            abilityManger.use(2);
        }
        //3 ability slot
        if(inputs.getHeldKeys().contains(KeyEvent.VK_3)) {
            abilityManger.use(3);
        }
        //4 ability slot
        if(inputs.getHeldKeys().contains(KeyEvent.VK_4)) {
            abilityManger.use(4);
        }
    }
}

