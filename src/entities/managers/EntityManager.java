package entities.managers;

import entities.Entity;

import java.util.ArrayList;

public class EntityManager implements EntityRegistry {
    private ArrayList<Entity> entities;

    public EntityManager(){
        entities = new ArrayList<>();
    }

    @Override
    public void register(Entity entity){ //fügt eine (neu erstellte) Entity in die Liste hinzu
        entities.add(entity);
    }//fügt eine (neu erstellte) Entity in die Liste hinzu
    public void unregister(Entity entity){entities.remove(entity); //entfernt eine Entity aus der entities Liste
    }

    public ArrayList<Entity> getEntities(){return entities;}


}
