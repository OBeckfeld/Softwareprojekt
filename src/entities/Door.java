package entities;

import entities.enemies.Enemy;
import entities.managers.AttackManager;
import entities.managers.EntityRegistry;
import tools.TileManager;

public class Door extends Entity{
    
    private boolean open;

    public Door(int x, int y, EntityRegistry registry, AttackManager attackManager, TileManager tileManager) {
        super(x, y, 10, 120, registry, tileManager);
        this.open = false;
    }

    public void update() {
        //Die Tür öffnet sich, sobald alle Enemies besiegt sind
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
