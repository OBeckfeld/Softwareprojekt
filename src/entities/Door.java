package entities;

import entities.managers.AttackManager;
import entities.managers.EntityRegistry;
import tools.TileManager;

public class Door extends Entity{
    
    private boolean open;

    public Door(double x, double y, EntityRegistry registry, AttackManager attackManager, TileManager tileManager) {
        super(x, y, 10, 120, registry, attackManager, tileManager);
        this.open = false;
    }

    public void update() {
        for (Entity entity : registry.getEntities()) {
            if (entity instanceof Enemy) {
                return;
            }
        }
        open = true;
        for (Entity entity : registry.getCollisions(this)) {
            if (entity instanceof Player) {
                String mapLoader = "Load new Map"; //Platzhalter für Maploader
            }
        }
    }

    public boolean isOpen() {
        return open;
    }
}
