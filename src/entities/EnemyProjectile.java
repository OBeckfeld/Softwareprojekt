package entities;

import entities.enemies.Enemy;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;
import tools.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyProjectile extends Projectile {

    private BufferedImage sprite;

    public EnemyProjectile(double x, double y, int width, int height,
                           EntityRegistry registry, AttackRegistry attackRegistry,
                           PlayerTypeEntity owner, double speed,
                           Vector vector, int timeToLive, int damage, TileManager tileManager) {
        super(x, y, width, height, registry, attackRegistry,
                owner, speed, vector, timeToLive, damage, tileManager);

        sprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    @Override
    protected void logic() {
        for (Entity entity : registry.getInRange(this, Math.max(width, height), Math.max(width, height))) {
            if (!(entity instanceof PlayerTypeEntity) || entity instanceof Enemy) {
                continue;
            }
            hit();
            return;
        }
        if (movement.collidesWithWall(this, moveVector.getOffsetX(), moveVector.getOffsetY())){
            hit();
            return;
        }
        move();
    }

    protected void hit() {

        attackRegistry.attack(owner, x, y, height + 2, width + 2, 2, damage);
        attackRegistry.distributeDamage();
        unregister();
    }

    public BufferedImage getSprite() {
        return sprite;
    }
}

