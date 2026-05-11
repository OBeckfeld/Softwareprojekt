package entities.components;

import entities.Entity;
import entities.managers.EntityRegistry;

public class Wall extends Entity {


    public Wall(double x, double y, int height, int width, EntityRegistry registry) {
        super(x, y, height, width, registry);
    }
}
