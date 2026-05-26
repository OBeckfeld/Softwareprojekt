package entities.enemies;

import entities.Entity;
import entities.Player;
import entities.PlayerTypeEntity;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.Animation;
import tools.SpriteSheet;
import tools.TileManager;
import tools.Vector;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Basis-Klasse für alle Gegner im Spiel.
 * Enthält Lauf-, Angriffs- und Idle-Animationen sowie die grundlegende
 * Bewegungs- und Angriffslogik für Nahkampf-Gegner.
 */
public class Enemy extends PlayerTypeEntity {

    // Referenz auf den Spieler, wird gesetzt sobald er in Sichtweite ist
    protected Player player;

    private SpriteSheet sheet;
    private Animation currentAnimation;   // aktuell abgespielte Lauf-Animation
    private Animation[] animations;       // Lauf-Animationen für alle 4 Richtungen
    private Animation attackAnimation;    // Angriffs-Animation (richtungsabhängig)
    private Animation idleAnimation;      // Idle-Animation wenn der Gegner steht
    private boolean isMoving = false;     // ob der Gegner sich gerade bewegt
    private boolean isAttacking = false;  // ob der Gegner gerade angreift
    private int lastDirection = -1;       // letzte Bewegungsrichtung (für Animationswechsel)

    /**
     * Erstellt einen neuen Nahkampf-Gegner.
     *
     * @param x             X-Startposition
     * @param y             Y-Startposition
     * @param width         Breite der Entity
     * @param height        Höhe der Entity
     * @param hitCooldown   Ticks zwischen zwei Treffern
     * @param registry      Referenz auf die EntityRegistry
     * @param attackRegistry Referenz auf die AttackRegistry
     * @param tileManager   Referenz auf den TileManager
     */
    public Enemy(int x, int y, int width, int height, int hitCooldown,
                 EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, 100, hitCooldown, registry, attackRegistry, tileManager);
        defaultSpeed = 2;
        speed = defaultSpeed;
        viewRange = 500;
        mass = 1;
        currentHealth = 100;
        player = null;
        damage = 20;

        sheet = new SpriteSheet("src/sprites/meleeenemy.png", 161, 161);

        // Lauf-Animationen: Reihe 0 seitlich, Reihe 1 oben/unten
        animations = new Animation[4];
        animations[0] = new Animation(new BufferedImage[]{ // rechts
                sheet.getFrame(0, 0), sheet.getFrame(0, 1),
                sheet.getFrame(0, 2), sheet.getFrame(0, 3)
        }, 10, true);
        animations[2] = new Animation(new BufferedImage[]{ // links (gespiegelt)
                sheet.getFrameMirrored(0, 0), sheet.getFrameMirrored(0, 1),
                sheet.getFrameMirrored(0, 2), sheet.getFrameMirrored(0, 3)
        }, 10, true);
        animations[3] = new Animation(new BufferedImage[]{ // oben
                sheet.getFrame(1, 0), sheet.getFrame(1, 1)
        }, 10, true);
        animations[1] = new Animation(new BufferedImage[]{ // unten
                sheet.getFrame(1, 2), sheet.getFrame(1, 3)
        }, 10, true);

        // Angriffs-Animation: Reihe 2
        attackAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                sheet.getFrame(2, 2), sheet.getFrame(2, 3)
        }, 20, false);

        // Idle-Animation: Reihe 3
        idleAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(3, 0), sheet.getFrame(3, 1),
                sheet.getFrame(3, 2), sheet.getFrame(3, 3)
        }, 15, true);

        currentAnimation = animations[0];
    }

    /**
     * Wird jeden Tick aufgerufen.
     * Prüft den Tod, sucht den Spieler falls noch nicht gefunden,
     * führt Bewegung aus und aktualisiert die Animationen.
     */
    @Override
    public void update() {
        super.update();

        // Gegner stirbt wenn Gesundheit auf 0 fällt
        if (currentHealth <= 0) {
            registry.unregister(this);
            return;
        }

        // Spieler suchen falls noch nicht in Sichtweite
        if (player == null) {
            ArrayList<Entity> inView = getInView();
            for (Entity entity : inView) {
                if (entity instanceof Player) {
                    player = (Player) entity;
                }
            }
        }

        handleMovement();

        // Animations-Update: Angriff hat Priorität vor Laufen, dann Idle
        if (isAttacking) {
            attackAnimation.update();
            if (attackAnimation.isFinished()) {
                isAttacking = false;
            }
        } else if (isMoving) {
            // Richtungswechsel → neue Animation laden
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
     * Steuert die Bewegung des Gegners zum Spieler hin.
     * Greift an wenn der Spieler nah genug ist.
     */
    protected void handleMovement() {
        if (player == null) {
            isMoving = false;
            return;
        }

        Vector vector = new Vector(getX(), getY(), player.getX(), player.getY());

        // Auf Spieler zu bewegen solange Abstand größer als Geschwindigkeit
        if (vector.getLength() > speed) {
            isMoving = true;
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
            // Richtung bestimmen: horizontal oder vertikal dominierend?
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

        // Angriff auslösen wenn Spieler in Reichweite
        if (registry.getInRange(this, 100, 100).contains(player)) {
            isMoving = false;
            if (!isAttacking) {
                isAttacking = true;
                // Angriffs-Animation je nach Richtung spiegeln
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

    /**
     * Führt den Angriff auf eine Ziel-Entity aus.
     * Richtet die Angriffsrichtung aus und nutzt die Waffe.
     *
     * @param targetPlayer die zu angreifende Entity
     */
    protected void tryAttackEntity(PlayerTypeEntity targetPlayer) {
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        weapon.use();
    }

    /**
     * Gibt das aktuelle Sprite zurück.
     * Priorität: Angriff → Laufen → Idle
     */
    public BufferedImage getSprite() {
        if (isAttacking) return attackAnimation.getCurrentFrame();
        if (isMoving) return currentAnimation.getCurrentFrame();
        return idleAnimation.getCurrentFrame();
    }
}