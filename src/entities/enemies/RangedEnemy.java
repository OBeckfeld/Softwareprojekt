package entities;

import tools.SpriteSheet;
import tools.Animation;
import java.awt.image.BufferedImage;
import entities.managers.EntityRegistry;
import entities.managers.AttackManager;
import tools.Vector;

public class RangedEnemy extends Enemy {

    private SpriteSheet sheet;
    private Animation currentAnimation;
    private Animation[] animations;
    private Animation shootAnimation;
    private boolean isShooting = false;
    private boolean isMoving = false;
    private int lastDirection = -1;
    int size = 12;

    private static final int FLEE_RANGE    = 150;
    private static final int ATTACK_RANGE  = 300;
    private static final int SHOOT_COOLDOWN = 60;
    private int shootTimer = 0;

    public RangedEnemy(int x, int y, int width, int height,
                       EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, 60, registry, attackManager);
        health = 30;
        setSpeed(3);
        viewRange = 400;
        damage=10;

        sheet = new SpriteSheet("src/sprites/gegnerranged.png", 161, 161);

        animations = new Animation[4];
        animations[0] = new Animation(new BufferedImage[]{
                sheet.getFrame(0, 0), sheet.getFrame(0, 1),
                sheet.getFrame(0, 2), sheet.getFrame(0, 3)
        }, 10, true);
        animations[2] = new Animation(new BufferedImage[]{
                sheet.getFrameMirrored(0, 0), sheet.getFrameMirrored(0, 1),
                sheet.getFrameMirrored(0, 2), sheet.getFrameMirrored(0, 3)
        }, 10, true);
        animations[1] = new Animation(new BufferedImage[]{
                sheet.getFrame(3, 0)
        }, 10, true);
        animations[3] = new Animation(new BufferedImage[]{
                sheet.getFrame(3, 0)
        }, 10, true);

        shootAnimation = new Animation(new BufferedImage[]{
                sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                sheet.getFrame(2, 2), sheet.getFrame(2, 3)
        }, 8, false);

        currentAnimation = animations[0];
    }

    @Override
    public void update() {
        if (health <= 0) {
            registry.unregister(this);
            return;
        }
        super.update();

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
            isMoving = true;
            isShooting = false;
            double dx = player.getX() - getX();
            double dy = player.getY() - getY();
            if (Math.abs(dx) >= Math.abs(dy)) {
                direction = dx > 0 ? 2 : 0;
            } else {
                direction = dy > 0 ? 3 : 1;
            }
            Vector fleeVector = new Vector(player.getX(), player.getY(), getX(), getY());
            fleeVector.setLength(getSpeed());
            move(fleeVector);

        } else if (dist <= ATTACK_RANGE) {
            isMoving = false;
            if (shootTimer >= SHOOT_COOLDOWN) {
                shootTimer = 0;
                isShooting = true;
                shootAnimation = new Animation(new BufferedImage[]{
                        sheet.getFrame(2, 0), sheet.getFrame(2, 1),
                        sheet.getFrame(2, 2), sheet.getFrame(2, 3)
                }, 8, false);

                double x = getCenter()[0];
                double y = getCenter()[1];
                Vector shootVector = new Vector(x, y, player.getCenter()[0], player.getCenter()[1]);
                shootVector.setLength(10);
                new EnemyProjectile(x, y, size, size, registry, attackManager,
                        this, 5, shootVector, 120, damage);
            }
        } else {
            isMoving = false;
        }
    }

    @Override
    public BufferedImage getSprite() {
        if (isShooting) return shootAnimation.getCurrentFrame();
        if (currentAnimation == null) return null;
        return currentAnimation.getCurrentFrame();
    }
}