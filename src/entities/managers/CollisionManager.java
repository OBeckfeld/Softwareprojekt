package entities.managers;

import entities.Entity;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class CollisionManager {
    private EntityManager entityManager;
    private HashMap<Entity, ArrayList<Entity>> entities;

    /**
     * Erstellt einen neuen CollisionManager und speichert die Referenz
     * auf den EntityManager, um später alle Entities auf Kollisionen zu prüfen.
     */
    public CollisionManager(EntityManager entityManager){
        this.entityManager = entityManager;
        this.entities = new HashMap<>();
    }

    /**
     * Gibt alle Entities zurück, die aktuell mit der übergebenen Entity kollidieren.
     * Falls keine Kollisionen vorhanden sind, wird eine leere Liste zurückgegeben.
     */
    public ArrayList<Entity> getEntities(Entity entity){return entities.getOrDefault(entity, new ArrayList<>());}

    /**
     * Prüft alle registrierten Entities paarweise auf Kollisionen
     * und speichert die gefundenen Kollisionen in einer HashMap.
     */
    public void checkCollisions(){
        entities.clear();
        for(Entity entity : entityManager.getEntities()){
            for(Entity otherEntity : entityManager.getEntities()){
                if(entity != otherEntity && entity.getHurtbox().intersects(otherEntity.getHurtbox())){
                    if(!entities.containsKey(entity)){
                        entities.put(entity, new ArrayList<>());
                    }
                    entities.get(entity).add(otherEntity);
                }
            }
        }
    }

}