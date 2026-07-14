package entities.enemies;

import Weapons.Weapon;
import entities.EnemyProjectile;
import entities.Entity;
import entities.Player;
import entities.PlayerTypeEntity;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import main.GamePanel;
import skilltree.SkillTree;
import tools.Animation;
import tools.SpriteSheet;
import tools.TileManager;
import tools.Vector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Gegner der aus der Ferne schießt und bei Annäherung des Spielers flieht.
 * Hält eine optimale Distanz zum Spieler und schießt in regelmäßigen Abständen.
 */
public class Boss extends Enemy {
    private SpriteSheet sheet;
    private Animation currentAnimation;  // aktuell abgespielte Lauf-Animation
    private Animation[] animations;      // Lauf-Animationen für alle 4 Richtungen
    private Animation shootAnimation;    // Schuss-Animation (einmalig pro Schuss)
    private boolean isShooting = false;  // ob gerade die Schuss-Animation abläuft
    private boolean isMoving = false;
    private int lastDirection = -1;
    int size = 12; // Größe des Projektils in Pixeln
    private static final int FLEE_RANGE    = 150;  // Abstand unter dem der Gegner flieht
    private static final int ATTACK_RANGE  = 300;  // maximale Schussreichweite
    private static int SHOOT_COOLDOWN = 60;  // Ticks zwischen zwei Schüssen
    private int shootTimer = 0;                    // zählt Ticks seit letztem Schuss

    private Weapon weapon1;
    private Weapon weapon2;
    private int phase;
    private boolean healing = false;
    private int healTime;
    private int waiting = 0;

    /**
     * Erstellt einen neuen RangedEnemy.
     */

    public Boss(int x, int y, int width, int height,
                EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackRegistry, tileManager, gamePanel);
        currentHealth = 30;
        setSpeed(3);
        viewRange = 400;
        damage = 10;
        phase = 1;
        healTime = 100;

        sheet = new SpriteSheet("src/data/sprites/gegnerranged.png", 161, 161);


        // Lauf-Animationen: Reihe 0 seitlich (4 Frames), Reihe 3 oben/unten (1 Frame)
        animations = new Animation[4];
        animations[0] = new Animation(new BufferedImage[]{ // rechts
                sheet.getFrame(0, 0), sheet.getFrame(0, 1),
                sheet.getFrame(0, 2), sheet.getFrame(0, 3)
        }, 10, true);
        animations[2] = new Animation(new BufferedImage[]{ // links (gespiegelt)
                sheet.getFrameMirrored(0, 0), sheet.getFrameMirrored(0, 1),
                sheet.getFrameMirrored(0, 2), sheet.getFrameMirrored(0, 3)
        }, 10, true);
        animations[1] = new Animation(new BufferedImage[]{ // unten (statisch)
                sheet.getFrame(3, 0)
        }, 10, true);
        animations[3] = new Animation(new BufferedImage[]{ // oben (statisch)
                sheet.getFrame(3, 0)
        }, 10, true);

