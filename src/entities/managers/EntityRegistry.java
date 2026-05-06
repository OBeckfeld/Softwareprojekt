package entities.managers;

import entities.Entity;

public interface EntityRegistry {
    void register(Entity entity);
    void unregister(Entity entity);
}
