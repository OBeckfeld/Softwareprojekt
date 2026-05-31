package entities.enemies;

import entities.EnemyProjectile;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import main.GamePanel;
import skilltree.SkillTree;
import tools.Animation;
import tools.SpriteSheet;
import tools.TileManager;
import tools.Vector;
import java.awt.image.BufferedImage;

/**
 * Gegner der aus der Ferne schießt und bei Annäherung des Spielers flieht.
 * Hält eine optimale Distanz zum Spieler und schießt in regelmäßigen Abständen.
 */
public class RangedEnemy extends Enemy {
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
    private static final int SHOOT_COOLDOWN = 60;  // Ticks zwischen zwei Schüssen
    private int shootTimer = 0;                    // zählt Ticks seit letztem Schuss
    /**
     * Erstellt einen neuen RangedEnemy.
     */    

    public RangedEnemy(int x, int y, int width, int height,
                       EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackRegistry, tileManager, gamePanel);
        currentHealth = 30;
        setSpeed(3);
        viewRange = 400;
        damage = 10;

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

    public RangedEnemy(int x, int y, int width, int height, int health, int damage, int defense, int viewrange, int hitCooldown, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 60, registry, attackRegistry, tileManager, gamePanel);
        this.maxHealth = health;
        this.currentHealth = health;
        this.damage = damage;
        this.hitCooldown = hitCooldown;
        this.defense = defense;
        setSpeed(3);
        pointsOnDeath = 2;

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

    /**
     * Wird jeden Tick aufgerufen.
     * Prüft Tod, aktualisiert Schuss- oder Lauf-Animation.
     */
    @Override
    public void update() {
        if (currentHealth <= 0) {
            registry.unregister(this);
            return;
        }
        super.update();

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

        shootTimer++;
        Vector toPlayer = new Vector(getX(), getY(), player.getX(), player.getY());
        double dist = toPlayer.getLength();

        if (dist < FLEE_RANGE) {
            // Spieler zu nah → vom Spieler wegbewegen
            isMoving = true;
            isShooting = false;
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
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
            // Spieler außer Reichweite → stillstehen
            isMoving = false;
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