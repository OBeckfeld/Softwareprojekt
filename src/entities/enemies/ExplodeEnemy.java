package entities.enemies;

import entities.Entity;
import entities.PlayerTypeEntity;
import entities.managers.EntityRegistry;
import entities.managers.AttackManager;
import main.GamePanel;
import tools.TileManager;
import tools.Vector;
import java.util.ArrayList;

public class ExplodeEnemy extends Enemy {

    private static final int EXPLODE_RANGE = 50;
    private static final int BLAST_RADIUS  = 100;
    private static final int EXPLODE_DMG   = 50;
    private boolean exploded = false;

    public ExplodeEnemy(int x, int y, int width, int height,
                        EntityRegistry registry, AttackManager attackManager, TileManager tileManager, GamePanel gamePanel) {
        super(x, y, width, height, 0, registry, attackManager, tileManager, gamePanel);
        currentHealth = 20;
        setSpeed(3);
        viewRange = 300;
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
        ArrayList<Entity> inRange = registry.getInRange(this, BLAST_RADIUS, BLAST_RADIUS);
        for (Entity entity : inRange) {
            if (entity instanceof PlayerTypeEntity && !(entity instanceof ExplodeEnemy)) {
                ((PlayerTypeEntity) entity).setCurrentHealth(
                        ((PlayerTypeEntity) entity).getCurrentHealth() - EXPLODE_DMG
                );
            }
        }
        registry.unregister(this);
    }
}