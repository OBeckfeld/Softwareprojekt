package entities.managers;

import entities.Entity;

import java.util.ArrayList;

public class EntityManager implements EntityRegistry {
    private ArrayList<Entity> entities;

    public EntityManager(){
        entities = new ArrayList<>();
    }

    @Override
    public void register(Entity entity){
        entities.add(entity);
    }
    public void unregister(Entity entity){entities.remove(entity);
    }

    public ArrayList<Entity> getEntities(){return entities;}


}
