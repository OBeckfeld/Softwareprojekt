package entities.enemies;

import entities.Entity;
import entities.Player;
import entities.PlayerTypeEntity;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import entities.Entity;
import main.GamePanel;
import skilltree.SkillTree;
import tools.Animation;
import tools.SpriteSheet;
import tools.TileManager;
import tools.Vector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Standardgegner, der den Spieler verfolgt, angreift und bei niedrigem Leben flieht.
 * Erbt von PlayerTypeEntity und besitzt einen eigenen SkillTree sowie Animationen
 * für Laufen, Angreifen und Idle.
 */
public class Enemy extends PlayerTypeEntity {

    /** Referenz auf den Spieler, sobald dieser in Sichtweite ist. */
    protected Player player;

    /** SpriteSheet mit allen Animations-Frames des Gegners. */
    private SpriteSheet sheet;

    /** Aktuell abgespielte Animation. */
    private Animation currentAnimation;

    /** Lauf-Animationen für alle 4 Richtungen (rechts, unten, links, oben). */
    private Animation[] animations;

    /** Angriffs-Animation. */
    private Animation attackAnimation;

    /** Idle-Animation wenn der Gegner stillsteht. */
    private Animation idleAnimation;

    /** Gibt an, ob der Gegner sich gerade bewegt. */
    private boolean isMoving = false;

    /** Gibt an, ob der Gegner gerade angreift. */
    private boolean isAttacking = false;

    /** Letzte Richtung, um unnötige Animation-Wechsel zu vermeiden. */
    private int lastDirection = -1;

    /** Cooldown zwischen zwei Treffern in Ticks. */
    protected int hitCooldown;

    /** Vertikale Angriffsreichweite. */
    protected int verticalRange;

    /** Horizontale Angriffsreichweite. */
    protected int horizontalRange;

    /** Gibt an, ob der Fluchtmodus aktiv ist. */
    protected boolean fleeMode = false;

    /** Schwellenwert für den Fluchtmodus: 30% der maximalen HP. */
    private static final double FLEE_HEALTH = 0.3;


    private double healTime = 8;

    private int healCounter = 0;

    /**
     * Erstellt einen Standard-Gegner mit Standardwerten.
     *
     * @param x              X-Koordinate
     * @param y              Y-Koordinate
     * @param width          Breite der Hitbox
     * @param height         Höhe der Hitbox
     * @param hitCooldown    Cooldown zwischen Treffern in Ticks
     * @param registry       EntityRegistry zum Registrieren der Entity
     * @param attackRegistry AttackRegistry für Angriffe
     * @param tileManager    TileManager für Kollisionen
     * @param gamePanel      GamePanel für SkillTree
     */
    public Enemy(int x, int y, int width, int height, int hitCooldown,
                 EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager,
                 GamePanel gamePanel) {
        super(x, y, width, height, 100, hitCooldown, registry, attackRegistry, tileManager, gamePanel);
        defaultSpeed = 2;
        speed = defaultSpeed;
        viewRange = 500;
        mass = 1;
        currentHealth = 100;
        player = null;
        damage = 20;
        skillTree = new SkillTree(this, gamePanel);

        initAnimations();
        this.maxHealth = 100;
        this.currentHealth = 100;
        this.weapon.setDamage(damage);
        this.weapon.setHitCooldown(hitCooldown);
        this.defense = 0;
        pointsOnDeath = 1;
    }

    /**
     * Erstellt einen Gegner mit vollständig anpassbaren Werten.
     *
     * @param x               X-Koordinate
     * @param y               Y-Koordinate
     * @param width           Breite der Hitbox
     * @param height          Höhe der Hitbox
     * @param health          Maximale Trefferpunkte
     * @param damage          Schadenswert
     * @param defense         Rüstungswert in Prozent
     * @param verticalRange   Vertikale Angriffsreichweite
     * @param horizontalRange Horizontale Angriffsreichweite
     * @param attackDuration  Dauer eines Angriffs in Ticks
     * @param hitCooldown     Cooldown zwischen Treffern in Ticks
     * @param registry        EntityRegistry
     * @param attackRegistry  AttackRegistry
     * @param tileManager     TileManager
     * @param gamePanel       GamePanel
     */
    public Enemy(int x, int y, int width, int height, int health, int damage, int defense,
                 int verticalRange, int horizontalRange, int attackDuration, int hitCooldown,
                 EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager,
                 GamePanel gamePanel) {
        super(x, y, width, height, attackDuration, hitCooldown, registry, attackRegistry, tileManager, gamePanel);
        this.hitCooldown = hitCooldown;
        this.maxHealth = health;
        this.currentHealth = health;
        this.weapon.setDamage(damage);
        this.weapon.setHitCooldown(hitCooldown);
        this.defense = defense;
        this.verticalRange = verticalRange;
        this.horizontalRange = horizontalRange;

        initAnimations();
        pointsOnDeath = 1;
    }