        // Schuss-Animation: Reihe 2, 4 Frames, einmalig
        shootAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                sheet.getFrame(2, 2), sheet.getFrame(2, 3)
        }, 8, false);

        currentAnimation = animations[0];
    }

    public Boss(int x, int y, int width, int height, int health, int damage, int defense, int viewrange, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, hitCooldown, registry, attackRegistry, tileManager, gamePanel);
        this.maxHealth = health;
        this.currentHealth = health;
        this.damage = damage;
        this.hitCooldown = hitCooldown;
        this.defense = defense;
        setSpeed(3);
        pointsOnDeath = 30;
        phase = 1;
        healTime = 300;

        sheet = new SpriteSheet("src/data/sprites/gegnerranged.png", 161, 161);


        // Lauf-Animationen: Reihe 0 seitlich (4 Frames), Reihe 3 oben/unten (1 Frame)
        animations = new Animation[4];
        animations[0] = new Animation(new BufferedImage[]{ // rechts
                sheet.getFrame(0, 0), sheet.getFrame(0, 1),
                sheet.getFrame(0, 2), sheet.getFrame(0, 3)
        }, 10, true);
        animations[2] = new Animation(new BufferedImage[]{ // links (gespiegelt)
                sheet.getFrameMirrored(0, 0), sheet.getFrameMirrored(0, 1),
                sheet.getFrameMirrored(0, 2), sheet.getFrameMirrored(0, 3)
        }, 10, true);
        animations[1] = new Animation(new BufferedImage[]{ // unten (statisch)
                sheet.getFrame(3, 0)
        }, 10, true);
        animations[3] = new Animation(new BufferedImage[]{ // oben (statisch)
                sheet.getFrame(3, 0)
        }, 10, true);

        // Schuss-Animation: Reihe 2, 4 Frames, einmalig
        shootAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                sheet.getFrame(2, 2), sheet.getFrame(2, 3)
        }, 8, false);

        currentAnimation = animations[0];
    }


    public void setWeapon1(Weapon weapon1) {
        this.weapon1 = weapon1;
    }

    public void setWeapon2(Weapon weapon2) {
        this.weapon2 = weapon2;
    }

    /**
     * Wird jeden Tick aufgerufen.
     * Prüft Tod, aktualisiert Schuss- oder Lauf-Animation.
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

        if (currentHealth < maxHealth/3 && phase == 1) {
            phase = 2;
            healing = true;
            SHOOT_COOLDOWN = 35;
        }
        if (healing) {
            if (waiting > healTime) {
                gainLife(200);
                healing = false;
                waiting = 0;
            } else {
                waiting++;
            }
            return;
        }


        if (player == null) {
            ArrayList<Entity> inView = getInView();
            for (Entity entity : inView) {
                if (entity instanceof Player) {
                    player = (Player) entity;
                }
            }
        }

        if (idleAnimation == null) {
            return;
        }

        if (isAttacking) {
            attackAnimation.update();
            if (attackAnimation.isFinished()) {
                isAttacking = false;
            }
        } else {
            idleAnimation.update();
        }
        // Schuss-Animation hat Priorität vor Lauf-Animation
        if (isShooting) {
            shootAnimation.update();
            if (shootAnimation.isFinished()) {
                isShooting = false;
            }
        } else if (isMoving) {
            if (direction != lastDirection) {
                currentAnimation = animations[direction];
                lastDirection = direction;
            }
            currentAnimation.update();
        }

    }

    /**
     * Steuert das Verhalten abhängig von der Distanz zum Spieler:
     * - Zu nah (< FLEE_RANGE): flieht vom Spieler weg
     * - In Reichweite (< ATTACK_RANGE): schießt wenn Cooldown abgelaufen
     * - Zu weit: steht still
     */
    @Override
    protected void handleMovement() {
        if (player == null) {
            isMoving = false;
            return;
        }

        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());

        if (phase == 1){
            if (vector.getLength() > ATTACK_RANGE && !isAttacking) {
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
            if ((registry.getInRange(this, 100, 100).contains(player) || attackDelay != 50)&& !weapon.onCooldown()) {
                isMoving = false;
                isAttacking = true;
                if (attackDelay == 0) {

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
                    tryAttackEntity(player);
                    attackDelay = 50;
                    isAttacking = false;
                } else {
                    attackDelay--;
                }
            }
            shootTimer++;
            Vector toPlayer = new Vector(getX(), getY(), player.getX(), player.getY());
            double dist = toPlayer.getLength();
            if (dist < FLEE_RANGE) {
                // Spieler zu nah → vom Spieler wegbewegen
                isMoving = true;
                isShooting = false;
                double dx = player.getX() + getX();
                double dy = player.getY() + getY();
                if (Math.abs(dx) >= Math.abs(dy)) {
                    direction = dx > 0 ? 2 : 0; // invertiert: weg vom Spieler
                } else {
                    direction = dy > 0 ? 3 : 1;
                }
                Vector fleeVector = new Vector(player.getX(), player.getY(), getX(), getY());
                fleeVector.setLength(getSpeed());
                move(fleeVector);

            } else if (dist <= ATTACK_RANGE) {
                // Spieler in Schussreichweite → schießen wenn Cooldown abgelaufen
                isMoving = false;
                if (shootTimer >= SHOOT_COOLDOWN) {
                    shootTimer = 0;
                    isShooting = true;

                    // Schuss-Animation neu starten
                    shootAnimation = new Animation(new BufferedImage[]{
                            sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                            sheet.getFrame(2, 2), sheet.getFrame(2, 3)
                    }, 8, false);

                    // Projektil in Richtung Spieler abfeuern
                    double x = getCenter()[0];
                    double y = getCenter()[1];
                    Vector shootVector = new Vector(x, y, player.getCenter()[0], player.getCenter()[1]);
                    shootVector.setLength(10);
                    new EnemyProjectile(x, y, size, size, registry, attackRegistry,
                            this, 5, shootVector, 120, damage, tileManager);
                }
            } else {
                isMoving = true;
                isShooting = false;
                super.handleMovement();
            }
        }
        if(phase == 2){
            shootTimer++;
            Vector toPlayer = new Vector(getX(), getY(), player.getX(), player.getY());
            double dist = toPlayer.getLength();
            isMoving = false;
            if (shootTimer >= SHOOT_COOLDOWN) {
                shootTimer = 0;
                isShooting = true;

                // Schuss-Animation neu starten
                shootAnimation = new Animation(new BufferedImage[]{
                        sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                        sheet.getFrame(2, 2), sheet.getFrame(2, 3)
                }, 8, false);

                // Projektil in Richtung Spieler abfeuern
                double x = getCenter()[0];
                double y = getCenter()[1];
                Vector shootVector = new Vector(x, y, player.getCenter()[0], player.getCenter()[1]);
                shootVector.setLength(10);
                new EnemyProjectile(x, y, size, size, registry, attackRegistry,
                        this, 7, shootVector, 120, damage+3, tileManager);
            }

        }


    }

    /**
     * Gibt das aktuelle Sprite zurück.
     * Schuss-Animation hat Priorität vor Lauf-Animation.
     */
    @Override
    public BufferedImage getSprite() {
        if (isShooting) return shootAnimation.getCurrentFrame();
        if (currentAnimation == null) return null;
        return currentAnimation.getCurrentFrame();
    }
}