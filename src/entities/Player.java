package entities;

import Weapons.Gun;
import Weapons.StarterSword;
import Weapons.Weapon;
import entities.components.MovementComponent;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import entities.managers.AttackManager;
import skilltree.DMGBoost;
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
        DMGBoost dmgBoost = new DMGBoost(this);
        abilityManger.unlock(dmgBoost);
        abilityManger.equip(dmgBoost, 2);
        weapon = new Gun(this,attackManager);
    }

    public void update() {
        super.update();
        movement.move(this);
        if(inputs.getHeldKeys().contains(KeyEvent.VK_J)) {
            //attackManager.newAttack(this);
            //attackManager.attack(attack);
            weapon.use();
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
        //4 ability slot
        if(inputs.getHeldKeys().contains(KeyEvent.VK_Q)) {
            new FollowingProjectile(getCenter() [0], getCenter() [1], 10, 10, registry, attackManager, this, 4, direction, 300);
        }
    }
}

