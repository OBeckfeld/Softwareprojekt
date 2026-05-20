package entities;

import entities.managers.EntityRegistry;

public class Door extends Entity{
    
    private boolean open;

    public Door(int x, int y, EntityRegistry registry) {
        super(x, y, 10, 120, registry);
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
        for (Entity entity : registry.getCollisions(this)) {
            if (entity instanceof Player) {
                registry.getMapLoader().buildMap();
            }
        }
    }

    public boolean isOpen() {
        return open;
    }
}
