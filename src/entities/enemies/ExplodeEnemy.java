package entities.enemies;

import entities.Attack;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.Animation;
import tools.SpriteSheet;
import tools.TileManager;
import tools.Vector;
import java.awt.image.BufferedImage;

/**
 * Gegner der sich auf den Spieler zubewegt und explodiert
 * sobald er nah genug ist oder stirbt.
 * Nach dem Auslösen folgt ein Countdown bevor die Explosion stattfindet,
 * damit der Spieler noch reagieren kann.
 */
public class ExplodeEnemy extends Enemy {

    private SpriteSheet sheet;
    private Animation currentAnimation;
    private Animation[] animations;        // Lauf-Animationen für 4 Richtungen
    private Animation explodeAnimation;    // Explosions-Animation (einmalig)
    private boolean explodeAnimationDone = false; // ob die Explosions-Animation abgeschlossen ist
    private int lastDirection = -1;
    private boolean isMoving = false;

    private static final int EXPLODE_RANGE = 100;  // Abstand zum Spieler der Explosion auslöst
    private static final int EXPLODE_DELAY = 120;  // Ticks bis zur Explosion (2 Sek. bei 60 FPS)
    private static final int BLAST_RADIUS  = 100;  // Radius des Explosionsschadens
    private static final int EXPLODE_DMG   = 500;  // Schadenswert der Explosion

    private boolean exploded = false;      // ob die Explosion bereits stattgefunden hat
    private int explodeTimer = 0;          // zählt Ticks seit Explosion ausgelöst wurde
    private boolean isExploding = false;   // ob der Countdown läuft

    /**
     * Erstellt einen neuen ExplodeEnemy.
     */
    public ExplodeEnemy(int x, int y, int width, int height,
                        EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, width, height, 0, registry, attackRegistry, tileManager);
        currentHealth = 1;
        setSpeed(3);
        viewRange = 300;

        sheet = new SpriteSheet("src/sprites/gegnerexplosive2.png", 161, 161);

        // Lauf-Animationen für alle 4 Richtungen (je 3 Frames)
        animations = new Animation[4];
        animations[0] = new Animation(new BufferedImage[]{
                sheet.getFrame(2, 0), sheet.getFrame(2, 1), sheet.getFrame(2, 2)
        }, 10, true);
        animations[1] = new Animation(new BufferedImage[]{
                sheet.getFrame(1, 0), sheet.getFrame(1, 1), sheet.getFrame(1, 2)
        }, 10, true);
        animations[2] = new Animation(new BufferedImage[]{
                sheet.getFrame(3, 0), sheet.getFrame(3, 1), sheet.getFrame(3, 2)
        }, 10, true);
        animations[3] = new Animation(new BufferedImage[]{
                sheet.getFrame(0, 0), sheet.getFrame(0, 1), sheet.getFrame(0, 2)
        }, 10, true);

        // Explosions-Animation (Spalte 3, alle 4 Reihen, einmalig)
        explodeAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(0, 3), sheet.getFrame(1, 3),
                sheet.getFrame(2, 3), sheet.getFrame(3, 3)
        }, 8, false);

        currentAnimation = animations[0];
    }

    /**
     * Startet den Explosions-Countdown.
     */
    private void explode() {
        isExploding = true;
    }

    /**
     * Führt die eigentliche Explosion aus:
     * erstellt einen flächendeckenden Angriff und verteilt den Schaden sofort.
     */
    private void doExplode() {
        exploded = true;
        attackRegistry.attack(this,
                getCenter()[0] - BLAST_RADIUS,
                getCenter()[1] - BLAST_RADIUS,
                BLAST_RADIUS * 2, BLAST_RADIUS * 2,
                5, EXPLODE_DMG);
        attackRegistry.distributeDamage();
    }

    /**
     * Wird jeden Tick aufgerufen.
     * Verwaltet die drei Zustände: explodiert, explodierend (Countdown), normal.
     */
    @Override
    public void update() {
        // Zustand 1: Explosion hat stattgefunden → Animations-Abschluss abwarten, dann unregistrieren
        if (exploded) {
            if (!explodeAnimationDone) {
                explodeAnimation.update();
                if (explodeAnimation.isFinished()) {
                    explodeAnimationDone = true;
                    registry.unregister(this);
                }
            }
            return;
        }

        // Zustand 2: Countdown läuft → nach EXPLODE_DELAY Ticks explodieren
        if (isExploding) {
            explodeTimer++;
            if (explodeTimer >= EXPLODE_DELAY) {
                doExplode();
            }
            return;
        }

        // Zustand 3: normal → Tod prüfen, dann Bewegung
        if (currentHealth <= 0) {
            explode();
            return;
        }
        super.update();
        if (isMoving) {
            currentAnimation.update();
        }
    }

    /**
     * Bewegt den Gegner auf den Spieler zu.
     * Löst Explosion aus wenn Kontakt besteht.
     */
    @Override
    protected void handleMovement() {
        if (player == null || exploded) {
            isMoving = false;
            return;
        }

        Vector toPlayer = new Vector(
                getCenter()[0], getCenter()[1],
                player.getCenter()[0], player.getCenter()[1]
        );

        // Kollisionsdistanz: Summe der halben Breiten beider Entities
        double minDist = (getWidth() / 2.0) + (player.getWidth() / 2.0);

        if (toPlayer.getLength() <= minDist) {
            // Spieler berührt → Explosion auslösen
            explode();
        } else {
            isMoving = true;
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
            if (Math.abs(dx) >= Math.abs(dy)) {
                direction = dx > 0 ? 0 : 2;
            } else {
                direction = dy > 0 ? 1 : 3;
            }
            currentAnimation = animations[direction];
            toPlayer.setLength(getSpeed());
            move(toPlayer);
        }
    }

    /**
     * Gibt das aktuelle Sprite zurück.
     * Nach der Explosion wird die Explosions-Animation gezeigt.
     */
    public BufferedImage getSprite() {
        if (exploded) return explodeAnimation.getCurrentFrame();
        if (currentAnimation == null) return null;
        return currentAnimation.getCurrentFrame();
    }
}