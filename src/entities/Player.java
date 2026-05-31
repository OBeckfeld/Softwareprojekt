package entities;

import Weapons.*;
import entities.components.MovementComponent;
import entities.managers.AttackManager;
import entities.managers.AttackRegistry;
import main.GamePanel;
import skilltree.SpeedBoost;
import skilltree.PoisonCloud;
import skilltree.SkillTree;
import tools.Animation;
import tools.SpriteSheet;
import tools.TileManager;
import entities.managers.EntityRegistry;
import inputs.KeyboardInputs;
import skilltree.DMGBoost;
import skilltree.Dash;
import skilltree.SkillTree;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.awt.image.BufferedImage;

public class Player extends PlayerTypeEntity {

    protected final KeyboardInputs inputs;
    private int skillpoints = 0;
    private Animation[] walkAnimations;
    private Animation[] attackAnimation;
    private boolean isAnimatingAttack = false;

    private SpriteSheet sheet;
    private Animation currentAnimation;
    private int lastDirection = -1;

    /**
     * Erstellt einen neuen Spieler und initialisiert Eingaben, Bewegung,
     * Werte, SkillTree, Standardwaffe, SpriteSheet und Animationen.
     */
    public Player(int x, int y, int width, int height, EntityRegistry registry, KeyboardInputs keyboardInputs, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 20, 60, registry, attackRegistry, tileManager, gamePanel);
        this.inputs = Objects.requireNonNull(keyboardInputs, "keyboardInputs must not be null");
        movement = new MovementComponent(keyboardInputs, tileManager);
        mass = 3;
        currentHealth = 100;
        skillTree = new SkillTree(this, gamePanel);
        weapon = new Rifle(this, attackRegistry, tileManager);

        sheet = new SpriteSheet("src/data/sprites/playerCrawler.png", 1024, 1024);
        loadWeaponAnimations();
    }

    /**
     * Lädt abhängig von der aktuell ausgerüsteten Waffe die passenden
     * Lauf- und Angriffsanimationen aus dem SpriteSheet.
     */
    private void loadWeaponAnimations() {
        walkAnimations = new Animation[4];
        attackAnimation = new Animation[4];

        int walkRow, attackRow;

        if (weapon instanceof StarterSword) {
            walkRow = 1; attackRow = 0;
        } else if (weapon instanceof IronSword) {
            walkRow = 3; attackRow = 2;
        } else if (weapon instanceof Gun) {
            walkRow = 5; attackRow = 4;
        } else if (weapon instanceof Rifle) {
            walkRow = 7; attackRow = 6;
        } else if (weapon instanceof MiniGun) {
            walkRow = 9; attackRow = 8;
        } else if (weapon instanceof ShotGun) {
            walkRow = 11; attackRow = 10;
        } else {
            walkRow = 1; attackRow = 0;
        }

        walkAnimations[0] = new Animation(new BufferedImage[]{
                sheet.getFrame(walkRow, 0), sheet.getFrame(walkRow, 1), sheet.getFrame(walkRow, 2)
        }, 8, true);
        walkAnimations[2] = new Animation(new BufferedImage[]{
                sheet.getFrame(walkRow, 3), sheet.getFrame(walkRow, 4), sheet.getFrame(walkRow, 5)
        }, 8, true);
        walkAnimations[1] = new Animation(new BufferedImage[]{
                sheet.getFrame(walkRow, 6), sheet.getFrame(walkRow, 7), sheet.getFrame(walkRow, 8)
        }, 8, true);
        walkAnimations[3] = new Animation(new BufferedImage[]{
                sheet.getFrame(walkRow, 9), sheet.getFrame(walkRow, 10), sheet.getFrame(walkRow, 11)
        }, 8, true);

        attackAnimation[0] = new Animation(new BufferedImage[]{
                sheet.getFrame(attackRow, 0), sheet.getFrame(attackRow, 1), sheet.getFrame(attackRow, 2)
        }, 8, false);
        attackAnimation[2] = new Animation(new BufferedImage[]{
                sheet.getFrame(attackRow, 3), sheet.getFrame(attackRow, 4), sheet.getFrame(attackRow, 5)
        }, 8, false);
        attackAnimation[1] = new Animation(new BufferedImage[]{
                sheet.getFrame(attackRow, 6), sheet.getFrame(attackRow, 7), sheet.getFrame(attackRow, 8)
        }, 8, false);
        attackAnimation[3] = new Animation(new BufferedImage[]{
                sheet.getFrame(attackRow, 9), sheet.getFrame(attackRow, 10), sheet.getFrame(attackRow, 11)
        }, 8, false);

        currentAnimation = walkAnimations[0];
    }

    /**
     * Setzt eine neue Waffe für den Spieler und lädt anschließend
     * die dazugehörigen Animationen neu.
     */
    public void setWeapon(Weapon newWeapon) {
        this.weapon = newWeapon;
        loadWeaponAnimations();
    }

    /**
     * Aktualisiert den Spieler pro Tick.
     * Dabei werden SkillTree-Eingaben, Bewegung, Tod, Animationen,
     * Angriffe und das Benutzen von Fähigkeiten verarbeitet.
     */
    public void update() {
        boolean skillTreeActive = skillTree != null && skillTree.isActive;
        if (skillTreeActive) {
            skillTree.update();
        }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_P)) {
            if (skillTree != null) {
                if (!skillTree.isActive) {
                    skillTree.open();
                    skillTree.update();
                } else {
                    skillTree.close();
                }
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if (skillTree == null || !skillTree.isActive) {
            if (!gamePanel.getDeathScreen()) {
                super.update();
                if (currentHealth <= 0) {
                    registry.unregister(this);
                    return;
                }
                movement.move(this);
            }
        }

        if (isAnimatingAttack) {
            attackAnimation[direction].update();
            if (attackAnimation[direction].isFinished()) {
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
                attackAnimation[direction].reset();
                weapon.use();
            }
        }

        if (inputs.getHeldKeys().contains(KeyEvent.VK_1)) { abilityManger.use(1); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_2)) { abilityManger.use(2); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_3)) { abilityManger.use(3); }
        if (inputs.getHeldKeys().contains(KeyEvent.VK_4)) { abilityManger.use(4); }
    }

    /**
     * Gibt den aktuell darzustellenden Sprite des Spielers zurück.
     * Während eines Angriffs wird der Angriffsframe genutzt,
     * ansonsten der aktuelle Laufanimationsframe.
     */
    public BufferedImage getSprite() {
        if (isAnimatingAttack) return attackAnimation[direction].getCurrentFrame();
        if (currentAnimation == null) return null;
        return currentAnimation.getCurrentFrame();
    }

    /**
     * Gibt die aktuellen Skillpoints des Spielers zurück.
     */
    public int getSkillpoints() { return skillpoints; }

    /**
     * Setzt die Skillpoints des Spielers auf den angegebenen Wert.
     */
    public void setSkillpoints(int number) { skillpoints = number; }
}