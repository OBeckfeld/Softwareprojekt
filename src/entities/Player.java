package entities;

import Weapons.*;
import entities.components.MovementComponent;
import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import main.GamePanel;
import skilltree.SpeedBoost;
import skilltree.PoisonCloud;
import tools.TileManager;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import skilltree.DMGBoost;
import skilltree.Dash;
import skilltree.SkillTree;

import java.awt.event.KeyEvent;
import java.util.Objects;

public class Player extends PlayerTypeEntity {

    protected final KeyboardInputs inputs;
    private int skillpoints = 0;


    public Player(int x, int y, int width, int height, EntityRegistry registry, KeyboardInputs keyboardInputs, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 20, 60, registry, attackRegistry, tileManager, gamePanel);//attackDuration und cooldown machen nichts. Beides wird von Waffe bestimmt
        // ein Objekt wird gefordert, um Exceptions zu vermeiden
        this.inputs = Objects.requireNonNull(keyboardInputs, "keyboardInputs must not be null");
        movement = new MovementComponent(keyboardInputs, tileManager);
        mass = 3;
        defaultSpeed = 3;
        speed = defaultSpeed;
        weapon = new StarterSword(this, attackRegistry, tileManager);
        gamePanel.assignPlayer(this);
        attackRegistry.grabOwner(this);
    }

    public void update() {
        boolean skillTreeActive = skillTree != null && skillTree.isActive;
        if (skillTreeActive) {
            skillTree.update();
        }
        if (inputs != null && inputs.getHeldKeys().contains(KeyEvent.VK_P)) { //öffnet den SkillTree
            if (skillTree != null) {
                if(!skillTree.isActive) {
                    skillTree.open();
                    skillTree.update();
                }
                else {
                    skillTree.close();
                }
            }
            try {
                Thread.sleep(250); //damit mit einem Tastendruck der skillTree sich nicht öffnet und direkt wieder schließt
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if ((skillTree == null || !skillTree.isActive) && (gamePanel != null)) {
            if (!gamePanel.getDeathScreen()) {
                super.update();
                movement.move(this);
                if (inputs != null && inputs.getHeldKeys().contains(KeyEvent.VK_J)) {
                        weapon.use();
                }
                // 1. ability slot
                if (inputs != null && inputs.getHeldKeys().contains(KeyEvent.VK_1)) {
                    abilityManger.use(1);
                }
                // 2. ability slot
                if (inputs != null && inputs.getHeldKeys().contains(KeyEvent.VK_2)) {
                    abilityManger.use(2);
                }
                // 3. ability slot
                if (inputs != null && inputs.getHeldKeys().contains(KeyEvent.VK_3)) {
                    abilityManger.use(3);
                }
                // 4. ability slot
                if (inputs != null && inputs.getHeldKeys().contains(KeyEvent.VK_4)) {
                    abilityManger.use(4);
                }
                // 5. ability slot
                if (inputs != null && inputs.getHeldKeys().contains(KeyEvent.VK_5)) {
                    abilityManger.use(5);
                }
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

