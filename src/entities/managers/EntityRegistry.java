package entities.managers;

import entities.Entity;

import java.util.ArrayList;

//interface, damit Objekte der Unterklassen von Entity keinen Zugriff auf den gesamten EntityManager haben
public interface EntityRegistry {
    /**
     * Registriert eine Entity in der Registry, damit sie im Spiel verwaltet
     * und bei Abfragen berücksichtigt werden kann.
     */
    void register(Entity entity);

    /**
     * Entfernt eine Entity aus der Registry, sodass sie nicht mehr verwaltet
     * oder bei Abfragen berücksichtigt wird.
     */
    void unregister(Entity entity);

    /**
     * Gibt alle Entities zurück, die sich innerhalb eines bestimmten Bereichs
     * um die übergebene Entity befinden.
     */
    ArrayList<Entity> getInRange(Entity entity, int rangeX, int rangeY);

    /**
     * Prüft, ob zwei Entities miteinander kollidieren.
     */
    boolean collidesWith(Entity entity1, Entity entity2);

    /**
     * Gibt alle aktuell registrierten Entities zurück.
     */
    ArrayList<Entity> getEntities();

    /**
     * Gibt alle Entities zurück, die mit der übergebenen Entity kollidieren.
     */
    ArrayList<Entity> getCollisions(Entity entity);
}
