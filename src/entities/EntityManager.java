package entities;

import java.util.ArrayList;

public class EntityManager implements entityRegistry {
    private ArrayList<Entity> entities;

    public EntityManager(){
        entities = new ArrayList<>();
    }

    @Override
    public void register(Entity entity){ //fügt eine (neu erstellte) Entity in die Liste hinzu
        entities.add(entity);
    }

    public ArrayList<Entity> getEntities(){return entities;}
}
