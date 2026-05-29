package entities;

import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;

public class Waypoint extends Entity{
    private Player player;
    // Angabe, um zu überprüfen, ob der Waypoint bereits benutzt wurde (sonst wird mehrfach gespeichert)
    private boolean used;

    public Waypoint(double x, double y, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, 40, 40, registry, attackRegistry, tileManager);
        used = false;
    }

    public void update() {
        
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
