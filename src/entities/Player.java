package entities;

import Weapons.*;
import entities.components.MovementComponent;
import entities.managers.AttackRegistry;
import main.GamePanel;
import skilltree.SkillTree;
import tools.Animation;
import tools.SpriteSheet;
import tools.TileManager;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player extends PlayerTypeEntity {

    protected KeyboardInputs inputs;
    private int skillpoints = 0;
    private Animation[] walkAnimations;
    private Animation attackAnimation;
    private boolean isAnimatingAttack = false;


    private SpriteSheet sheet;
    private Animation currentAnimation;
    private int lastDirection = -1;


    public Player(int x, int y, int width, int height, EntityRegistry registry,
                  KeyboardInputs keyboardInputs, AttackRegistry attackRegistry,
                  TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 20, 60, registry, attackRegistry, tileManager);
        this.inputs = keyboardInputs;
        movement = new MovementComponent(keyboardInputs, tileManager);
        mass = 3;
        currentHealth = 100;
        skillTree = new SkillTree(this, gamePanel);
        weapon = new Rifle(this, attackRegistry, tileManager);

        sheet = new SpriteSheet("src/data/sprites/playerCrawler.png", 833, 833);

        walkAnimations = new Animation[4];
        walkAnimations[0] = new Animation(new BufferedImage[]{ // rechts
                sheet.getFrame(1, 0), sheet.getFrame(1, 1), sheet.getFrame(1, 2)
        }, 8, true);
        walkAnimations[2] = new Animation(new BufferedImage[]{ // links
                sheet.getFrame(1, 3), sheet.getFrame(1, 4), sheet.getFrame(1, 5)
        }, 8, true);
        walkAnimations[1] = new Animation(new BufferedImage[]{ // unten
                sheet.getFrame(1, 6), sheet.getFrame(1, 7), sheet.getFrame(1, 8)
        }, 8, true);
        walkAnimations[3] = new Animation(new BufferedImage[]{ // oben
                sheet.getFrame(1, 9), sheet.getFrame(1, 10), sheet.getFrame(1, 11)
        }, 8, true);

        currentAnimation = walkAnimations[0];
    }

    public void update() {
        if(skillTree.isActive){
            skillTree.update();
        }
        if(inputs.getHeldKeys().contains(KeyEvent.VK_P)){ //öffnet den SkillTree
            if(!skillTree.isActive) {
                skillTree.open();
                skillTree.update();
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
        if(!skillTree.isActive){
            super.update();
            if (currentHealth <= 0) {
                registry.unregister(this);
                return;
            }
            movement.move(this);

        }
        if (isAnimatingAttack) {
            attackAnimation.update();
            if (attackAnimation.isFinished()) {
                isAnimatingAttack = false;
            }
        } else {
            if (direction != lastDirection) {
                currentAnimation = walkAnimations[direction];
                lastDirection = direction;
            }
            currentAnimation.update();
        }

        if (inputs.getHeldKeys().contains(KeyEvent.VK_J)) {
            if (!isAnimatingAttack) {
                isAnimatingAttack = true;
                switch (direction) {
                    case 0: // rechts
                        attackAnimation = new Animation(new BufferedImage[]{
                                sheet.getFrame(0, 0), sheet.getFrame(0, 1), sheet.getFrame(0, 2)
                        }, 8, false);
                        break;
                    case 2: // links
                        attackAnimation = new Animation(new BufferedImage[]{
                                sheet.getFrame(0, 3), sheet.getFrame(0, 4), sheet.getFrame(0, 5)
                        }, 8, false);
                        break;
                    case 1: // unten
                        attackAnimation = new Animation(new BufferedImage[]{
                                sheet.getFrame(0, 6), sheet.getFrame(0, 7), sheet.getFrame(0, 8)
                        }, 8, false);
                        break;
                    case 3: // oben
                        attackAnimation = new Animation(new BufferedImage[]{
                                sheet.getFrame(0, 9), sheet.getFrame(0, 10), sheet.getFrame(0, 11)
                        }, 8, false);
                        break;
                }
                weapon.use();
            }
        }

        if (inputs.getHeldKeys().contains(KeyEvent.VK_1)) { abilityManger.use(1); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_2)) { abilityManger.use(2); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_3)) { abilityManger.use(3); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_4)) { abilityManger.use(4); }
    }


    public BufferedImage getSprite() {
        if (isAnimatingAttack) return attackAnimation.getCurrentFrame();
        if (currentAnimation == null) return null;
        return currentAnimation.getCurrentFrame();
    }

    public int getSkillpoints() { return skillpoints; }
    public void setSkillpoints(int number) { skillpoints = number; }
}