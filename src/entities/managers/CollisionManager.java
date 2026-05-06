package entities.managers;

import entities.Entity;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

public class CollisionManager {
    private EntityManager entityManager;
    private HashMap<Entity, ArrayList<Entity>> entities;

    public CollisionManager(EntityManager entityManager){
        this.entityManager = entityManager;
        this.entities = new HashMap<>();
    }

    public ArrayList<Entity> getEntities(Entity entity){return entities.getOrDefault(entity, new ArrayList<>());}

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
