package entities.enemies;

import entities.Entity;
import entities.PlayerTypeEntity;
import entities.managers.EntityRegistry;
import entities.managers.AttackRegistry;
import main.GamePanel;
import tools.TileManager;
import tools.Vector;
import java.util.ArrayList;

public class ExplodeEnemy extends Enemy {

    private static final int EXPLODE_RANGE = 50;
    private final int blastRadius;
    private final int explodeDamage;
    private boolean exploded = false;

    public ExplodeEnemy(int x, int y, int width, int height,
                        EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 0, registry, attackRegistry, tileManager, gamePanel);
        currentHealth = 20;
        blastRadius = 100;
        explodeDamage = 50;
        setSpeed(3);
        viewRange = 300;
        pointsOnDeath = 3;
    }

    public ExplodeEnemy(int x, int y, int width, int height, int damage, int blastRadius, int viewRange, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 0, registry, attackRegistry, tileManager, gamePanel);
        currentHealth = 20;
        this.blastRadius = blastRadius;
        this.explodeDamage = damage;
        this.viewRange = viewRange;
        setSpeed(3);
        pointsOnDeath = 3;
    }

    @Override
    protected void handleMovement() {
        if (player == null || exploded) return;
        Vector toPlayer = new Vector(getX(), getY(), player.getX(), player.getY());

        if (toPlayer.getLength() <= EXPLODE_RANGE) {
            explode();
        } else {
            toPlayer.setLength(getSpeed());
            move(toPlayer);
        }
    }

    private void explode() {
        exploded = true;
        ArrayList<Entity> inRange = registry.getInRange(this, blastRadius, blastRadius);
        for (Entity entity : inRange) {
            if (entity instanceof PlayerTypeEntity && !(entity instanceof ExplodeEnemy)) {
                ((PlayerTypeEntity) entity).setCurrentHealth(
                        ((PlayerTypeEntity) entity).getCurrentHealth() - explodeDamage
                );
            }
        }
        registry.unregister(this);
    }
}