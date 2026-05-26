package entities;

import entities.managers.AttackManager;
import entities.managers.EntityRegistry;
import tools.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EnemyProjectile extends Projectile {

    private BufferedImage sprite;

    public EnemyProjectile(double x, double y, int width, int height,
                           EntityRegistry registry, AttackManager attackManager,
                           PlayerTypeEntity owner, double speed,
                           Vector vector, int timeToLive, int damage) {
        super(x, y, width, height, registry, attackManager,
                owner, speed, vector, timeToLive, damage);

        sprite = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = sprite.createGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, width, height);
        g.dispose();
    }

    @Override
    protected void logic() {
        for (Entity entity : registry.getInRange(this, Math.max(width, height))) {
            if (!(entity instanceof PlayerTypeEntity) || entity instanceof Enemy) {
                continue;
            }
            hit();
        }
        move();
    }

    @Override
    public BufferedImage getSprite() {
        return sprite;
    }
}