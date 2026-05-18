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

    private void attack(){
        if(inputs.getHeldKeys().contains(KeyEvent.VK_J)){
            attackManager.newAttack(this);
            attackManager.attack(attack);
            isAttacking = true;
        }
    }

    public void update() {
        super.update();
        if(isAttacking){
            speed = 0;
        }
        else{
            speed = defaultSpeed;
        }
        movement.move(this);
        attack();
        }
    }


