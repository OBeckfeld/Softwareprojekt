package entities.managers;

import entities.Entity;
import entities.ViewBox;

import java.util.ArrayList;

public class EntityManager implements EntityRegistry {
    private ArrayList<Entity> entities;
    CollisionManager collisionManager;

    public EntityManager(){

        entities = new ArrayList<>();
    }

    @Override
    public void register(Entity entity){
        if (entity != null) {
            entities.add(entity);
        }
    }

    public void unregister(Entity entity){entities.remove(entity);
    }

    public ArrayList<Entity> getEntities(){return new ArrayList<>(entities);}//damit die eigentliche Liste nicht von anderen Klassen bearbeitet werden kann

    public ArrayList<Entity> getInRange(Entity entity, int range){//gibt alle entities zurück, die im sichtfeld von entity sind
        ViewBox viewBox = new ViewBox((entity.getCenter() [0]-range/2), (entity.getCenter() [1]-range/2), range, range,  this);//viewBox wird zentriert
        collisionManager.checkCollisions();//collisions werden geupdatet, da die viewbox am anfang nicht da war | könnte das gameplay langsamer machen
        ArrayList<Entity> colls = collisionManager.getEntities(viewBox);//Entities checken, die in range sind

        entities.remove(viewBox);//view box wieder entfernen

        colls.remove(entity);//die original Entity wird nicht zu den Entities, die gesehen werden ,gezählt

        return colls;
    }
    public void setCollisions(CollisionManager collisionManager){
        this.collisionManager = collisionManager;
    }
    public boolean collidesWith(Entity entity1, Entity entity2){ return collisionManager.getEntities(entity1).contains(entity2);}
}
