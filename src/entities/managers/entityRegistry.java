package entities.managers;

import entities.Entity;

//interface, damit Objekte der Unterklassen von Entity keinen Zugriff auf den gesamten EntityManager haben
public interface entityRegistry {
    void register(Entity entity);
    void unregister(Entity entity);
}
