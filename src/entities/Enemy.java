package entities;

import entities.managers.EntityRegistry;
import tools.Vector;
import tools.SpriteSheet;
import tools.Animation;
import entities.managers.AttackManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

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

    public Enemy(int x, int y, int width, int height, int hitCooldown, EntityRegistry registry, AttackManager attackManager) {
        super(x, y, width, height, 100, 100, registry, attackManager);
        defaultSpeed = 2;
        speed = defaultSpeed;
        viewRange = 500;
        mass = 1;
        health = 100;
        player = null;
        damage = 20;

        sheet = new SpriteSheet("src/sprites/meleeenemy.png", 161, 161);

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

    @Override
    public void update() {
        super.update();
        if (health <= 0) {
            registry.unregister(this);
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
        handleMovement();

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

    protected void handleMovement() {
        if (player == null) {
            isMoving = false;
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

        if (registry.getInRange(this, 100).contains(player)) {
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

    protected void tryAttackEntity(PlayerTypeEntity targetPlayer) {
        direction = getDirectionTo(targetPlayer.getCenter()[0], targetPlayer.getCenter()[1]);
        weapon.use();
    }

    @Override
    public BufferedImage getSprite() {
        if (isAttacking) return attackAnimation.getCurrentFrame();
        if (isMoving) return currentAnimation.getCurrentFrame();
        return idleAnimation.getCurrentFrame();
    }
}