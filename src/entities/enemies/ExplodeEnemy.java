package entities;

import tools.SpriteSheet;
import tools.Animation;
import java.awt.image.BufferedImage;
import entities.managers.EntityRegistry;
import entities.managers.AttackManager;
import tools.Vector;

public class ExplodeEnemy extends Enemy {

    private SpriteSheet sheet;
    private Animation currentAnimation;
    private Animation[] animations;
    private Animation explodeAnimation;
    private boolean explodeAnimationDone = false;
    private int lastDirection = -1;
    private boolean isMoving = false;

    private static final int EXPLODE_RANGE = 100;
    private static final int EXPLODE_DELAY = 120;
    private static final int BLAST_RADIUS = 100;
    private static final int EXPLODE_DMG = 500;
    private boolean exploded = false;
    private int explodeTimer = 0;
    private boolean isExploding = false;

    public ExplodeEnemy(int x, int y, int width, int height,
                        EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, 0, registry, attackManager);
        health = 1;
        setSpeed(3);
        viewRange = 300;

        sheet = new SpriteSheet("src/sprites/gegnerexplosive2.png", 161, 161);
        animations = new Animation[4];
        animations[0] = new Animation(new BufferedImage[]{
                sheet.getFrame(2, 0), sheet.getFrame(2, 1), sheet.getFrame(2, 2)
        }, 10, true); // ← loop = true

        animations[1] = new Animation(new BufferedImage[]{
                sheet.getFrame(1, 0), sheet.getFrame(1, 1), sheet.getFrame(1, 2)
        }, 10, true);

        animations[2] = new Animation(new BufferedImage[]{
                sheet.getFrame(3, 0), sheet.getFrame(3, 1), sheet.getFrame(3, 2)
        }, 10, true);

        animations[3] = new Animation(new BufferedImage[]{
                sheet.getFrame(0, 0), sheet.getFrame(0, 1), sheet.getFrame(0, 2)
        }, 10, true);

        explodeAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(0, 3),
                sheet.getFrame(1, 3),
                sheet.getFrame(2, 3),
                sheet.getFrame(3, 3)
        }, 8, false); // ← loop = false, stoppt am Ende
        currentAnimation = animations[0];
    }


    private void explode() {
        isExploding = true; // Countdown starten, noch nicht explodiert
    }

    private void doExplode() {
        exploded = true;
        Attack attack = attackManager.newAttack(this, getCenter()[0] - BLAST_RADIUS,
                getCenter()[1] - BLAST_RADIUS,
                BLAST_RADIUS * 2, BLAST_RADIUS * 2,
                5, EXPLODE_DMG);
        attackManager.attack(attack);
        attackManager.damageDistribution();
    }


    @Override
    public void update() {
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
        if (isExploding) {
            explodeTimer++;
            if (explodeTimer >= EXPLODE_DELAY) {
                doExplode();
            }
            return; // bewegt sich nicht mehr während Countdown
        }
        if (health <= 0) {
            explode();
            return;
        }
        super.update();
        if (isMoving) {
            currentAnimation.update();
        }
    }

    @Override
    protected void handleMovement() {
        if (player == null || exploded) {
            isMoving = false;
            return;
        }
        Vector toPlayer = new Vector(
                getCenter()[0], getCenter()[1],      // ← Mitte des Enemys
                player.getCenter()[0], player.getCenter()[1]  // ← Mitte des Spielers
        );

        if (toPlayer.getLength() <= EXPLODE_RANGE) {
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

    @Override
    public BufferedImage getSprite() {
        if (exploded) return explodeAnimation.getCurrentFrame();
        if (currentAnimation == null) return null; // ← null check
        return currentAnimation.getCurrentFrame();
    }

}