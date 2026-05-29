package entities;

import Weapons.*;
import entities.components.MovementComponent;
import entities.managers.AttackRegistry;
import tools.TileManager;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import skilltree.DMGBoost;
import skilltree.Dash;
import skilltree.SkillTree;

import java.awt.event.KeyEvent;

public class Player extends PlayerTypeEntity {

    protected KeyboardInputs inputs;
    private int skillpoints = 0;

    public Player(int x, int y, int width, int height, EntityRegistry registry, KeyboardInputs keyboardInputs, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, 20, 60, registry, attackRegistry, tileManager);//attackDuration und cooldown machen nichts. Beides wird von Waffe bestimmt
        this.inputs = keyboardInputs;
        movement = new MovementComponent(keyboardInputs, tileManager);
        mass = 3;
        //nur zum TESTEN
        Dash dash = new Dash(this);
        abilityManger.unlock(skillTree.getAbilityReference("Dash"));
        abilityManger.equip(skillTree.getAbilityReference("Dash"), 1);
        //---------------------------------------------------------------
        DMGBoost dmgBoost = new DMGBoost(this);
        abilityManger.unlock(skillTree.getAbilityReference("DMGBoost"));
        abilityManger.equip(skillTree.getAbilityReference("DMGBoost"), 2);
        weapon = new MiniGun(this, attackRegistry, tileManager);
    }

    public void update() {
        if(inputs.getHeldKeys().contains(KeyEvent.VK_P)){
            if(!skillTree.isActive) {
                skillTree.open();
            }
            else {
                skillTree.close();
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(!skillTree.isActive) {
            super.update();
            movement.move(this);
            if (inputs.getHeldKeys().contains(KeyEvent.VK_J)) {
                weapon.use();
            }
            //1 ability slot
            if (inputs.getHeldKeys().contains(KeyEvent.VK_1)) {
                abilityManger.use(1);
            }
            //2 ability slot
            if (inputs.getHeldKeys().contains(KeyEvent.VK_2)) {
                abilityManger.use(2);
            }
            //3 ability slot
            if (inputs.getHeldKeys().contains(KeyEvent.VK_3)) {
                abilityManger.use(3);
            }
            //4 ability slot
            if (inputs.getHeldKeys().contains(KeyEvent.VK_4)) {
                abilityManger.use(4);
            }
            if (inputs.getHeldKeys().contains(KeyEvent.VK_5)) {
                abilityManger.use(5);
            }
        }
    }

    public int getSkillpoints() {
        return skillpoints;
    }

    public void setSkillpoints(int number) {
        skillpoints = number;
    }
}

