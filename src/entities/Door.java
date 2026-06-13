package entities;

import entities.enemies.Enemy;
import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;

public class Door extends Entity{

    private boolean open;

    public Door(double x, double y, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, 10, 120, registry, attackRegistry, tileManager);
        this.open = false;
    }

    public void update() {
        for (Entity entity : registry.getEntities()) {
            if (entity instanceof Enemy) {
                return;
            }
        }
        open = true;
    }

    public boolean isOpen() {
        return open;
    }
}