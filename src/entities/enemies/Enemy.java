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
 * Gegner, der den Spieler verfolgt, angreift und bei wenig Leben flieht.
 */
public class Enemy extends PlayerTypeEntity {

    protected Player player;

    private SpriteSheet sheet;
    private Animation currentAnimation;
    private Animation[] animations;
    private Animation attackAnimation;
    private Animation idleAnimation;
    private boolean isMoving = false;
    private boolean isAttacking = false;
    private int lastDirection = -1;
    protected int hitCooldown;
    protected int verticalRange;
    protected int horizontalRange;

    // Flucht-Logik
    protected boolean fleeMode = false;
    private static final double FLEE_HEALTH = 0.3; // 30% Health

    /**
     * Erstellt einen Standard-Gegner und initialisiert Werte, SpriteSheet,
     * Laufanimationen, Angriffsanimation und Idle-Animation.
     */
    public Enemy(int x, int y, int width, int height, int hitCooldown,
                 EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager,
                 GamePanel gamePanel) {
        super(x, y, width, height, 100, hitCooldown, registry, attackRegistry, tileManager, gamePanel);
        defaultSpeed = 2;
        registry.register(this);
        this.registry = registry;
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

    public Enemy(int x, int y, int width, int height, int health, int damage, int defense, int verticalRange, int horizontalRange, int attackDuration, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, attackDuration, hitCooldown, registry, attackRegistry, tileManager, gamePanel);
        this.hitCooldown = hitCooldown;
        this.maxHealth = health;
        this.currentHealth = health;
        this.weapon.setDamage(damage);
        this.weapon.setHitCooldown(hitCooldown);
        this.defense = defense;
        this.verticalRange = verticalRange;
        this.horizontalRange = horizontalRange;
        skillTree = new SkillTree(this, gamePanel);
        initAnimations();
        pointsOnDeath = 1;
    }

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
     * Aktualisiert den Gegner pro Tick.
     * Dabei werden Tod, Fluchtmodus, Spielererkennung, Bewegung
     * sowie Angriffs-, Lauf- und Idle-Animationen verarbeitet.
     */
    @Override
    public void update() {
        if (player != null && player.getSkillTree().isActive) return;

        if(skillTree != null && !skillTree.isActive){
            super.update();
            if (currentHealth <= 0) {
                registry.unregister(this);
                return;
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
     * Steuert die Bewegung und das Angriffsverhalten des Gegners.
     * Der Gegner verfolgt den Spieler, greift in Reichweite an
     * oder flieht, wenn der Fluchtmodus aktiv ist.
     */
    protected void handleMovement() {
        if (player == null) {
            isMoving = false;
            return;
        }

        if (fleeMode) {
            flee();
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
                    }, 20, false);
                } else {
                    attackAnimation = new Animation(new BufferedImage[]{
                            sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                            sheet.getFrame(2, 2), sheet.getFrame(2, 3)
                    }, 20, false);
                }
                tryAttackEntity((PlayerTypeEntity) player);
            } else if (attackAnimation.isFinished()) {
                isAttacking = false;
            }
        }
    }

    // ── Flucht-Logik ─────────────────────────────────────────
    /**
     * Bewegt den Gegner vom Spieler weg und aktualisiert dabei
     * die Bewegungsrichtung passend zur Fluchtbewegung.
     */
    private void flee() {
        isAttacking = false;
        isMoving = true;


        Vector fleeVector = new Vector(player.getX(), player.getY(), getX(), getY());
        fleeVector.setLength(getSpeed() * 1); //
        move(fleeVector);

        // Richtung aktualisieren (umgekehrt weil wegrennen)
        double dx = getX() - player.getX();
        double dy = getY() - player.getY();
        if (Math.abs(dx) >= Math.abs(dy)) {
            direction = dx > 0 ? 0 : 2;
        } else {
            direction = dy > 0 ? 1 : 3;
        }


    }

    /**
     * Verarbeitet erlittenen Schaden und setzt den angreifenden Spieler
     * als Ziel des Gegners.
     */
    @Override
    public void takeDamage(int damage, PlayerTypeEntity source, boolean piercing){ //wenn gegner Schaden nimmt, sieht er den Player

        super.takeDamage(damage, source, piercing);
    }

    /**
     * Richtet den Gegner zum Ziel aus und benutzt die aktuelle Waffe.
     */
    protected void tryAttackEntity(PlayerTypeEntity targetPlayer) {
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        weapon.use();
    }

    /**
     * Gibt das aktuelle Sprite des Gegners zurück.
     * Angriffsanimation hat Priorität vor Laufanimation und Idle-Animation.
     */
    public BufferedImage getSprite() {
        if(idleAnimation == null) {return null;}
        if (isAttacking) return attackAnimation.getCurrentFrame();
        if (isMoving) return currentAnimation.getCurrentFrame();
        return idleAnimation.getCurrentFrame();
    }
}
