package entities;

import entities.managers.AttackRegistry;
import entities.managers.EntityRegistry;
import tools.TileManager;

public class Waypoint extends Entity{
    Player player;

    public Waypoint(double x, double y, EntityRegistry registry, AttackRegistry attackRegistry, TileManager tileManager) {
        super(x, y, 10, 10, registry, attackRegistry, tileManager);
    }
}
