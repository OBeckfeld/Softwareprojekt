package entities.managers;

import entities.Entity;

import java.util.ArrayList;

//interface, damit Objekte der Unterklassen von Entity keinen Zugriff auf den gesamten EntityManager haben
public interface EntityRegistry {
    void register(Entity entity);
    void unregister(Entity entity);
    ArrayList<Entity> getInRange(Entity entity, int range);
    boolean collidesWith(Entity entity1, Entity entity2);
}