    /**
     * Lädt das SpriteSheet und initialisiert alle Animationen
     * (Laufen in 4 Richtungen, Angriff, Idle).
     */
    private void initAnimations() {
        sheet = new SpriteSheet("src/data/sprites/meleeenemy.png", 161, 161);

        animations = new Animation[4];
        animations[0] = new Animation(new BufferedImage[]{
                sheet.getFrame(0, 0), sheet.getFrame(0, 1),
                sheet.getFrame(0, 2), sheet.getFrame(0, 3)
        }, 10, true);
        animations[2] = new Animation(new BufferedImage[]{
                sheet.getFrameMirrored(0, 0), sheet.getFrameMirrored(0, 1),
                sheet.getFrameMirrored(0, 2), sheet.getFrameMirrored(0, 3)
        }, 10, true);
        animations[3] = new Animation(new BufferedImage[]{
                sheet.getFrame(1, 0), sheet.getFrame(1, 1)
        }, 10, true);
        animations[1] = new Animation(new BufferedImage[]{
                sheet.getFrame(1, 2), sheet.getFrame(1, 3)
        }, 10, true);

        attackAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                sheet.getFrame(2, 2), sheet.getFrame(2, 3)
        }, 20, false);

        idleAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(3, 0), sheet.getFrame(3, 1),
                sheet.getFrame(3, 2), sheet.getFrame(3, 3)
        }, 15, true);

        currentAnimation = animations[0];
    }

    /**
     * Aktualisiert den Gegner jeden Tick.
     * Friert ein wenn der SkillTree des Spielers aktiv ist.
     * Verwaltet Tod, Fluchtmodus, Spielererkennung, Bewegung und Animationen.
     */
    @Override
    public void update() {
        if (player != null && player.getSkillTree() != null && player.getSkillTree().isActive) return;

        if (skillTree != null && !skillTree.isActive) {
            super.update();
            if (currentHealth <= 0) {
                registry.unregister(this);
                return;
            }
        }

        if ((double) currentHealth / maxHealth < FLEE_HEALTH) {
            fleeMode = true;
        }

        if (player == null) {
            ArrayList<Entity> inView = getInView();
            for (Entity entity : inView) {
                if (entity instanceof Player) {
                    player = (Player) entity;
                }
            }
        }

        handleMovement();

        if (idleAnimation == null) {
            return;
        }

        if (isAttacking) {
            attackAnimation.update();
            if (attackAnimation.isFinished()) {
                isAttacking = false;
            }
        } else if (isMoving) {
            if (direction != lastDirection) {
                currentAnimation = animations[direction];
                lastDirection = direction;
            }
            currentAnimation.update();
        } else {
            idleAnimation.update();
        }
    }

    /**
     * Steuert Bewegung und Angriff des Gegners.
     * Verfolgt den Spieler, greift in Reichweite an oder flieht bei niedrigem Leben.
     */
    protected void handleMovement() {
        if (player == null) {
            isMoving = false;
            return;
        }

        if (fleeMode) {
            flee();
            healCounter ++;
            if(healCounter == healTime*120){
                setHealth(maxHealth);
                fleeMode = false;
            }
            return;
        }

        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());

        if (vector.getLength() > speed) {
            isMoving = true;
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
            if (Math.abs(dx) >= Math.abs(dy)) {
                direction = dx > 0 ? 0 : 2;
            } else {
                direction = dy > 0 ? 1 : 3;
            }
            if (!isAttacking) {
                vector.setLength(speed);
                move(vector);
            }
        }

        if (registry.getInRange(this, 100, 100).contains(player)) {
            isMoving = false;
            if (!isAttacking) {
                isAttacking = true;

                if (direction == 2) {
                    attackAnimation = new Animation(new BufferedImage[]{
                            sheet.getFrameMirrored(2, 0), sheet.getFrameMirrored(2, 1),
                            sheet.getFrameMirrored(2, 2), sheet.getFrameMirrored(2, 3)
                    }, 30, false);
                } else {
                    attackAnimation = new Animation(new BufferedImage[]{
                            sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                            sheet.getFrame(2, 2), sheet.getFrame(2, 3)
                    }, 30, false);
                }
                tryAttackEntity((PlayerTypeEntity) player);
            } else if (attackAnimation.isFinished()) {
                isAttacking = false;
            }
        }
    }

    /**
     * Bewegt den Gegner vom Spieler weg und aktualisiert die Laufrichtung.
     */
    private void flee() {
        isAttacking = false;
        isMoving = true;

        Vector fleeVector = new Vector(player.getX(), player.getY(), getX(), getY());
        fleeVector.setLength(getSpeed());
        move(fleeVector);

        double dx = getX() - player.getX();
        double dy = getY() - player.getY();
        if (Math.abs(dx) >= Math.abs(dy)) {
            direction = dx > 0 ? 0 : 2;
        } else {
            direction = dy > 0 ? 1 : 3;
        }
    }

    /**
     * Verarbeitet erlittenen Schaden.
     *
     * @param damage   Schadenswert
     * @param source   Angreifende Entity
     * @param piercing Gibt an ob der Angriff rüstungsdurchdringend ist
     */
    @Override
    public void takeDamage(int damage, PlayerTypeEntity source, boolean piercing) {
        player = (Player) source;
        super.takeDamage(damage, source, piercing);
    }

    /**
     * Richtet den Gegner zum Ziel aus und führt einen Angriff aus.
     *
     * @param targetPlayer Ziel des Angriffs
     */
    protected void tryAttackEntity(PlayerTypeEntity targetPlayer) {
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        weapon.use();
    }

    /**
     * Gibt das aktuelle Sprite des Gegners zurück.
     * Priorität: Angriff → Laufen → Idle.
     *
     * @return Aktueller Animations-Frame als BufferedImage
     */
    public BufferedImage getSprite() {
        if (idleAnimation == null) return null;
        if (isAttacking) return attackAnimation.getCurrentFrame();
        if (isMoving) return currentAnimation.getCurrentFrame();
        return idleAnimation.getCurrentFrame();
    }
}