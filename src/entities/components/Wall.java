package entities.components;

import entities.Entity;
import entities.entityRegistry;

public class Wall extends Entity {


    public Wall(double x, double y, int height, int width, entityRegistry registry) {
        super(x, y, height, width, registry);
    }
}
